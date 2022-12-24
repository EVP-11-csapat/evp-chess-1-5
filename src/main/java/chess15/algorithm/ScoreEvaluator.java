package chess15.algorithm;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.util.PiecePoints;

public class ScoreEvaluator {

    public static int evaluate(Engine engine) {
        Board board = engine.board;

        int whiteMaterial = 0;
        int blackMaterial = 0;

        for (Vector2 piece : engine.getPieces()) {
            blackMaterial += PiecePoints.evaluate(((Piece) board.at(piece))) * 100;
        }
        for (Vector2 piece : engine.selectPieces(Engine.switchColor(engine.nextPlayer))) {
            whiteMaterial += PiecePoints.evaluate(((Piece) board.at(piece))) * 100;
        }

        int score = (engine.nextPlayer == Piece.Color.WHITE) ? whiteMaterial - blackMaterial : blackMaterial - whiteMaterial;

        return score;


    }
}
