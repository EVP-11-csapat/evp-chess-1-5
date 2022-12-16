package chess15;

import java.util.ArrayList;
import java.util.function.Function;

public class Pawn extends MoveSet {
    private static Pawn instance;

    private Pawn() {
        moves = new ArrayList<>();
        moves.add(new Vector2(0, 1));

        attacks = new ArrayList<>();
        attacks.add(new Vector2(1, 1));
        attacks.add(new Vector2(-1, 1));

        repeating = false;
        attackDifferent = true;
        whiteDifferent = true;


        special = (pos, board) -> {
            ArrayList<Vector2> specialMoves = new ArrayList<Vector2>();
            Piece piece = (Piece) board.at(pos);
            Vector2 left = Vector2.add(pos, new Vector2(-1, 0));
            Vector2 right = Vector2.add(pos, new Vector2(1, 0));

            if ((pos.y == 1 && (piece).color == Piece.Color.BLACK && board.at(Vector2.add(pos, new Vector2(0, 1))).isEmpty && board.at(Vector2.add(pos, new Vector2(0, 2))).isEmpty))
                specialMoves.add(new Vector2(0, 2));
            else if (pos.y == 6 && (piece).color == Piece.Color.WHITE && board.at(Vector2.add(pos, new Vector2(0, -1))).isEmpty && board.at(Vector2.add(pos, new Vector2(0, -2))).isEmpty)
                specialMoves.add(new Vector2(0, -2));

            if (!left.outOfBounds() && !board.at(left).isEmpty) {
                Piece leftPiece = (Piece) board.at(left);
                if(leftPiece.movement.getClass() == Pawn.class && leftPiece.color != piece.color && leftPiece.boolProperty){
                    Vector2 enpassant = new Vector2(-1, 1);
                    if(piece.color == Piece.Color.WHITE) enpassant = enpassant.flip();
                    specialMoves.add(enpassant);
                }
            }
            if (!right.outOfBounds() && !board.at(right).isEmpty) {
                Piece rightPiece = (Piece) board.at(right);
                if(rightPiece.movement.getClass() == Pawn.class && rightPiece.color != piece.color && rightPiece.boolProperty){
                    Vector2 enpassant = new Vector2(1, 1);
                    if(piece.color == Piece.Color.WHITE) enpassant = enpassant.flip();
                    specialMoves.add(enpassant);
                }
            }


            return specialMoves;
        };
    }

    public static MoveSet getInstance() {
        if (instance == null) instance = new Pawn();
        return instance;
    }

}