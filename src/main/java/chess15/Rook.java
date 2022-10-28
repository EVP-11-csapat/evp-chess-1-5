package chess15;

import java.util.ArrayList;

//Singleton class
public class Rook extends MoveSet
{
    private static Rook instance;

    private Rook()
    {
        ArrayList<Coords> m = new ArrayList<Coords>();
        m.add(new Coords(0, -1));
        m.add(new Coords(1, 0));
        m.add(new Coords(0, 1));
        m.add(new Coords(-1, 0));

        moves = m;
        repeating = true;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Rook();
        return instance;
    }
//    private Rook(ArrayList<Coords> moves, boolean repeating)
//    {
//        this.moves = moves;
//        this.repeating = repeating;
//    }
//    private static Rook RookSingleton= null;
//    public  static Rook getRookSingleton()
//    {
//        if(RookSingleton == null)
//        {
//            ArrayList<Coords> m = new ArrayList<Coords>();
//            m.add(new Coords(1,0));
//            m.add(new Coords(0,1));
//            RookSingleton = new Rook( m,true);
//        }
//
//        return RookSingleton;
//    }
//    @Override public Rook getSingleton()
//    {
//        if(RookSingleton == null)
//        {
//            ArrayList<Coords> m = new ArrayList<Coords>();
//            m.add(new Coords(1,0));
//            m.add(new Coords(0,1));
//            RookSingleton = new Rook( m,true);
//        }
//
//        return RookSingleton;
//    }
}