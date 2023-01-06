package chess15.gamemode;

import chess15.board.*;
import chess15.gui.newui.Variables;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Chaos Game Mode
 */
public class ChaosMode extends Gamemode {

    /**
     * Gets a random {@link Piece.Type} to create a random piece
     * @return The {@link Piece.Type} of the piece
     */
    private Piece.Type getRandomType() {
        ArrayList<Piece.Type> possible = new ArrayList<>(List.of(
                Piece.Type.ROOK,
                Piece.Type.BISHOP,
                Piece.Type.PAWN,
                Piece.Type.KNIGHT,
                Piece.Type.QUEEN
        ));
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(possible.size());
        if (Variables.DEVMODE)
            System.out.println("Choosing random: " + index);
        return possible.get(index);
    }

    /**
     * Gets a random {@link MoveSet} to create a random piece
     * @return The {@link MoveSet} of the piece
     */
    private MoveSet getMovement() {
        ArrayList<MoveSet> possible = new ArrayList<>(List.of(
                Rook.getInstance(),
                Bishop.getInstance(),
                Pawn.getInstance(),
                Knight.getInstance(),
                Queen.getInstance()
        ));
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(possible.size());
        if (Variables.DEVMODE)
            System.out.println("Choosing random: " + index);
        return possible.get(index);
    }

    /**
     * Prepares the board for the gamemode
     * @return The {@link Board} created for the mode
     */
    @Override
    public Board startState() {
        Board board = new Board();

        // Gemerate king positions
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(7);

        // Place the two kings
        Piece whiteKing = new Piece(Piece.Color.WHITE, Piece.Type.KING, King.getInstance(), true);
        Piece blackKing = new Piece(Piece.Color.BLACK, Piece.Type.KING, King.getInstance(), true);
        board.elements[x][0] = blackKing;
        board.elements[x][7] = whiteKing;

        // Generate random pieces
        for (int j = 0; j < 2; j++){
            for (int i = 0; i < 8; i++) {
                Vector2 position = new Vector2(i, j);
                if (board.at(position) instanceof Piece) continue;
                Piece p = new Piece(Piece.Color.BLACK, getRandomType(), getMovement(), false);
                if (Variables.DEVMODE)
                    System.out.println(p);
                board.elements[position.x][position.y] = new Piece(p);
                p.color = Piece.Color.WHITE;
                board.elements[position.x][7 - position.y] = new Piece(p);
            }
        }

        return board;
    }
}
