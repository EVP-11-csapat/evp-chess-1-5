package chess15.algorithm;

import chess15.Piece;
import chess15.engine.Engine;
import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
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
    public void testTree(){
        long startTime = System.nanoTime();
        algo.move(engine.board);
        long endTime = System.nanoTime();
        System.out.println(endTime-startTime);
    }
}
