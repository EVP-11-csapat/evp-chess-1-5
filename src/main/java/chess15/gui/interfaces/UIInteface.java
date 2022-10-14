package chess15.gui.interfaces;
import chess15.Coords;
import chess15.Piece;

public interface UIInteface {
    void endGame(Piece.Color won);
    void setCheck(Coords checkCoord);
    void remove(Piece pieceToRemove); // Temp
}
