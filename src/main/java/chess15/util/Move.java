package chess15.util;

import chess15.Piece;
import chess15.Vector2;

/**
 * The Move class is used to package the from, to, and color of a move
 */
public class Move {
    public Vector2 from;
    public Vector2 to;
    public Piece.Color color;

    /**
     * Create the move class with all parameters
     * @param from The {@link Vector2} position of where the piece was moved from
     * @param to The {@link Vector2} position of where the piece was moved to
     * @param color The {@link chess15.Piece.Color} of the side that played the move
     */
    public Move(Vector2 from, Vector2 to, Piece.Color color) {
        this.from = from;
        this.to = to;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Move: {From: " + from + ", To: "  + to + ", Color: " + color + "}";
    }
}
