package chess15.gamemode;

import chess15.Board;
import chess15.King;
import chess15.Piece;

public class Classical extends Gamemode {
    public Board startState(){
        Board board = new Board();

        for (int i = 0; i < 7; i++) {
            board.elements[i][1] = new Piece(Piece.Color.BLACK, Piece.Type.PAWN, Pawn.getInstance(),false);
            board.elements[i][6] = new Piece(Piece.Color.WHITE, Piece.Type.PAWN, Pawn.getInstance(),false);
        }


        return board;
    }
}
