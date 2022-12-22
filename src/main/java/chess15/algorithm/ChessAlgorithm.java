package chess15.algorithm;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.engine.RuleSet;

import java.util.ArrayList;

public class ChessAlgorithm implements AlgorithmInterface {

    private final RuleSet rules;
    private final Piece.Color color;

    @Override
    public Vector2[] move(Board positions) {


        Engine engine = new Engine(rules, positions, color);

        ArrayList<MoveNode> moveTree = generateLayers(engine, color, 4);

        Vector2[] bestMove = engine.getRandomMove();

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
    }

    private ArrayList<MoveNode> generateLayers(Engine engine, Piece.Color player, int depth){
        ArrayList<MoveNode> moveTree = new ArrayList<>();

        if(depth == 0) return moveTree;

        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                MoveNode node = new MoveNode();
                node.player = player;
                node.move = new Vector2[]{start,end};

                Engine copiedEngine = new Engine(engine);
                copiedEngine.move(start, end);
                node.board = copiedEngine.getBoard();

                node.nextMoves = generateLayers(copiedEngine, Engine.switchColor(player), depth - 1);

            }
        }
        return moveTree;
    }

}
