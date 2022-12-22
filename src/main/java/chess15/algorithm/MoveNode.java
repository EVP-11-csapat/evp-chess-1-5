package chess15.algorithm;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;

import java.util.ArrayList;

public class MoveNode {
    Vector2[] move;
    int score;
    Piece.Color player;

    ArrayList<MoveNode> nextMoves;
}
