package chess15.algorithm;

import chess15.Board;
import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import chess15.gui.util.Constants;
import chess15.util.Move;
import chess15.util.PiecePoints;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ChessAlgorithm is the class that contains the algorithm for playing the game.
 */
public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;
    private static final int mateScore = 100000;

    private static final int infinity = 1073741824;
    private SearchTree tree;

    private Move bestMove;
    private Move bestMoveInIteration;

    /**
     * Calculate the best possible move from the current board setup and the opponents last move.
     * @param positions The current {@link Board} positions.
     * @param playerMove The {@link Move} the user made.
     * @return The best {@link Move} for the computer's pieces.
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
                if (Constants.DEVMODE)
                    System.out.println("going dark!");
            }
        }


        Engine engine = new Engine(rules, positions, color);
        Thread searchThread = new Thread(() -> {
            int i = 1;
            try {

                while (!Thread.currentThread().isInterrupted()) {
                    if (Constants.DEVMODE)
                        System.out.println("starting iteration " + i);
                    minmax(i, 0, engine, -infinity, infinity);
                    bestMove = bestMoveInIteration;
                    if (Constants.DEVMODE)
                        System.out.println("completed iteration " + i);
                    i++;
                }
            } catch (InterruptedException ignored) {

            }
        });

        searchThread.start();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            searchThread.interrupt();
            if (Constants.DEVMODE)
                System.out.println("time's up\n");
        }, 3, TimeUnit.SECONDS);

        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {

        } finally {
            executor.shutdown();
        }
        return bestMove;
    }

    /**
     * Constructor for the ChessAlgorithm class.
     * @param rules The {@link RuleSet} of the game.
     * @param player The {@link Piece.Color} of the side the computer plays.
     */
    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
        if (rules.gamemode instanceof Classical) {
            tree = new SearchTree();
        }
    }

    /**
     * The negamax algorithm used to calculate the best move.
     * @param depth The current depth of the search. Starts from the desired number and ends on 0.
     * @param plyFromRoot Number of moves made from the inital Board positions in the current search depth.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @return The score of the best move in the current depth level.
     */
    private int minmax(int depth, int plyFromRoot, Engine engine, int alpha, int beta) throws InterruptedException {

        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

        long hash = Zobrist.calculateHash(engine);

        TranspositionTable.TableEntry entry = TranspositionTable.retrieve(hash);

        Move storedmove = null;

        if (entry != null) {
            int score = entry.getCorrectedScore(depth, plyFromRoot, alpha, beta);
            storedmove = entry.bestMove;
            if (score != TranspositionTable.LOOKUPFAILED) {
                if (plyFromRoot == 0) bestMoveInIteration = entry.bestMove;
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

            if (score > alpha) {
                evalType = TranspositionTable.EXACT;
                localBestMove = move;

                alpha = score;
                if (plyFromRoot == 0) {
                    bestMoveInIteration = move;
                }
            }
        }

        TranspositionTable.store(hash, evalType, alpha, depth, plyFromRoot, localBestMove);

        return alpha;
    }

    /**
     * Searches for a quiet position (i.e. no captures avaliable on the next move) to evaluate.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @return The score of the best possible move, or when a quiet position is reached the evaluation of that position.
     */
    private int searchCaptures(Engine engine, int alpha, int beta) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();


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

    /**
     * Returns if the score is for a move that leads to a forced mate.
     * @param score The score to check.
     * @return True if the score is for a move that leads to a forced mate..
     */
    public static boolean isWinScore(int score) {
        final int maxWinDepth = 1000;
        return Math.abs(score) > mateScore - maxWinDepth;
    }

    /**
     * Order the moves based on the estimated score of the moves.
     * @param engine The {@link Engine} used to calculate legal moves.
     * @return The ordered list of moves.
     */
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

    /**
     * Score a move based on estimates.
     * @param start The start {@link Vector2} position of the move.
     * @param end The end {@link Vector2} position of the move.
     * @param board The {@link Board} the move is made on.
     * @return The estimated score of the move.
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
