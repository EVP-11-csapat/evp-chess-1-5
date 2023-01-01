package chess15.board;

/**
 * This class represents a piece in a chess game.
 */
public class Piece extends BoardElement{
    /**
     * The {@link Color} of the piece
     */
    public Color color;
    /**
     * The Look of the piece, determines what image to use
     */
    public Type look;
    /**
     * The Movement of the piece, determines how the piece moves
     */
    public MoveSet movement;
    /**
     * True if the piece is king
     */
    public boolean isKing;

    /**
     * use this both for castling and en passant
     */
    public boolean boolProperty = false;

    /**
     * Constructor for a piece.
     * @param color The color of the piece.
     * @param look The type of the piece.
     * @param movement The movement of the piece.
     * @param isKing Whether the piece is a king.
     */
    public Piece(Color color, Type look, MoveSet movement, boolean isKing){
        isEmpty = false;
        this.color = color;
        this.look = look;
        this.movement = movement;
        this.isKing = isKing;
    }

    /**
     * Constructor for piece
     * @param original The piece to copy from
     */
    public Piece(Piece original){
        this.isEmpty = false;
        this.color = original.color;
        this.movement = original.movement;
        this.look = original.look;
        this.isKing = original.isKing;
        this.pin = original.pin;
        this.boolProperty = original.boolProperty;
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
     * Used for pin calcualtion
     */
    public Vector2 pin = null;

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