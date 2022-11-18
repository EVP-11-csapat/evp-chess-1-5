package chess15;

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
}