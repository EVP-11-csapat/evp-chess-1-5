package chess15.algorithm;

import chess15.util.Move;

import java.util.HashMap;

public class TranspositionTable {

    public static final byte EXACT = 0;
    public static final byte LOWER_BOUND = 1;
    public static final byte UPPER_BOUND = 2;

    public static final int LOOKUPFAILED = Integer.MIN_VALUE;
    private static final HashMap<Long, TableEntry> table;

    public static class TableEntry {
        int score;

        byte nodeType;
        int depth;
        Move bestMove;

        public TableEntry(byte nodeType, int score, int depth, Move bestMove) {
            this.nodeType = nodeType;
            this.score = score;
            this.depth = depth;
            this.bestMove = bestMove;
        }

        public int getCorrectedScore(int depth, int plyFromRoot, int alpha, int beta) {
            if (this.depth >= depth) {
                int correctedScore = correctRetrievedWinEval(this.score, plyFromRoot);
                if (nodeType == TranspositionTable.EXACT) {

                    // Cached at PV-Node but alpha-beta range could change
                    if (score >= beta) return beta; // respect Fail-hard beta cutoff (Cut-Node)
                    if (score <= alpha) return alpha; // Fail-hard fail-low (All-Node)
                    return correctedScore; // Within the window (PV-Node)
                } else if (nodeType == TranspositionTable.UPPER_BOUND && correctedScore <= alpha) {
                    // All-Node
                    return alpha;
                } else if (nodeType == TranspositionTable.LOWER_BOUND && correctedScore >= beta) {
                    // Cut-Node
                    return beta;
                }
            }
            return TranspositionTable.LOOKUPFAILED;
        }
    }

    private static int correctWinEvalForStorage(int score, int depth) {
        if (ChessAlgorithm.isWinScore(score)) {
            int sign = (int) Math.signum((float) score);
            return (score * sign + depth) * sign;
        }
        return score;
    }

    private static int correctRetrievedWinEval(int score, int depth) {
        if (ChessAlgorithm.isWinScore(score)) {
            int sign = (int) Math.signum((float) score);
            return (score * sign - depth) * sign;
        }
        return score;
    }


    static {
        table = new HashMap<>();
    }

    public static void store(long hash, byte nodeType, int score, int depth, int plyFromRoot, Move bestMove) {
        table.put(hash, new TableEntry(nodeType, correctWinEvalForStorage(score, plyFromRoot), depth, bestMove));
    }

    public static TableEntry retrieve(long hash) {
        return table.get(hash);
    }
}
