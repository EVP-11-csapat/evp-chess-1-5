package chess15.engine;

import chess15.Vector2;
import chess15.gamemode.Classical;
import chess15.gui.interfaces.UIInteface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EngineTest {

    private Engine engine;
    private UIInteface UIRef;

    @Before
    public void setUp() {
        engine = new Engine(new Classical(), new RuleSet(), UIRef);
    }

    @Test
    public void getMoves() {
        engine.reset();
        assertTrue(engine.getMoves(new Vector2(1, 1)).isEmpty());
        assertFalse(engine.getMoves(new Vector2(3, 6)).isEmpty());
        engine.move(new Vector2(3, 6), new Vector2(3, 5));
        assertTrue(engine.getMoves(new Vector2(1, 0)).contains(new Vector2(2,2)));
        engine.move(new Vector2(1, 0), new Vector2(2, 2));
        assertTrue(engine.getMoves(new Vector2(2, 7)).contains(new Vector2(5,4)));
        assertTrue(engine.getMoves(new Vector2(6, 6)).contains(new Vector2(6,4)));
    }

    @Test
    public void move() {
        engine.reset();
        engine.move(new Vector2(0, 6), new Vector2(0, 5));
        assertTrue(engine.board.at(new Vector2(0, 6)).isEmpty);
        assertFalse(engine.board.at(new Vector2(0, 5)).isEmpty);
    }

    @Test
    public void reset() {
        engine.reset();
        engine.move(new Vector2(1, 1), new Vector2(1, 2));
        engine.reset();
        assertFalse(engine.board.at(new Vector2(1, 1)).isEmpty);
        assertTrue(engine.board.at(new Vector2(1, 2)).isEmpty);
    }
}