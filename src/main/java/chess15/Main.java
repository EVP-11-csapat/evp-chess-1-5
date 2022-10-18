package chess15;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Chess");

        MoveSet kingMoves = King.getInstance();

        System.out.println(kingMoves.moves.size());

        //tesztelések
        RisztererIstvan.run();
    }
}
