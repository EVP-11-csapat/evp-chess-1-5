package chess15.board;

import java.util.ArrayList;

/**
 * King movement class
 * Singleton class
 */
public class King extends MoveSet
{
    private static King instance;

    /**
     * Add the posible moves for the class
     */
    private King()
    {
        moves = new ArrayList<Vector2>();
        moves.add(new Vector2(-1, -1));
        moves.add(new Vector2(0, -1));
        moves.add(new Vector2(1, -1));
        moves.add(new Vector2(-1, 0));
        moves.add(new Vector2(1, 0));
        moves.add(new Vector2(-1, 1));
        moves.add(new Vector2(0, 1));
        moves.add(new Vector2(1, 1));

        repeating = false;
        attackDifferent = false;
        whiteDifferent = false;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance()
    {
        if(instance == null) instance = new King();
        return instance;
    }

    @Override
    public int hashCode() {
        return 6;
    }
}
