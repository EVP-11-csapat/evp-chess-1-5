package chess15.gamemode;

import chess15.*;
import chess15.util.BoardVisualizer;
import chess15.util.JsonToBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Chess960 extends Gamemode {
    @Override
    public Board startState() {
        Board board = new Board();

        board = JsonToBoard.jsonToBoard("chess960.json");

        ArrayList<Piece> pieces = new ArrayList<>(List.of(
                new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
                new Piece(Piece.Color.WHITE, Piece.Type.KING, King.getInstance(), true),
                new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false)
        ));

        ArrayList<Vector2> remainingPositions = new ArrayList<>(List.of(
                new Vector2(0, 7),
                new Vector2(1, 7),
                new Vector2(2, 7),
                new Vector2(3, 7),
                new Vector2(4, 7),
                new Vector2(5, 7),
                new Vector2(6, 7),
                new Vector2(7, 7)
        ));

        boolean firstBishop = true;
        Random random = new Random(System.currentTimeMillis());
        for (Piece p : pieces) {
            if (p.movement.getClass() == Bishop.class) {
                if (firstBishop) {
                    Predicate<Vector2> isDarkX = n -> n.x % 2 == 0;
                    ArrayList<Vector2> darkMoves = (ArrayList<Vector2>) remainingPositions.stream()
                            .filter(isDarkX)
                            .collect(Collectors.toList());
                    int index = random.nextInt(darkMoves.size());
                    Vector2 pos = darkMoves.get(index);
                    remainingPositions.remove(pos);
                    addPiece(board, p, pos);
                    firstBishop = false;
                    continue;
                } else {
                    Predicate<Vector2> isListX = n -> n.x % 2 != 0;
                    ArrayList<Vector2> lightMoves = (ArrayList<Vector2>) remainingPositions.stream()
                            .filter(isListX)
                            .collect(Collectors.toList());
                    int index = random.nextInt(lightMoves.size());
                    Vector2 pos = lightMoves.get(index);
                    remainingPositions.remove(pos);
                    addPiece(board, p, pos);
                    continue;
                }
            } else if (p.movement.getClass() == Rook.class || p.isKing) {
                Vector2 pos = remainingPositions.remove(0);
                addPiece(board, p, pos);
                continue;
            }

            int index = random.nextInt(remainingPositions.size());
            Vector2 pos = remainingPositions.remove(index);
            addPiece(board, p, pos);
        }

        return board;
    }

    private static void addPiece(Board board, Piece p, Vector2 pos) {
        board.elements[pos.x][pos.y] = new Piece(p);
        p.color = Piece.Color.BLACK;
        board.elements[pos.x][7 - pos.y] = new Piece(p);
    }

    @Override
    public String toString() {
        return "chess960";
    }
}
