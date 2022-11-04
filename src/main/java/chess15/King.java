package chess15;

import java.util.ArrayList;

//Singleton class
public class King extends MoveSet
{
    private static King instance;

    private King()
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
        repeating = false;
        attackDifferent = false;
        whiteDifferent = false;
    }
    public static MoveSet getInstance()
    {
        if(instance == null) instance = new King();
        return instance;
    }
}