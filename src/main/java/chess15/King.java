package chess15;

import java.util.ArrayList;

//Singleton class
public class King extends MoveSet
{
    private static King instance;

    private King()
    {
        ArrayList<Coords> m = new ArrayList<Coords>();
        m.add(new Coords(-1, -1));
        m.add(new Coords(0, -1));
        m.add(new Coords(1, -1));
        m.add(new Coords(-1, 0));
        m.add(new Coords(1, 0));
        m.add(new Coords(-1, 1));
        m.add(new Coords(0, 1));
        m.add(new Coords(1, 1));

        moves = m;
        repeating = false;
    }
    public static MoveSet getInstance()
    {
        if(instance == null) instance = new King();
        return instance;
    }
}