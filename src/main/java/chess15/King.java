package chess15;

import java.util.ArrayList;

//Singleton class
public class King extends MoveSet
{
    private King(ArrayList<Coords> moves, boolean repeating) 
    {
        this.moves = moves;
        this.repeating = repeating;
    }
    private static King KingSingleton= null;
    public  static King getKingSingleton()
    {
        if(KingSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(-1,1));
            KingSingleton = new King( m,false);
        }

        return KingSingleton;
    }
    @Override public King getSingleton()
    {
        if(KingSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(-1,1));
            KingSingleton = new King( m,false);
        }

        return KingSingleton;
    }
}