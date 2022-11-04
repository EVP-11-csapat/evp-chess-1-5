package chess15;

import java.util.Arrays;

public class Board {
    public BoardElement[][] elements = new BoardElement[8][8];

    public BoardElement getElement(int col, int row){
        // column is a number too, (1,1) translates to a1, while keeping the implementation in the correct orientation
        return elements[col - 1][8 - row];
    }

    public BoardElement at(Vector2 position){
        return elements[position.x][position.y];
    }

    public Board()
    {
        for (BoardElement[] row : elements) {
            Arrays.fill(row, new BoardElement());
        }
    }

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