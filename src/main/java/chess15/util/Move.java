package chess15.util;

import chess15.Piece;
import chess15.Vector2;

public class Move {
    public Vector2 from;
    public Vector2 to;
    public Piece.Color color;

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
