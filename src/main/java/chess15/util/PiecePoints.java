package chess15.util;

import chess15.Piece;

public class PiecePoints {
    public static int evaluate(Piece p) {
        return switch (p.look) {
            case PAWN -> 1;
            case ROOK -> 5;
            case KNIGHT, BISHOP -> 3;
            case QUEEN -> 9;
            default -> 0;
        };
    }
}
