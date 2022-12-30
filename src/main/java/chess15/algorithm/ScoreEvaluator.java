package chess15.algorithm;

import chess15.Board;
import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import chess15.engine.Engine;
import chess15.util.PiecePoints;

import java.util.ArrayList;

/**
 * ScoreEvaluator is a class that evaluates the score of a board.
 */
public class ScoreEvaluator {

    private static final float endgameMaterialStart = 1600;

    /**
     * Evaluates the score of a board.
     *
     * @param engine The engine to evaluate.
     * @return The score of the board from the perspective of the player that would make the next move.
     */
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

        float whiteEndgame = endgame(whiteMaterialWoPawns);
        float blackEndgame = endgame(blackMaterialWoPawns);

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
        whiteScore += (int) (PiecePoints.evaluateTable(whiteKing, whiteKingPos) * (1 - whiteEndgame));

        Vector2 blackKingPos = whitePieces.get(whitePieces.size() - 1);
        Piece blackKing = (Piece) board.at(whiteKingPos);
        blackScore += (int) (PiecePoints.evaluateTable(blackKing, blackKingPos) * (1 - blackEndgame));

        whiteScore += mopUp(whiteKingPos, blackKingPos, whiteMaterial, blackMaterial, blackEndgame);
        blackScore += mopUp(blackKingPos, whiteKingPos, blackMaterial, whiteMaterial, whiteEndgame);


        return (engine.nextPlayer == Piece.Color.WHITE) ? whiteScore - blackScore : blackScore - whiteScore;
    }

    /**
     * Calculates the endgame factor.
     * @param materialCountWithoutPawns The material count without pawns.
     * @return The endgame factor.
     */
    private static float endgame(int materialCountWithoutPawns) {
        float multiplier = 1 / endgameMaterialStart;
        return 1 - Math.min(1, materialCountWithoutPawns * multiplier);
    }

    /**
     * Calculates the mop up factor.
     * @param playerKing The position of the player's king.
     * @param opponentKing The position of the opponent's king.
     * @param playerMaterial The material count of the player.
     * @param opponentMaterial The material count of the opponent.
     * @param endgame The endgame factor.
     * @return The mop up factor.
     */
    private static int mopUp(Vector2 playerKing, Vector2 opponentKing, int playerMaterial, int opponentMaterial, float endgame) {
        int mopUpScore = 0;
        if (playerMaterial > opponentMaterial + 200 && endgame > 0) {

            mopUpScore += centerManhattanDistance[opponentKing.y * 8 + opponentKing.x] * 10;

            mopUpScore += (14 - manhattanDistance(playerKing, opponentKing)) * 4;

            return (int) (mopUpScore * endgame);
        }
        return 0;
    }

    /**
     * Calculates the manhattan distance between two positions.
     * @param a The first {@link Vector2} position.
     * @param b The second {@link Vector2} position.
     * @return The manhattan distance.
     */
    private static byte manhattanDistance(Vector2 a, Vector2 b) {
        return (byte) (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }

    private static final byte[] centerManhattanDistance = {
            6, 5, 4, 3, 3, 4, 5, 6,
            5, 4, 3, 2, 2, 3, 4, 5,
            4, 3, 2, 1, 1, 2, 3, 4,
            3, 2, 1, 0, 0, 1, 2, 3,
            3, 2, 1, 0, 0, 1, 2, 3,
            4, 3, 2, 1, 1, 2, 3, 4,
            5, 4, 3, 2, 2, 3, 4, 5,
            6, 5, 4, 3, 3, 4, 5, 6
    };

}
