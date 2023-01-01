package chess15.util;

import chess15.board.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardVisualizerTest {

    Piece pawn;
    Piece knight;
    Piece bishop;
    Piece rook;
    Piece queen;
    Piece king;

    @Before
    public void setUp() throws Exception {
        pawn = new Piece(Piece.Color.WHITE, Piece.Type.PAWN, Pawn.getInstance(), false);
        knight = new Piece(Piece.Color.BLACK, Piece.Type.KNIGHT, Knight.getInstance(), false);
        bishop = new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false);
        rook = new Piece(Piece.Color.BLACK, Piece.Type.ROOK, Rook.getInstance(), false);
        queen = new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false);
        king = new Piece(Piece.Color.BLACK, Piece.Type.KING, King.getInstance(), false);
    }

    @Test
    public void getLookChar() {
        assertEquals("P", BoardVisualizer.getLookChar(pawn));
        assertEquals("N", BoardVisualizer.getLookChar(knight));
        assertEquals("B", BoardVisualizer.getLookChar(bishop));
        assertEquals("R", BoardVisualizer.getLookChar(rook));
        assertEquals("Q", BoardVisualizer.getLookChar(queen));
        assertEquals("K", BoardVisualizer.getLookChar(king));
        assertEquals(" ", BoardVisualizer.getLookChar(null));
    }

    @Test
    public void getColorChar() {
        assertEquals("w", BoardVisualizer.getColorChar(pawn));
        assertEquals("b", BoardVisualizer.getColorChar(knight));
        assertEquals("w", BoardVisualizer.getColorChar(bishop));
        assertEquals("b", BoardVisualizer.getColorChar(rook));
        assertEquals("w", BoardVisualizer.getColorChar(queen));
        assertEquals("b", BoardVisualizer.getColorChar(king));
        assertEquals(" ", BoardVisualizer.getColorChar(null));
    }
}