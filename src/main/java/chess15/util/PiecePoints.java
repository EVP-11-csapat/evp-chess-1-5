package chess15.util;

import chess15.*;

public class PiecePoints {
    public static int evaluate(Piece p) {
        if (p.movement.getClass() == Pawn.class) return 1;
        if (p.movement.getClass() == Knight.class) return 3;
        if (p.movement.getClass() == Bishop.class) return 3;
        if (p.movement.getClass() == Rook.class) return 5;
        if (p.movement.getClass() == Queen.class) return 9;
        else return 0;
    }
}
