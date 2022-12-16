package chess15.gui.interfaces;
import chess15.Vector2;
import chess15.Piece;

public interface UIInteface {
    void endGame(Piece.Color won);
    void setCheck(Vector2 checkCoord);
    void remove(Vector2 pieceToRemove, Piece taken); // Temp
}
