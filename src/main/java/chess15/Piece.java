package chess15;

/**
 * This class represents a piece in a chess game.
 */
public class Piece extends BoardElement{
    public Color color;
    public Type look;
    public MoveSet movement;
    public boolean isKing;

    /**
     * Constructor for a piece.
     * @param color The color of the piece.
     * @param look The type of the piece.
     * @param movement The movement of the piece.
     * @param isKing Whether or not the piece is a king.
     */
    public Piece(Color color, Type look, MoveSet movement, boolean isKing){
        isEmpty = false;
        this.color = color;
        this.look = look;
        this.movement = movement;
        this.isKing = isKing;
    }

    /**
     * Enum that represents the look of the piece
     */
    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }


    /**
     * Enum that represents the color of the piece
     */
    public enum Color {
        WHITE, BLACK
    }

    /**
     * A method to print the piece to the console. Used for debugging.
     * @return A string representation of the piece.
     */
    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", look=" + look +
                ", movement=" + movement +
                ", isKing=" + isKing +
                '}';
    }
}