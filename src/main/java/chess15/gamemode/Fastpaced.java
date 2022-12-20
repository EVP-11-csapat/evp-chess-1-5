package chess15.gamemode;

import chess15.Board;
import chess15.util.JsonToBoard;

public class Fastpaced extends Gamemode {
    public Board startState(){
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("classical.json");

        return board;
    }

    @Override
    public String toString() {
        return "classical";
    }
}
