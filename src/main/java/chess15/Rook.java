package chess15;

import java.util.ArrayList;

//Singleton class
public class Rook extends MoveSet
{
    private static Rook instance;

    private Rook()
    {
        ArrayList<Vector2> m = new ArrayList<Vector2>();
        m.add(new Vector2(0, -1));
        m.add(new Vector2(1, 0));
        m.add(new Vector2(0, 1));
        m.add(new Vector2(-1, 0));

        moves = m;
        repeating = true;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Rook();
        return instance;
    }
}