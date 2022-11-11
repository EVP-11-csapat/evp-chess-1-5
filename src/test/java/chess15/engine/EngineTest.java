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
        System.out.println(engine.getMoves(new Vector2(1, 1)));
        assertFalse(engine.getMoves(new Vector2(1, 1)).isEmpty());
        assertNull(engine.getMoves(new Vector2(0, 0)));
    }
}