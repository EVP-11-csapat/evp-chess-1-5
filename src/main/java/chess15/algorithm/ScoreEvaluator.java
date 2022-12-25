package chess15.algorithm;

import chess15.Board;
import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.util.PiecePoints;

import java.util.ArrayList;

public class ScoreEvaluator {

    private static final float endgameMaterialStart = 1600;

    public static int evaluate(Engine engine) {
        Board board = engine.board;

        ArrayList<Vector2> blackPieces = (engine.nextPlayer == Piece.Color.WHITE) ? engine.getOpponentPieces() : engine.getPieces();
        ArrayList<Vector2> whitePieces = (engine.nextPlayer == Piece.Color.BLACK) ? engine.getOpponentPieces() : engine.getPieces();


        int whiteMaterial = 0;
        int blackMaterial = 0;
        int whiteMaterialWoPawns = 0;
        int blackMaterialWoPawns = 0;

        int whiteScore = 0;
        int blackScore = 0;

        for (Vector2 p : whitePieces) {
            Piece piece = (Piece) board.at(p);
            int points = PiecePoints.evaluate(piece) * 100;

            if (!(piece.movement instanceof Pawn)) {
                whiteMaterialWoPawns += points;
            }

            whiteMaterial += points;
        }

        for (Vector2 p : blackPieces) {
            Piece piece = (Piece) board.at(p);
            int points = PiecePoints.evaluate(piece) * 100;

            if (!(piece.movement instanceof Pawn)) {
                blackMaterialWoPawns += points;
            }

            blackMaterial += points;
        }

        whiteScore += whiteMaterial;
        blackScore += blackMaterial;

        float whiteEndgamePhaseWeight = endgamePhaseWeight(whiteMaterialWoPawns);
        float blackEndgamePhaseWeight = endgamePhaseWeight(blackMaterialWoPawns);

        for (int i = 0; i < whitePieces.size() - 1; i++) {
            Vector2 p = whitePieces.get(i);
            Piece piece = (Piece) board.at(p);
            whiteScore += PiecePoints.evaluateTable(piece, p);
        }

        for (int i = 0; i < blackPieces.size() - 1; i++) {
            Vector2 p = blackPieces.get(i);
            Piece piece = (Piece) board.at(p);
            blackScore += PiecePoints.evaluateTable(piece, p);
        }

        Vector2 whiteKingPos = whitePieces.get(whitePieces.size() - 1);
        Piece whiteKing = (Piece) board.at(whiteKingPos);
        whiteScore += (int) (PiecePoints.evaluateTable(whiteKing, whiteKingPos) * (1 - whiteEndgamePhaseWeight));

        Vector2 blackKingPos = whitePieces.get(whitePieces.size() - 1);
        Piece blackKing = (Piece) board.at(whiteKingPos);
        blackScore += (int) (PiecePoints.evaluateTable(blackKing, blackKingPos) * (1 - blackEndgamePhaseWeight));


        return (engine.nextPlayer == Piece.Color.WHITE) ? whiteScore - blackScore : blackScore - whiteScore;
    }

    private static float endgamePhaseWeight(int materialCountWithoutPawns) {
        float multiplier = 1 / endgameMaterialStart;
        return 1 - Math.min(1, materialCountWithoutPawns * multiplier);
    }

}
