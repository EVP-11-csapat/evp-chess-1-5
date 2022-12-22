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

    private final int searchDepth = 4;

    @Override
    public Vector2[] move(Board positions) {


        Engine engine = new Engine(rules, positions, color);

        ArrayList<MoveNode> moveTree = generateLayers(engine, color, 0);

        Vector2[] bestMove = moveTree.get(minmax(moveTree, 0)).move;

        return bestMove;
    }

    public ChessAlgorithm(RuleSet rules, Piece.Color player) {
        this.rules = rules;
        this.color = player;
    }

    private ArrayList<MoveNode> generateLayers(Engine engine, Piece.Color player, int depth) {
        ArrayList<MoveNode> moveTree = new ArrayList<>();

        if (depth == searchDepth) return null;

        for (Vector2 start : engine.getPieces()) {
            for (Vector2 end : engine.getMoves(start)) {
                MoveNode node = new MoveNode();
                node.player = player;
                node.move = new Vector2[2];
                node.move[0] = start;
                node.move[1] = end;

                Engine copiedEngine = new Engine(engine);
                copiedEngine.move(start, end);
                node.score = copiedEngine.score();

                node.nextMoves = generateLayers(copiedEngine, Engine.switchColor(player), depth + 1);

                moveTree.add(node);
            }
        }
        return moveTree;
    }

    private int minmax(ArrayList<MoveNode> tree, int depth) {
        int selectedScore = Integer.MAX_VALUE;
        int selectedIndex = 0;

        boolean even = (depth % 2 == 0);

        if (even) selectedScore = 0;

        for (int i = 0; i < tree.size(); i++) {
            MoveNode node = tree.get(i);
            if (depth < searchDepth - 1) node.score = minmax(node.nextMoves, depth + 1);
            int score = node.score;
            if (even) {
                if (score > selectedScore) {
                    selectedScore = score;
                    selectedIndex = i;
                }
            } else {
                if (score < selectedScore) {
                    selectedScore = score;
                    selectedIndex = i;
                }
            }
        }

        if (depth == 0) return selectedIndex;
        return selectedScore;
    }


}
