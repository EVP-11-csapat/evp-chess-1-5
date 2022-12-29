package chess15;

import java.util.ArrayList;

/**
 * Rook movement class
 * Singleton class
 */
public class Rook extends MoveSet
{
    private static Rook instance;

    /**
     * Add the posible moves for the class
     */
    private Rook()
    {
        moves = new ArrayList<Vector2>();
        moves.add(new Vector2(0, -1));
        moves.add(new Vector2(1, 0));
        moves.add(new Vector2(0, 1));
        moves.add(new Vector2(-1, 0));

        repeating = true;
        attackDifferent = false;
        whiteDifferent = false;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Rook();
        return instance;
    }
}
