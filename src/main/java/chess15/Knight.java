package chess15;

import java.util.ArrayList;

//Singleton class
public class Knight extends MoveSet
{
    private static Knight instance;

    private Knight()
    {
        ArrayList<Vector2> m = new ArrayList<Vector2>();
        m.add(new Vector2(-1, -2));
        m.add(new Vector2(1, -2));
        m.add(new Vector2(-2, -1));
        m.add(new Vector2(2, -1));
        m.add(new Vector2(-2, 1));
        m.add(new Vector2(2, 1));
        m.add(new Vector2(-1, 2));
        m.add(new Vector2(1, 2));

        moves = m;
        repeating = false;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Knight();
        return instance;
    }
}