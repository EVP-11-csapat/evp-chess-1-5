package chess15.algorithm;

import chess15.Board;
import chess15.Vector2;
import chess15.util.Move;

public interface AlgorithmInterface {
    Move move(Board positions, Move playerMove);

}
