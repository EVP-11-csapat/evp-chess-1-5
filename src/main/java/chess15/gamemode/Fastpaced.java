package chess15.gamemode;

import chess15.board.Board;
import chess15.util.JsonToBoard;

/**
 * Fast Paced Game Mode
 */
public class Fastpaced extends Gamemode {
    /**
     * Prepares the board for the gamemode
     * @return The {@link Board} created for the mode
     */
    public Board startState(){
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("classical.json");

        return board;
    }

    @Override
    public String toString() {
        return "fast-paced";
    }
}
