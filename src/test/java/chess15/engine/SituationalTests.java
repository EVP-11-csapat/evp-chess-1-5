package chess15.engine;

import chess15.board.Piece;
import chess15.board.Vector2;
import chess15.gamemode.Classical;
import chess15.gui.interfaces.UIInteface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SituationalTests {

    private Engine engine;
    private UIInteface UIRef;

    @Before
    public void setUp() {
        RuleSet rules = RuleSet.getInstance();
        rules.gamemode = new Classical();
        engine = new Engine(rules, UIRef);
    }

    @Test
    public void pinning() {
        engine.reset();
        engine.move(new Vector2(4,6), new Vector2(4,4));
        engine.move(new Vector2(4,1), new Vector2(4,2));
        engine.move(new Vector2(3,7), new Vector2(4,6));
        engine.move(new Vector2(3,0), new Vector2(6,3));
        engine.move(new Vector2(5,6), new Vector2(5,5));
        engine.move(new Vector2(6,3), new Vector2(7,4));
        engine.move(new Vector2(4,6), new Vector2(5,6));
        engine.move(new Vector2(1,1), new Vector2(1,2));

        assertEquals(new Vector2(2,-2   ), ((Piece)engine.board.at(new Vector2(5,6))).pin);
        assertEquals(2, engine.getMoves(new Vector2(5,6)).size());
        assertTrue(engine.getMoves(new Vector2(5, 6)).contains(new Vector2(6,5)));
        assertFalse(engine.getMoves(new Vector2(5, 6)).contains(new Vector2(5,5)));
    }

    @Test
    public void kingMovesInCheck(){
        engine.reset();
        engine.move(new Vector2(5,6), new Vector2(5,4));
        engine.move(new Vector2(4,1), new Vector2(4,2));
        engine.move(new Vector2(4,6), new Vector2(4,5));
        engine.move(new Vector2(5,0), new Vector2(4,1));
        engine.move(new Vector2(3,7), new Vector2(7,3));
        engine.move(new Vector2(4,1), new Vector2(7,4));
        assertEquals(2, engine.getMoves(new Vector2(4,7)).size());
        assertFalse(engine.getMoves(new Vector2(4,7)).contains(new Vector2(5,6)));
        engine.move(new Vector2(4,7), new Vector2(4,6));
        engine.move(new Vector2(4,2), new Vector2(4,3));
        engine.move(new Vector2(4,6), new Vector2(3,5));
        engine.move(new Vector2(2,1), new Vector2(2,2));
        assertEquals(4, engine.getMoves(new Vector2(3,5)).size());
        assertFalse(engine.getMoves(new Vector2(3,5)).contains(new Vector2(3,4)));
    }

    @Test
    public void movesInCheck(){
        engine.reset();
        engine.move(new Vector2(5,6), new Vector2(5,4));
        engine.move(new Vector2(4,1), new Vector2(4,2));
        engine.move(new Vector2(4,6), new Vector2(4,5));
        engine.move(new Vector2(5,0), new Vector2(4,1));
        engine.move(new Vector2(3,7), new Vector2(7,3));
        engine.move(new Vector2(4,1), new Vector2(7,4));
        assertEquals(0, engine.getMoves(new Vector2(1,7)).size());
        assertEquals(0, engine.getMoves(new Vector2(2,6)).size());
        assertEquals(1, engine.getMoves(new Vector2(7,3)).size());
    }


}