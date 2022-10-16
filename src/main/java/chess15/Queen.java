package chess15;

import java.util.ArrayList;

//Singleton class
public class Queen extends MoveSet
{
    private Queen(ArrayList<Coords> moves, boolean repeating) 
    {
        this.moves = moves;
        this.repeating = repeating;
    }
    private static Queen queenSingleton= null;
    public  static Queen getQueenSingleton()
    {
        if(queenSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(1,-1));
            queenSingleton = new Queen( m,true);
        }

        return queenSingleton;
    }
    @Override public Queen getSingleton()
    {
        if(queenSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(1,-1));
            queenSingleton = new Queen( m,true);
        }

        return queenSingleton;
    }
}