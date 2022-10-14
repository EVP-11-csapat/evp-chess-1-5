package chess15;

public class Board {
    private BoardElement[][] elements = new BoardElement[8][8];

    public BoardElement getElement(int row, int col){
        // column is a number too, (1,1) translates to a1, while keeping the implementation in the correct orientation
        return elements[col - 1][8 - row];
    }
}