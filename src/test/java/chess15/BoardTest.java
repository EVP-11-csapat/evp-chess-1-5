package chess15;

import chess15.board.Board;
import chess15.board.Vector2;
import chess15.gamemode.Classical;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    Board board = new Board();
    @Before
    public void setUp() throws Exception {
        board = new Classical().startState();
    }

    @Test
    public void getElement() {
        assertSame(board.getElement(1,1), board.elements[0][7]);
        assertSame(board.getElement(2,1), board.elements[1][7]);
        assertSame(board.getElement(1,8), board.elements[0][0]);
        assertSame(board.getElement(2,8), board.elements[1][0]);
        assertSame(board.getElement(4,4), board.elements[3][5]);
    }

    @Test
    public void at() {
        assertSame(board.at(new Vector2(0,0)), board.elements[0][0]);
        assertSame(board.at(new Vector2(1,0)), board.elements[1][0]);
        assertSame(board.at(new Vector2(0,7)), board.elements[0][7]);
        assertSame(board.at(new Vector2(1,7)), board.elements[1][7]);
        assertSame(board.at(new Vector2(3,3)), board.elements[3][3]);
    }

    @Test
    public void testToString() {
        assertEquals(board.toString(), "Board{elements=\n" +
                "Piece{color=BLACK, look=ROOK, movement=MoveSet{moves=[( 0, -1 ), ( 1, 0 ), ( 0, 1 ), ( -1, 0 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=ROOK, movement=MoveSet{moves=[( 0, -1 ), ( 1, 0 ), ( 0, 1 ), ( -1, 0 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, \n" +
                "Piece{color=BLACK, look=KNIGHT, movement=MoveSet{moves=[( -1, -2 ), ( 1, -2 ), ( -2, -1 ), ( 2, -1 ), ( -2, 1 ), ( 2, 1 ), ( -1, 2 ), ( 1, 2 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=KNIGHT, movement=MoveSet{moves=[( -1, -2 ), ( 1, -2 ), ( -2, -1 ), ( 2, -1 ), ( -2, 1 ), ( 2, 1 ), ( -1, 2 ), ( 1, 2 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=false}, \n" +
                "Piece{color=BLACK, look=BISHOP, movement=MoveSet{moves=[( -1, -1 ), ( 1, -1 ), ( 1, 1 ), ( -1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=BISHOP, movement=MoveSet{moves=[( -1, -1 ), ( 1, -1 ), ( 1, 1 ), ( -1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, \n" +
                "Piece{color=BLACK, look=QUEEN, movement=MoveSet{moves=[( -1, -1 ), ( 0, -1 ), ( 1, -1 ), ( -1, 0 ), ( 1, 0 ), ( -1, 1 ), ( 0, 1 ), ( 1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=QUEEN, movement=MoveSet{moves=[( -1, -1 ), ( 0, -1 ), ( 1, -1 ), ( -1, 0 ), ( 1, 0 ), ( -1, 1 ), ( 0, 1 ), ( 1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, \n" +
                "Piece{color=BLACK, look=KING, movement=MoveSet{moves=[( -1, -1 ), ( 0, -1 ), ( 1, -1 ), ( -1, 0 ), ( 1, 0 ), ( -1, 1 ), ( 0, 1 ), ( 1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=true}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=KING, movement=MoveSet{moves=[( -1, -1 ), ( 0, -1 ), ( 1, -1 ), ( -1, 0 ), ( 1, 0 ), ( -1, 1 ), ( 0, 1 ), ( 1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=true}, \n" +
                "Piece{color=BLACK, look=BISHOP, movement=MoveSet{moves=[( -1, -1 ), ( 1, -1 ), ( 1, 1 ), ( -1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=BISHOP, movement=MoveSet{moves=[( -1, -1 ), ( 1, -1 ), ( 1, 1 ), ( -1, 1 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, \n" +
                "Piece{color=BLACK, look=KNIGHT, movement=MoveSet{moves=[( -1, -2 ), ( 1, -2 ), ( -2, -1 ), ( 2, -1 ), ( -2, 1 ), ( 2, 1 ), ( -1, 2 ), ( 1, 2 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=KNIGHT, movement=MoveSet{moves=[( -1, -2 ), ( 1, -2 ), ( -2, -1 ), ( 2, -1 ), ( -2, 1 ), ( 2, 1 ), ( -1, 2 ), ( 1, 2 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=false}, isKing=false}, \n" +
                "Piece{color=BLACK, look=ROOK, movement=MoveSet{moves=[( 0, -1 ), ( 1, 0 ), ( 0, 1 ), ( -1, 0 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, Piece{color=BLACK, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, BoardElement{isEmpty=true}, Piece{color=WHITE, look=PAWN, movement=MoveSet{moves=[( 0, 1 )], attacks=[( 1, 1 ), ( -1, 1 )], attackDifferent=true, whiteDifferent=true, repeating=false}, isKing=false}, Piece{color=WHITE, look=ROOK, movement=MoveSet{moves=[( 0, -1 ), ( 1, 0 ), ( 0, 1 ), ( -1, 0 )], attacks=null, attackDifferent=false, whiteDifferent=false, repeating=true}, isKing=false}, \n" +
                "}");
    }
}