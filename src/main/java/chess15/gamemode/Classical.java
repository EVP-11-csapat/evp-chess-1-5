package chess15.gamemode;

import chess15.Board;
import chess15.King;
import chess15.Piece;
import chess15.util.JsonToBoard;

public class Classical extends Gamemode {
    public Board startState(){
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("classical.json");

        System.out.println(board.toString());


        return board;
    }
}
