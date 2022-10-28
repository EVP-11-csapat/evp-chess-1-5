package chess15;

public class BoardElement {
    public boolean isEmpty;

    public BoardElement(){
        isEmpty = true;
    }

    @Override
    public String toString() {
        return "BoardElement{" +
                "isEmpty=" + isEmpty +
                '}';
    }
}
