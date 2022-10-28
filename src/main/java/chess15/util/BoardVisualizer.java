package chess15.util;

import chess15.Board;
import chess15.Piece;

public class BoardVisualizer {
    public static void printBoard(Board board){
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++) {
                try {
                    Piece piece = (Piece) board.getElement(8 - j, i + 1);
                    System.out.print(getColorChar(piece) + getLookChar(piece) + " ");
                } catch (ClassCastException e){
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private static String getLookChar(Piece piece){
        if(piece == null){
            return " ";
        }
        else{
            return switch (piece.look) {
                case PAWN -> "P";
                case KNIGHT -> "N";
                case BISHOP -> "B";
                case ROOK -> "R";
                case QUEEN -> "Q";
                case KING -> "K";
                default -> " ";
            };
        }
    }

    private static String getColorChar(Piece piece){
        if(piece == null){
            return " ";
        }
        else{
            return switch (piece.color) {
                case WHITE -> "w";
                case BLACK -> "b";
                default -> " ";
            };
        }
    }
}
