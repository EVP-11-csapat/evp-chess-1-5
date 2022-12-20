package chess15.engine;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;

import java.util.ArrayList;

public interface EngineInterface {
    ArrayList<Vector2> getMoves(Vector2 vector2);
    void move(Vector2 from, Vector2 to);
    void reset();
    Board getBoard();
    void setPiece(Vector2 position, Piece piece);
    Vector2[] getRandomMove();
}