package chess15.algorithm;

import chess15.Board;
import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import chess15.util.Move;
import chess15.util.PiecePoints;

import java.util.ArrayList;
import java.util.Comparator;

public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;
    private static final int mateScore = 100000;

    private static final int infinity = 1073741824;
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

        for (int i = 1; i < 6; i++) {
            minmax(i, 0, engine, -infinity, infinity);
        }

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
        if (rules.gamemode instanceof Classical) {
            tree = new SearchTree();
        }
    }

    private int minmax(int depth, int plyFromRoot, Engine engine, int alpha, int beta) {

        long hash = Zobrist.calculateHash(engine);

        TranspositionTable.TableEntry entry = TranspositionTable.retrieve(hash);

        Move storedmove = null;

        if (entry != null) {
            int score = entry.getCorrectedScore(depth, plyFromRoot, alpha, beta);
            storedmove = entry.bestMove;
            if (score != TranspositionTable.LOOKUPFAILED) {
                if (plyFromRoot == 0) bestMove = entry.bestMove;
                return score;
            }
        }


        if (plyFromRoot > 0) {
            alpha = Math.max(alpha, -mateScore + plyFromRoot);
            beta = Math.min(beta, mateScore - plyFromRoot);
            if (alpha >= beta) {
                return alpha;
            }
        }

        if (depth == 0) {
            return searchCaptures(engine, alpha, beta);
        }

        ArrayList<Move> moves = orderMoves(engine, storedmove);

        if (moves.size() == 0) {
            if (engine.inCheck) {
                return -1 * (mateScore - plyFromRoot);
            } else return 0;
        }

        byte evalType = TranspositionTable.UPPER_BOUND;
        Move localBestMove = null;

        for (Move move : moves) {
            Engine copiedEngine = new Engine(engine);
            copiedEngine.move(move.from, move.to);


            int score = -minmax(depth - 1, plyFromRoot + 1, copiedEngine, -beta, -alpha);

            if (score >= beta) {
                TranspositionTable.store(hash, TranspositionTable.LOWER_BOUND, beta, depth, plyFromRoot, move);
                return beta;
            }

            // Found a new best move in this position
            if (score > alpha) {
                evalType = TranspositionTable.EXACT;
                localBestMove = move;

                alpha = score;
                if (plyFromRoot == 0) {
                    bestMove = move;
                }
            }
        }

        TranspositionTable.store(hash, evalType, alpha, depth, plyFromRoot, localBestMove);

        return alpha;
    }

    private int searchCaptures(Engine engine, int alpha, int beta) {
        int score = ScoreEvaluator.evaluate(engine);

        if (score >= beta) {
            return beta;
        }
        if (score > alpha) {
            alpha = score;
        }


        ArrayList<Move> moves = orderMoves(engine, null);

        for (Move move : moves) {
            if (!engine.board.at(move.to).isEmpty) {
                if (!((Piece) engine.board.at(move.to)).isKing) {
                    Engine copiedEngine = new Engine(engine);
                    copiedEngine.move(move.from, move.to);
                    score = -searchCaptures(copiedEngine, -beta, -alpha);

                    if (score >= beta) {
                        return beta;
                    }
                    if (score > alpha) {
                        alpha = score;
                    }
                }
            }
        }

        return alpha;
    }

    public static boolean isWinScore(int score) {
        final int maxWinDepth = 1000;
        return Math.abs(score) > mateScore - maxWinDepth;
    }


    private ArrayList<Move> orderMoves(Engine engine, Move storedMove) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                if (storedMove != null && start == storedMove.from && end == storedMove.to) {
                    moves.add(new Move(start, end, 10000));
                } else moves.add(new Move(start, end, scoreMove(start, end, engine.getBoard())));
            }
        }

        moves.sort(new SortByScore().reversed());

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
                score -= 350;
            }
        } else {
            if (!right.outOfBounds() && !board.at(right).isEmpty) {
                Piece rightPawn = (Piece) board.at(right);
                if (rightPawn.movement instanceof Pawn && rightPawn.color == Engine.switchColor(p.color)) {
                    score -= 350;
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
