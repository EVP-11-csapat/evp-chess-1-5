package chess15.engine;

import chess15.board.Board;
import chess15.board.Piece;
import chess15.board.Vector2;
import chess15.board.Move;

import java.util.ArrayList;

public interface EngineInterface {
    ArrayList<Vector2> getMoves(Vector2 vector2);
    void move(Vector2 from, Vector2 to);
    void reset();
    Board getBoard();
    void setPiece(Vector2 position, Piece piece);
    Move getRandomMove();
}