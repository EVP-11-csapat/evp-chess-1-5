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
        
    }

}