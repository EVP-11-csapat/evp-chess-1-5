package chess15;

import java.util.ArrayList;

//Singleton class
public class Bishop extends MoveSet
{
    private Bishop(ArrayList<Coords> moves, boolean repeating) 
    {
        this.moves = moves;
        this.repeating = repeating;
    }
    private static Bishop BishopSingleton= null;
    public  static Bishop getBishopSingleton()
    {
        if(BishopSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(-1,1));
            BishopSingleton = new Bishop( m,true);
        }

        return BishopSingleton;
    }
    @Override public Bishop getSingleton()
    {
        if(BishopSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(-1,1));
            BishopSingleton = new Bishop( m,true);
        }

        return BishopSingleton;
    }
}