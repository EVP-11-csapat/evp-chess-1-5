package chess15.algorithm;

import chess15.util.Move;

import java.util.HashMap;

/**
 * Transpositon Table for storing already evaluated positions.
 */

public class TranspositionTable {

    public static final byte EXACT = 0;
    public static final byte LOWER_BOUND = 1;
    public static final byte UPPER_BOUND = 2;

    public static final int LOOKUPFAILED = Integer.MIN_VALUE;
    private final HashMap<Long, TableEntry> table;

    /**
     * Entry in the {@link TranspositionTable}.
     */
    public static class TableEntry {
        int score;

        byte nodeType;
        int depth;
        Move bestMove;

        /**
         * Constructor for the {@link TableEntry} class.
         *
         * @param nodeType The relationship between the score and the alpha/beta parameters of the search. EXACT, LOWER_BOUND or UPPER_BOUND
         * @param score    The value of the position. (may not be the node's true value see: <code>nodeType</code>)
         * @param depth    The depth used to calculate the score.
         * @param bestMove The best move that was selected for this position during the search.
         */

        public TableEntry(byte nodeType, int score, int depth, Move bestMove) {
            this.nodeType = nodeType;
            this.score = score;
            this.depth = depth;
            this.bestMove = bestMove;
        }

        /**
         * Gets the corrected score from a node.
         * Taking into account the node's type and wheter or not the score is for a mate.
         * The corrected score is calculated using the alpha/beta parameters from the search.
         *
         * @param depth       The depth of the current search. The node has to have an equal or higher depth to be useful.
         * @param plyFromRoot The number of moves from the start of the search to reach this position. Used to calculate the corrected score for a mate.
         * @param alpha       The alpha value of the search.
         * @param beta        The beta value of the search.
         * @return The score of the node after alpha-beta pruning.
         */
        public int getCorrectedScore(int depth, int plyFromRoot, int alpha, int beta) {
            if (this.depth >= depth) {
                int correctedScore = correctRetrievedMateScore(this.score, plyFromRoot);
                if (nodeType == TranspositionTable.EXACT) {

                    if (score >= beta) return beta;
                    if (score <= alpha) return alpha;
                    return correctedScore;
                } else if (nodeType == TranspositionTable.UPPER_BOUND && correctedScore <= alpha) {
                    return alpha;
                } else if (nodeType == TranspositionTable.LOWER_BOUND && correctedScore >= beta) {
                    return beta;
                }
            }
            return TranspositionTable.LOOKUPFAILED;
        }
    }


    /**
     * Method to store a forced mate without considering the depth it was found at.
     * A search using this score from the {@link TranspositionTable} may not find this position on the same depth as the search that stored it.
     * The corrected score is essentialy the score for a mate in 1 with the correct sign.
     *
     * @param score       The score to correct.
     * @param plyFromRoot The number of moves from the start of the search to reach this position.
     * @return The corrected score. (If the score wasn't for a mate the original score)
     */
    private static int correctMateScoreForStorage(int score, int plyFromRoot) {
        if (ChessAlgorithm.isWinScore(score)) {
            int sign = (int) Math.signum((float) score);
            return (score * sign + plyFromRoot) * sign;
        }
        return score;
    }

    /**
     * Method to add the depth back into the score of a stored mate.
     *
     * @param score       The score to correct.
     * @param plyFromRoot The number of moves from the start of the search to reach this position.
     * @return The corrected score. (If the score wasn't for a mate the original score)
     */
    private static int correctRetrievedMateScore(int score, int plyFromRoot) {
        if (ChessAlgorithm.isWinScore(score)) {
            int sign = (int) Math.signum((float) score);
            return (score * sign - plyFromRoot) * sign;
        }
        return score;
    }


    /**
     * Method to store values in the {@link TranspositionTable}
     *
     * @param hash        The {@link Zobrist} hash value of the board.
     * @param nodeType    The relationship between the score and the alpha/beta parameters of the search. EXACT, LOWER_BOUND or UPPER_BOUND
     * @param score       The value of the position. (may not be the node's true value see: <code>nodeType</code>)
     * @param depth       The depth used to calculate the score.
     * @param plyFromRoot The number of moves from the start of the search to reach this position.
     * @param bestMove    The best move that was selected for this position during the search.
     */
    public void store(long hash, byte nodeType, int score, int depth, int plyFromRoot, Move bestMove) {
        table.put(hash, new TableEntry(nodeType, correctMateScoreForStorage(score, plyFromRoot), depth, bestMove));
    }

    /**
     * Method to get an entry from the {@link TranspositionTable}
     *
     * @param hash The {@link Zobrist} hash value of the board.
     * @return The {@link TableEntry} mapped to the Board.
     */
    public TableEntry retrieve(long hash) {
        return table.get(hash);
    }

    /**
     * Constructor for the {@link TranspositionTable} class.
     * Creates an empty table.
     */

    public TranspositionTable() {
        table = new HashMap<>();
    }
}
