package chess15.util;

import chess15.board.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonToBoardTest {

    @Test
    public void getPieceType() {
        assertEquals(Piece.Type.PAWN, JsonToBoard.getPieceType("pawn"));
        assertEquals(Piece.Type.ROOK, JsonToBoard.getPieceType("rook"));
        assertEquals(Piece.Type.KNIGHT, JsonToBoard.getPieceType("knight"));
        assertEquals(Piece.Type.BISHOP, JsonToBoard.getPieceType("bishop"));
        assertEquals(Piece.Type.QUEEN, JsonToBoard.getPieceType("queen"));
        assertEquals(Piece.Type.KING, JsonToBoard.getPieceType("king"));
    }

    @Test
    public void getMoveSet() {
        assertEquals(Pawn.getInstance(), JsonToBoard.getMoveSet("pawn"));
        assertEquals(Rook.getInstance(), JsonToBoard.getMoveSet("rook"));
        assertEquals(Knight.getInstance(), JsonToBoard.getMoveSet("knight"));
        assertEquals(Bishop.getInstance(), JsonToBoard.getMoveSet("bishop"));
        assertEquals(Queen.getInstance(), JsonToBoard.getMoveSet("queen"));
        assertEquals(King.getInstance(), JsonToBoard.getMoveSet("king"));
    }
}