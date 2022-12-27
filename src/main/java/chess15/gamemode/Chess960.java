package chess15.gamemode;

import chess15.*;
import chess15.util.BoardVisualizer;
import chess15.util.JsonToBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Chess960 extends Gamemode {
    @Override
    public Board startState() {
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("chess960.json");

        ArrayList<Piece> remainingPieces = new ArrayList<>(List.of(
                new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KING, King.getInstance(), true),
                new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false)
        ));

        ArrayList<Vector2> remainingWitePositins = new ArrayList<>(List.of(
                new Vector2(0, 7),
                new Vector2(1, 7),
                new Vector2(2, 7),
                new Vector2(3, 7),
                new Vector2(4, 7),
                new Vector2(5, 7),
                new Vector2(6, 7),
                new Vector2(7, 7)
        ));

        ArrayList<Vector2> remainingBlackPositions = new ArrayList<>(List.of(
                new Vector2(0, 0),
                new Vector2(1, 0),
                new Vector2(2, 0),
                new Vector2(3, 0),
                new Vector2(4, 0),
                new Vector2(5, 0),
                new Vector2(6, 0),
                new Vector2(7, 0)
        ));

        // Randomize the starting row for the white pieces
        Vector2 firstRook = null;
        Vector2 secondRook = null;
        Random random = new Random(System.currentTimeMillis());
        for (Piece p : remainingPieces) {
            if (p.movement.getClass() == Rook.class) {
                int index = random.nextInt(remainingWitePositins.size());
                Vector2 pos;
                if (firstRook == null) {
                    pos = remainingWitePositins.remove(index);
                    firstRook = pos;
                } else {
                    pos = remainingWitePositins.get(index);
                    while (!(pos.x < (firstRook.x - 1)) && !(pos.x > (firstRook.x + 1))) {
                        index = random.nextInt(remainingWitePositins.size());
                        pos = remainingWitePositins.get(index);
                    }
                    remainingBlackPositions.remove(pos);
                    secondRook = pos;
                }
                board.elements[pos.x][pos.y] = p;
            } else if (p.movement.getClass() == King.class) {
                int smallerX = -1;
                int largerX = -1;
                if (firstRook.x > secondRook.x) {
                    smallerX = secondRook.x;
                    largerX = firstRook.x;
                } else {
                    smallerX = firstRook.x;
                    largerX = secondRook.x;
                }
                int index = random.nextInt(remainingWitePositins.size());
                Vector2 pos = remainingWitePositins.get(index);
                while (pos.x < smallerX || pos.x > largerX) {
                    index = random.nextInt(remainingWitePositins.size());
                    pos = remainingWitePositins.get(index);
                }
                remainingBlackPositions.remove(index);
                board.elements[pos.x][pos.y] = p;
            }
        }

        Piece blackKing = remainingPieces.get(2);
        blackKing.color = Piece.Color.BLACK;
        board.elements[4][4] = blackKing;
        Piece blackRook1 = remainingPieces.get(0);
        blackRook1.color = Piece.Color.BLACK;
        board.elements[4][5] = blackRook1;
        Piece blackRook2 = remainingPieces.get(1);
        blackRook2.color = Piece.Color.BLACK;
        board.elements[4][3] = blackRook2;

//        board = JsonToBoard.jsonToBoard("classical.json");

        BoardVisualizer.printBoard(board);
        return board;
    }

    @Override
    public String toString() {
        return "chess960";
    }
}
