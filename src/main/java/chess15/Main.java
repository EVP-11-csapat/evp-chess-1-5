package chess15;

import chess15.gamemode.Classical;
import chess15.gui.ChessApplication;
import chess15.util.BoardVisualizer;
import chess15.util.JsonToBoard;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Chess");

//        MoveSet kingMoves = King.getInstance();

//        System.out.println(kingMoves.moves.size());
//        tesztelések
//        RisztererIstvan.run();
//
//        Board board = new Board();
//
//
//        board = new Classical().startState();
//
//        System.out.println(board.toString());
//        System.out.println(board.at(new Vector2(0,6)));
//        BoardVisualizer.printBoard(board);
//
        ChessApplication.open();

    }
}
