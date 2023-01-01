package chess15.algorithm;

import chess15.board.Board;
import chess15.board.Move;

public interface AlgorithmInterface {
    Move move(Board positions, Move playerMove);

}
