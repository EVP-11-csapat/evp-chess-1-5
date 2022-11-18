package chess15.engine;

import chess15.Vector2;
import chess15.gamemode.Classical;
import chess15.gui.interfaces.UIInteface;
import com.almasb.fxgl.ui.UI;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import static org.junit.Assert.*;

public class EngineTest {

    private Engine engine;
    private UIInteface UIRef;

    @Before
    public void setUp() throws Exception {
        engine = new Engine(new Classical(), new RuleSet(), UIRef);
    }

    @Test
    public void getMoves() {
        assertFalse(engine.getMoves(new Vector2(1, 1)).isEmpty());
        assertTrue(engine.getMoves(new Vector2(0, 0)).isEmpty());
    }

    @Test
    public void move() {
        engine.move(new Vector2(1, 1), new Vector2(1, 2));
        assertTrue(engine.board.at(new Vector2(1, 1)).isEmpty);
        assertFalse(engine.board.at(new Vector2(1, 2)).isEmpty);
    }
}