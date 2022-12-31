package chess15.gui.interfaces;
import chess15.Vector2;
import chess15.Piece;
import chess15.util.WinReason;

/**
 * An interface for the UI, so the engine can call methods
 */
public interface UIInteface {
    /**
     * @param won The {@link chess15.Piece.Color} of the winning side, <b>null</b> for draw
     * @param reason The {@link WinReason} of the game
     */
    void endGame(Piece.Color won, WinReason reason);

    /**
     * @deprecated Not currently used
     * @param checkCoord The {@link Vector2} position of the king in check
     */
    void setCheck(Vector2 checkCoord);

    /**
     * @param pieceToRemove The {@link Vector2} position of the piece to remove
     * @param taken The {@link Piece} reference to add it to the taken list <b>null</b> if not added
     */
    void remove(Vector2 pieceToRemove, Piece taken);

    /**
     * @param color The {@link chess15.Piece.Color} of the side currently promoting
     * @param to The {@link Vector2} position of the pawn / where to put the new piece
     */
    void promote(Piece.Color color, Vector2 to);

    /**
     * @param piece The {@link Piece} to add to the board
     * @param pos The {@link Vector2} position of where to add it
     */
    void addPiece(Piece piece, Vector2 pos);
}
