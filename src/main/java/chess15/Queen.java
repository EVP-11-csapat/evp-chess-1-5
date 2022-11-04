package chess15;

import java.util.ArrayList;

//Singleton class
public class Queen extends MoveSet
{
    private static Queen instance;

    private Queen()
    {
        ArrayList<Vector2> m = new ArrayList<Vector2>();
        m.add(new Vector2(-1, -1));
        m.add(new Vector2(0, -1));
        m.add(new Vector2(1, -1));
        m.add(new Vector2(-1, 0));
        m.add(new Vector2(1, 0));
        m.add(new Vector2(-1, 1));
        m.add(new Vector2(0, 1));
        m.add(new Vector2(1, 1));

        moves = m;
        repeating = true;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Queen();
        return instance;
    }
}