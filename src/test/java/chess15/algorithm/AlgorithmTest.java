package chess15.algorithm;

import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import chess15.util.Move;
import org.junit.Before;
import org.junit.Test;

public class AlgorithmTest {

    private Engine engine;
    private ChessAlgorithm algo;
    @Before
    public void setUp() {
        RuleSet rules = RuleSet.getInstance();
        rules.gamemode = new Classical();
        engine = new Engine(rules, null);
        algo = new ChessAlgorithm(rules, Piece.Color.BLACK);
    }

    @Test
    public void testSearch(){
        long startTime = System.nanoTime();
        algo.move(engine.board, new Move(new Vector2(0,0), new Vector2(0,0)));
        long endTime = System.nanoTime();
        System.out.println(endTime-startTime);
    }
}
