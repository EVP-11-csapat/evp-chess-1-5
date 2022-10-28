package chess15;

import java.util.ArrayList;

//Singleton class
public class Knight extends MoveSet
{
    private static Knight instance;

    private Knight()
    {
        ArrayList<Coords> m = new ArrayList<Coords>();
        m.add(new Coords(-1, -2));
        m.add(new Coords(1, -2));
        m.add(new Coords(-2, -1));
        m.add(new Coords(2, -1));
        m.add(new Coords(-2, 1));
        m.add(new Coords(2, 1));
        m.add(new Coords(-1, 2));
        m.add(new Coords(1, 2));

        moves = m;
        repeating = false;
    }

    public static MoveSet getInstance()
    {
        if (instance == null) instance = new Knight();
        return instance;
    }

//    private Knight(ArrayList<Coords> moves, boolean repeating)
//    {
//        this.moves = moves;
//        this.repeating = repeating;
//    }
//    private static Knight KnightSingleton= null;
//    public  static Knight getKnightSingleton()
//    {
//        if(KnightSingleton == null)
//        {
//            ArrayList<Coords> m = new ArrayList<Coords>();
//            m.add(new Coords(2,1));
//            m.add(new Coords(2,-1));
//            m.add(new Coords(1,2));
//            m.add(new Coords(-1,2));
//            KnightSingleton = new Knight( m,false);
//        }
//
//        return KnightSingleton;
//    }
//    @Override public Knight getSingleton()
//    {
//        if(KnightSingleton == null)
//        {
//            ArrayList<Coords> m = new ArrayList<Coords>();
//            m.add(new Coords(2,1));
//            m.add(new Coords(2,-1));
//            m.add(new Coords(1,2));
//            m.add(new Coords(-1,2));
//            KnightSingleton = new Knight( m,false);
//        }
//
//        return KnightSingleton;
//    }
}