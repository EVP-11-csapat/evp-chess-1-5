package chess15.board;

import java.util.Arrays;

/**
 * The class representing the board of the game.
 */
public class Board {
    public BoardElement[][] elements = new BoardElement[8][8];

    /**
     * Converts the column and row coordinates, to the coordinate system used by our board.
     * @param col The column of the piece.
     * @param row The row of the piece.
     * @return The board element at the given coordinates.
     */
    public BoardElement getElement(int col, int row){
        // column is a number too, (1,1) translates to a1, while keeping the implementation in the correct orientation
        return elements[col - 1][8 - row];
    }

    public BoardElement getElement(Vector2 pos) {
        return elements[pos.x][pos.y];
    }

    /**
     * Returns the board element at the given coordinates defined by a Vector2.
     * @param position The position of the piece we want to get in Vector2.
     * @return The board element at the given coordinates.
     */
    public BoardElement at(Vector2 position){
        return elements[position.x][position.y];
    }

    /**
     * Initializes the board with empty elements.
     */

    public Board()
    {
        for (BoardElement[] row : elements) {
            Arrays.fill(row, new BoardElement());
        }
    }

    /**
     * Initializes a new board based on the passed original
     * @param original The {@link Board} to copy
     */
    public Board(Board original){
        elements = new BoardElement[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardElement toCopy = original.elements[i][j];
                elements[i][j] = (toCopy.isEmpty) ? new BoardElement() : new Piece((Piece)toCopy);
            }
        }
    }

    /**
     * A method to print the board to the console. Used for debugging.
     * @return A string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("Board{elements=\n");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                toReturn.append(elements[i][j].toString()).append(", ");
            }
            toReturn.append("\n");
        }

        toReturn.append("}");
        return toReturn.toString();
    }
}