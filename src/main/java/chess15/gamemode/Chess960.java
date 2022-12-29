package chess15.gamemode;

import chess15.*;
import chess15.util.JsonToBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Chess960 Game Mode
 */
public class Chess960 extends Gamemode {
    /**
     * Prepares the board for the gamemode
     * @return The {@link Board} created for the mode
     */
    @Override
    public Board startState() {
        Board board;

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
                    bishopPosition(board, remainingPositions, random, p, isDarkX);
                    firstBishop = false;
                    continue;
                } else {
                    Predicate<Vector2> isListX = n -> n.x % 2 != 0;
                    bishopPosition(board, remainingPositions, random, p, isListX);
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

    /**
     * Calculates the position of the bishops
     * @param board The {@link Board} where we need to add it
     * @param remainingPositions The ramaining positions available to put the bishop
     * @param random Random to generate the position
     * @param p The {@link Piece} bishop to add
     * @param isListX Check for dark and light positions
     */
    private void bishopPosition(Board board, ArrayList<Vector2> remainingPositions, Random random, Piece p, Predicate<Vector2> isListX) {
        ArrayList<Vector2> list = new ArrayList<>();
        for (Vector2 remainingPosition : remainingPositions) {
            if (isListX.test(remainingPosition)) {
                list.add(remainingPosition);
            }
        }
        int index = random.nextInt(list.size());
        Vector2 pos = list.get(index);
        remainingPositions.remove(pos);
        addPiece(board, p, pos);
    }

    /**
     * Add the {@link Piece} to the {@link Board}
     * @param board The {@link Board} we want to add the piece to
     * @param p The {@link Piece} we want to add
     * @param pos The {@link Vector2} position we want to add it at
     */
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
