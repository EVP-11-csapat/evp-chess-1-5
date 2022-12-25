package chess15.algorithm;

import chess15.Board;
import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import chess15.util.BoardVisualizer;
import chess15.util.Move;
import chess15.util.PiecePoints;

import java.util.ArrayList;
import java.util.Comparator;

public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;

    private static final int searchDepth = 4;
    private static final int mateScore = 100000;
    private SearchTree tree;

    private Move bestMove;

    @Override
    public Move move(Board positions, Move playerMove) {

        if (tree != null) {
            boolean exists = false;
            SearchTree.Node answer = null;

            for (SearchTree.Node node : tree.root.children) {
                if (node.white.equals(playerMove)) {
                    exists = true;
                    answer = node;
                    break;
                }
            }

            if (exists) {
                tree.root = answer;
                return answer.black;
            } else {
                tree = null;
                System.out.println("going dark!");
            }
        }

        Engine engine = new Engine(rules, positions, color);

        minmax(0, engine, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
        if (rules.gamemode instanceof Classical) {
            tree = new SearchTree();
        }
    }

    private int minmax(int depth, Engine engine, boolean max, int alpha, int beta) {
        int selectedScore = (max) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        ArrayList<Move> moves = orderMoves(engine, max);

        if (moves.size() == 0) {
            if (engine.inCheck){
                int mate = mateScore - depth;
                return (max) ? -mate : mate;
            }
            else return 0;
        }

        for (Move move : moves) {
            Engine copiedEngine = new Engine(engine);
            try{
                copiedEngine.move(move.from, move.to);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println(move);
                BoardVisualizer.printBoard(engine.board);
            }

            int score = (depth == searchDepth - 1) ? searchCaptures(copiedEngine, !max, alpha, beta, depth) : minmax(depth + 1, copiedEngine, !max, alpha, beta);

            if (max) {
                if (score > selectedScore) {
                    selectedScore = score;
                    if (depth == 0) {
                        bestMove = move;
                    }
                }
                alpha = Math.max(alpha, selectedScore);
            } else {
                selectedScore = Math.min(score, selectedScore);
                beta = Math.min(beta, selectedScore);
            }
            if (beta <= alpha) return selectedScore;
        }

        return selectedScore;
    }

    private int searchCaptures(Engine engine, boolean max, int alpha, int beta, int depth) {
        int score = ScoreEvaluator.evaluate(engine);

        if(depth == searchDepth + 1) return score;

        if (max) {
            alpha = Math.max(alpha, score);
        } else {
            beta = Math.min(beta, score);
        }
        if (beta <= alpha) return score;

        ArrayList<Move> moves = orderMoves(engine, max);

        if (moves.size() == 0) {
            if (engine.inCheck){
                int mate = mateScore - depth;
                return (max) ? -mate : mate;
            }
            else return 0;
        }

        int selectedScore = score;

        for (Move move : moves) {
            if (!engine.board.at(move.to).isEmpty) {
                if (!((Piece) engine.board.at(move.to)).isKing) {
                    Engine copiedEngine = new Engine(engine);
                    copiedEngine.move(move.from, move.to);
                    score = searchCaptures(copiedEngine, !max, alpha, beta, depth += 1);
                    if (max) {
                        if (score > selectedScore) {
                            selectedScore = score;
                        }
                        alpha = Math.max(alpha, selectedScore);
                    } else {
                        selectedScore = Math.min(score, selectedScore);
                        beta = Math.min(beta, selectedScore);
                    }
                }
            }
        }

        return selectedScore;
    }


    private ArrayList<Move> orderMoves(Engine engine, boolean max) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                moves.add(new Move(start, end, scoreMove(start, end, engine.getBoard())));
            }
        }
        if (max) {
            moves.sort(new SortByScore().reversed());
        } else moves.sort(new SortByScore());

        return moves;
    }

    private int scoreMove(Vector2 start, Vector2 end, Board board) {
        int score = 0;
        Piece p = (Piece) board.at(start);

        if (!board.at(end).isEmpty) {
            score = 10 * PiecePoints.evaluate((Piece) board.at(end)) - PiecePoints.evaluate(p);
        }

        if (p.movement instanceof Pawn) {
            if (p.color == Piece.Color.WHITE && end.y == 0 || p.color == Piece.Color.BLACK && end.y == 7) score += 9;
        }

        int y = (p.color == Piece.Color.WHITE) ? -1 : 1;

        Vector2 left = new Vector2(end.x - 1, y);
        Vector2 right = new Vector2(end.x + 1, y);

        if (!left.outOfBounds() && !board.at(left).isEmpty) {
            Piece leftPawn = (Piece) board.at(left);
            if (leftPawn.movement instanceof Pawn && leftPawn.color == Engine.switchColor(p.color)) {
                score -= PiecePoints.evaluate(p);
            }
        } else {
            if (!right.outOfBounds() && !board.at(right).isEmpty) {
                Piece rightPawn = (Piece) board.at(right);
                if (rightPawn.movement instanceof Pawn && rightPawn.color == Engine.switchColor(p.color)) {
                    score -= PiecePoints.evaluate(p);
                }
            }
        }

        return score;
    }

    static class SortByScore implements Comparator<Move> {

        @Override
        public int compare(Move a, Move b) {
            return a.score - b.score;
        }
    }


}
