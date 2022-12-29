package chess15;

import java.util.ArrayList;

/**
 * Queen movement class
 * Singleton class
 */
public class Queen extends MoveSet
{
    private static Queen instance;

    /**
     * Add the posible moves for the class
     */
    private Queen()
    {
        moves= new ArrayList<Vector2>();
        moves.add(new Vector2(-1, -1));
        moves.add(new Vector2(0, -1));
        moves.add(new Vector2(1, -1));
        moves.add(new Vector2(-1, 0));
        moves.add(new Vector2(1, 0));
        moves.add(new Vector2(-1, 1));
        moves.add(new Vector2(0, 1));
        moves.add(new Vector2(1, 1));
        
        repeating = true;
        attackDifferent = false;
        whiteDifferent = false;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Queen();
        return instance;
    }

    @Override
    public int hashCode() {
        return 5;
    }
}
