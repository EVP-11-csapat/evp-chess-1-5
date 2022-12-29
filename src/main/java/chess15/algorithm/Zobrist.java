package chess15.algorithm;

import chess15.*;
import chess15.engine.Engine;

import java.util.ArrayList;
import java.util.Random;

public class Zobrist {
    static long[][][] zobristArray;

    static {
        zobristArray = new long[7][13][64];
        Random rng = new Random();

        // Initialize the zobristArray with random long values
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {
                for (int k = 0; k < 64; k++) {
                    zobristArray[i][j][k] = rng.nextLong();
                }
            }
        }
    }

    public static long calculateHash(Engine engine) {
        long hash = 0;

        ArrayList<Vector2> pieces = engine.getPieces();
        pieces.addAll(engine.getOpponentPieces());

        for (Vector2 pos : pieces) {
            Piece piece = (Piece) engine.board.at(pos);
            int whiteToMove = (engine.nextPlayer == Piece.Color.WHITE) ? 0 : 1;
            int pieceHash = piece.movement.hashCode();
            if (piece.color == Piece.Color.BLACK) pieceHash = -pieceHash;
            hash ^= zobristArray[whiteToMove][pieceHash + 6][pos.y * 8 + pos.x];
        }

        // Add the hash values for the castling rights


        Vector2 kingPos = new Vector2(3, 7);
        Vector2 leftRookPos = new Vector2(0, 7);
        Vector2 rightRookPos = new Vector2(7, 7);

        if (!engine.board.at(kingPos).isEmpty && !engine.board.at(leftRookPos).isEmpty && !engine.board.at(rightRookPos).isEmpty){
            Piece king = (Piece) engine.board.at(kingPos);
            Piece leftRook = (Piece) engine.board.at(leftRookPos);
            Piece rightRook = (Piece) engine.board.at(rightRookPos);
            if(king.isKing && leftRook.movement instanceof Rook && rightRook.movement instanceof Rook){
                if(!king.boolProperty){
                    if(!leftRook.boolProperty) hash ^= zobristArray[3][0][0];
                    if(!rightRook.boolProperty) hash ^= zobristArray[2][0][0];
                }
            }
        }

        kingPos.y = 0;
        leftRookPos.y = 0;
        rightRookPos.y = 0;

        if (!engine.board.at(kingPos).isEmpty && !engine.board.at(leftRookPos).isEmpty && !engine.board.at(rightRookPos).isEmpty){
            Piece king = (Piece) engine.board.at(kingPos);
            Piece leftRook = (Piece) engine.board.at(leftRookPos);
            Piece rightRook = (Piece) engine.board.at(rightRookPos);
            if(king.isKing && leftRook.movement instanceof Rook && rightRook.movement instanceof Rook){
                if(!king.boolProperty){
                    if(!leftRook.boolProperty) hash ^= zobristArray[5][0][0];
                    if(!rightRook.boolProperty) hash ^= zobristArray[4][0][0];
                }
            }
        }

        Vector2 enpassantPosition = new Vector2(0, 3);
        if(engine.nextPlayer == Piece.Color.BLACK) enpassantPosition.y = 4;

        for (int i = 0; i < 8; i++) {
            if(!engine.board.at(enpassantPosition).isEmpty){
                Piece piece = (Piece) engine.board.at(enpassantPosition);
                if(piece.movement instanceof Pawn && piece.boolProperty) {
                    hash ^= zobristArray[6][0][i];
                    break;
                }
            }

            enpassantPosition.x = i;
        }

        return hash;
    }
}
