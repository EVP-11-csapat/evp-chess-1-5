package chess15.algorithm;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.util.BoardVisualizer;

public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;

    private final int searchDepth = 5;

    private Vector2[] bestMove;

    @Override
    public Vector2[] move(Board positions) {


        Engine engine = new Engine(rules, positions, color);

        bestMove = new Vector2[2];

        minmax(0, engine, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
    }

    private int minmax(int depth, Engine engine, boolean max, int alpha, int beta) {
        int selectedScore = (max) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                Engine copiedEngine = new Engine(engine);
                copiedEngine.move(start, end);

                int score = (depth == searchDepth - 1) ? copiedEngine.score(color) : minmax(depth + 1, copiedEngine, !max, alpha, beta);

                if (max) {
                    if (score > selectedScore) {
                        selectedScore = score;
                        if (depth == 0) {
                            bestMove[0] = start;
                            bestMove[1] = end;
                        }
                    }
                    alpha = Math.max(alpha, selectedScore);
                } else {
                    selectedScore = Math.min(score, selectedScore);
                    beta = Math.min(beta, selectedScore);
                }
                if(beta <= alpha) return selectedScore;
            }
        }

        return selectedScore;
    }


}
