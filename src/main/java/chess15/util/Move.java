package chess15.util;

import chess15.Piece;
import chess15.Vector2;

import java.util.Objects;

/**
 * The Move class is used to package the from, to, and color of a move
 */
public class Move {
    public Vector2 from;
    public Vector2 to;
    public Piece.Color color;
    public int score;

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
        this.score = 0;
    }

    public Move(Vector2 from, Vector2 to){
        this.from = from;
        this.to = to;
        this.color = null;
        this.score = 0;
    }

    public Move(Vector2 from, Vector2 to, int score){
        this.from = from;
        this.to = to;
        this.color = null;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Move: {From: " + from + ", To: "  + to + ", Color: " + color + ", Score: " + score + " }";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return from.equals(move.from) && to.equals(move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
