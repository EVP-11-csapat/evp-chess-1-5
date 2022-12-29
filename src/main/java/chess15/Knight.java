package chess15;

import java.util.ArrayList;

/**
 * Knight movement class
 * Singleton class
 */
public class Knight extends MoveSet
{
    private static Knight instance;

    /**
     * Add the posible moves for the class
     */
    private Knight()
    {
        moves = new ArrayList<Vector2>();
        moves.add(new Vector2(-1, -2));
        moves.add(new Vector2(1, -2));
        moves.add(new Vector2(-2, -1));
        moves.add(new Vector2(2, -1));
        moves.add(new Vector2(-2, 1));
        moves.add(new Vector2(2, 1));
        moves.add(new Vector2(-1, 2));
        moves.add(new Vector2(1, 2));

        repeating = false;
        attackDifferent = false;
        whiteDifferent = false;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Knight();
        return instance;
    }
}
