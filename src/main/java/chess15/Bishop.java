package chess15;

import java.util.ArrayList;

//Singleton class
public class Bishop extends MoveSet
{
    private static Bishop instance;

    private Bishop()
    {
        ArrayList<Vector2> m = new ArrayList<Vector2>();
        m.add(new Vector2(-1, -1));
        m.add(new Vector2(1, -1));
        m.add(new Vector2(1, 1));
        m.add(new Vector2(-1, 1));

        moves = m;
        repeating = true;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Bishop();
        return instance;
    }
}