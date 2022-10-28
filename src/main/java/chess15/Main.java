package chess15;

import chess15.gamemode.Classical;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Chess");

//        MoveSet kingMoves = King.getInstance();

//        System.out.println(kingMoves.moves.size());

        //tesztelések
//        RisztererIstvan.run();

        Board board = new Board();

        System.out.println(board.toString());

        board = new Classical().startState();

        System.out.println(board.toString());
    }
}
