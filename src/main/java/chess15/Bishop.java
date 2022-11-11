package chess15;

import java.util.ArrayList;

//Singleton class
public class Bishop extends MoveSet
{
    private static Bishop instance;

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

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Bishop();
        return instance;
    }
}
