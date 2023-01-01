package chess15.gamemode;

import chess15.board.Board;
import chess15.util.JsonToBoard;

/**
 * Pawn Attack Game Mode
 */
public class PawnAttack extends Gamemode {
    /**
     * Prepares the board for the gamemode
     * @return The {@link Board} created for the mode
     */
    public Board startState(){
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("pawnattack.json");

        return board;
    }

    @Override
    public String toString() {
        return "pawnattack";
    }
}
