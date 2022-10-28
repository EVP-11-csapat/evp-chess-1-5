package chess15.gamemode;

import chess15.Board;
import chess15.King;
import chess15.Piece;

public class Classical extends Gamemode {
    public Board startState(){
        Board board = new Board();

        for (int i = 0; i < 7; i++) {
            board.elements[1][i] = new Piece(Piece.Color.BLACK, Piece.Type.PAWN, King.getInstance(),false);
            board.elements[6][i] = new Piece(Piece.Color.WHITE, Piece.Type.PAWN, King.getInstance(),false);
        }


        return board;
    }
}
