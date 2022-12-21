package chess15.algorithm;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;

public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;

    @Override
    public Vector2[] move(Board positions) {

        Engine engine = new Engine(rules, positions, color);
        Vector2[] bestMove = engine.getRandomMove();

        return bestMove;
    }

    ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
    }
}
