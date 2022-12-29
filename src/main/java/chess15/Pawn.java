package chess15;

import java.util.ArrayList;


/**
 * Bishop movement class
 * Singleton class
 */
public class Pawn extends MoveSet {
    private static Pawn instance;

    /**
     * Add the posible moves for the class
     */
    private Pawn() {
        moves = new ArrayList<>();
        moves.add(new Vector2(0, 1));

        attacks = new ArrayList<>();
        attacks.add(new Vector2(1, 1));
        attacks.add(new Vector2(-1, 1));

        repeating = false;
        attackDifferent = true;
        whiteDifferent = true;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance() {
        if (instance == null) instance = new Pawn();
        return instance;
    }

}