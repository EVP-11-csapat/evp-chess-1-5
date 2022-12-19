package chess15.gui.interfaces;
import chess15.Vector2;
import chess15.Piece;
import chess15.util.WinReason;

public interface UIInteface {
    void endGame(Piece.Color won, WinReason reason);
    void setCheck(Vector2 checkCoord);
    void remove(Vector2 pieceToRemove, Piece taken); // Temp
    void promote(Vector2 pos);
    void addPiece(Piece piece, Vector2 pos);
}
