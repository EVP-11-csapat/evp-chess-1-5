package chess15;

import java.util.ArrayList;

//Singleton class
public class Queen extends MoveSet
{
    private static Queen instance;

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

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Queen();
        return instance;
    }
}
