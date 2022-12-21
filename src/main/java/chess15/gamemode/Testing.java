package chess15.gamemode;

import chess15.Board;
import chess15.util.JsonToBoard;

public class Testing extends Gamemode{
    @Override
    public Board startState() {
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("testing.json");

        return board;
    }

    @Override
    public String toString() {
        return "testing";
    }
}
