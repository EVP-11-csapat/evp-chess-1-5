package chess15;

import java.util.ArrayList;

/**
 * Bishop movement class
 * Singleton class
 */
public class Bishop extends MoveSet
{
    private static Bishop instance;

    /**
     * Add the posible moves for the class
     */
    private Bishop()
    {
        moves= new ArrayList<Vector2>();
        moves.add(new Vector2(-1, -1));
        moves.add(new Vector2(1, -1));
        moves.add(new Vector2(1, 1));
        moves.add(new Vector2(-1, 1));
        
        repeating = true;
        attackDifferent = false;
        whiteDifferent = false;
    }

    /**
     * @return The instance of the class
     */
    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Bishop();
        return instance;
    }

    @Override
    public int hashCode() {
        return 2;
    }
}
