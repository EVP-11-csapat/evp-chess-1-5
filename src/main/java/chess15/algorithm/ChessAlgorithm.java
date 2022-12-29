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

/**
 * ChessAlgorithm is the class that contains the algorithm for the game.
 */
public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;

    private static final int startDepth = 3;
    private static int searchDepth = 3;
    private static final int mateScore = 100000;

    private static final int infinity = 1073741824;
    private SearchTree tree;

    private Move bestMove;

    /**
     * Calculate the best move for the black pieces.
     * @param positions The current {@link Board} positions.
     * @param playerMove The {@link Move} the player made.
     * @return The best {@link Move} for the black pieces.
     */
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

        long startTime = System.nanoTime();

        searchDepth = startDepth;
        while (System.nanoTime() - startTime < 2000000000){
            minmax(0, engine, -infinity, infinity);
            searchDepth++;
        }

        System.out.println("Searched to depth of " + (searchDepth - 1));
        return bestMove;
    }

    /**
     * Constructor for the ChessAlgorithm class.
     * @param rules The {@link RuleSet} of the game.
     * @param player The {@link Piece.Color} of the pieces the algorithm plays.
     */
    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
        if (rules.gamemode instanceof Classical) {
            tree = new SearchTree();
        }
    }

    /**
     * The minmax algorithm used to calculate the best move.
     * @param depth The current depth of the search.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @param alpha The alpha value.
     * @param beta The beta value.
     * @return The score of the best move.
     */
    private int minmax(int depth, Engine engine, int alpha, int beta) {

        if(depth > 0){
            alpha = Math.max(alpha, -mateScore + depth);
            beta = Math.min(beta, mateScore - depth);
            if (alpha >= beta) {
                return alpha;
            }
        }

        if (depth == searchDepth) {
            return searchCaptures (engine, alpha, beta);
        }

        ArrayList<Move> moves = orderMoves(engine);

        if (moves.size() == 0) {
            if (engine.inCheck){
                return -1 * (mateScore - depth);
            }
            else return 0;
        }

        for (Move move : moves) {
            Engine copiedEngine = new Engine(engine);
            copiedEngine.move(move.from, move.to);


            int score = -minmax(depth + 1, copiedEngine, -beta, -alpha);

            if (score >= beta) return beta;


            if (score > alpha) {

                alpha = score;
                if (depth == 0) {
                    bestMove = move;
                }
            }
        }

        return alpha;
    }

    /**
     * Search for captures and return the score of the best capture.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @param alpha The alpha value.
     * @param beta The beta value.
     * @return The score of the best capture.
     */
    private int searchCaptures(Engine engine, int alpha, int beta) {
        int score = ScoreEvaluator.evaluate(engine);

        if (score >= beta) {
            return beta;
        }
        if (score > alpha) {
            alpha = score;
        }


        ArrayList<Move> moves = orderMoves(engine);

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


    /**
     * Order the moves based on the score of the move.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @return The ordered list of moves.
     */
    private ArrayList<Move> orderMoves(Engine engine) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                moves.add(new Move(start, end, scoreMove(start, end, engine.getBoard())));
            }
        }

        moves.sort(new SortByScore().reversed());

        return moves;
    }

    /**
     * Score a move based on the piece that is moved.
     * @param start The start {@link Vector2} position of the move.
     * @param end The end {@link Vector2} position of the move.
     * @param board The {@link Board} the move is made on.
     * @return The score of the move.
     */
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

    /**
     * The {@link Comparator} used to sort the moves.
     */
    static class SortByScore implements Comparator<Move> {
        /**
         * Compare two moves based on their score.
         * @param a the first object to be compared.
         * @param b the second object to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
         */
        @Override
        public int compare(Move a, Move b) {
            return a.score - b.score;
        }
    }


}
