package chess15.board;

/**
 * This class represents a single element of the chess board.
 * It is used to store the state of the board.
 */
public class BoardElement {
    public boolean isEmpty;

    /**
     * Initializes a new instance of the BoardElement class with the empty flag set true.
     */
    public BoardElement(){
        isEmpty = true;
    }

    /**
     * A method to print the board element to the console. Used for debugging.
     * @return A string representation of the board element.
     */
    @Override
    public String toString() {
        return "BoardElement{" +
                "isEmpty=" + isEmpty +
                '}';
    }
}
