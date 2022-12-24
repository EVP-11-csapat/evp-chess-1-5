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

    private final int searchDepth = 5;

    private SearchTree tree;

    private Move bestMove;

    @Override
    public Move move(Board positions, Move playerMove) {

        boolean exists = false;
        SearchTree.Node answer = null;


        for (SearchTree.Node node : tree.root.children) {
            if(node.white.equals(playerMove)){
                exists = true;
                answer = node;
                break;
            }
        }

        if(exists){
            tree.root = answer;
            return answer.black;
        }


        Engine engine = new Engine(rules, positions, color);

        minmax(0, engine, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
        if(rules.gamemode instanceof Classical){
            tree = new SearchTree();
        }
    }

    private int minmax(int depth, Engine engine, boolean max, int alpha, int beta) {
        int selectedScore = (max) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        ArrayList<Move> moves = orderMoves(engine);

        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                Engine copiedEngine = new Engine(engine);
                copiedEngine.move(start, end);

                int score = (depth == searchDepth - 1) ? copiedEngine.score(color) : minmax(depth + 1, copiedEngine, !max, alpha, beta);

                if (max) {
                    if (score > selectedScore) {
                        selectedScore = score;
                        if (depth == 0) {
                            bestMove = new Move(start,end);
                        }
                    }
                    alpha = Math.max(alpha, selectedScore);
                } else {
                    selectedScore = Math.min(score, selectedScore);
                    beta = Math.min(beta, selectedScore);
                }
                if (beta <= alpha) return selectedScore;
            }
        }

        return selectedScore;
    }

    private ArrayList<Move> orderMoves(Engine engine) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                moves.add(new Move(start, end, scoreMove(start, end, engine.getBoard())));
            }
        }
        moves.sort(new SortByScore());

        return moves;
    }

    private int scoreMove(Vector2 start, Vector2 end, Board board) {
        int score = 0;
        Piece p = (Piece) board.at(start);

        if (!board.at(end).isEmpty) {
            score = 10 * PiecePoints.evaluate((Piece) board.at(end)) - PiecePoints.evaluate(p);
        }

        if (p.movement instanceof Pawn){
            if(p.color == Piece.Color.WHITE && end.y == 0 || p.color == Piece.Color.BLACK && end.y == 7) score += 9;
        }

        int y = (p.color == Piece.Color.WHITE) ? -1 : 1;

        Vector2 left = new Vector2(end.x - 1, y);
        Vector2 right = new Vector2(end.x + 1, y);

        if(!left.outOfBounds() && !board.at(left).isEmpty){
            Piece leftPawn = (Piece) board.at(left);
            if(leftPawn.movement instanceof Pawn && leftPawn.color == Engine.switchColor(p.color)){
                score -= PiecePoints.evaluate(p);
            }
        }
        else{
            if(!right.outOfBounds() && !board.at(right).isEmpty){
                Piece rightPawn = (Piece) board.at(right);
                if(rightPawn.movement instanceof Pawn && rightPawn.color == Engine.switchColor(p.color)){
                    score -= PiecePoints.evaluate(p);
                }
            }
        }


        return score;
    }

    class SortByScore implements Comparator<Move> {

        @Override
        public int compare(Move a, Move b) {
            return b.score - a.score;
        }
    }


}
