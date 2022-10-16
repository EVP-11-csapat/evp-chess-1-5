package chess15;

import java.util.ArrayList;

//Singleton class
public class Pawn extends MoveSet
{
    private Pawn(ArrayList<Coords> moves, boolean repeating) 
    {
        this.moves = moves;
        this.repeating = repeating;
    }
    private static Pawn PawnSingleton= null;
    public  static Pawn getPawnSingleton()
    {
        if(PawnSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(1,-1));
            PawnSingleton = new Pawn( m,true);
        }

        return PawnSingleton;
    }
    @Override public Pawn getSingleton()
    {
        if(PawnSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(1,0));
            m.add(new Coords(0,1));
            m.add(new Coords(1,-1));
            PawnSingleton = new Pawn( m,true);
        }

        return PawnSingleton;
    }
    @Override 
    public ArrayList<Coords> possibleMoves(final Coords positionOfPiece)
    {

        return null;
    }
}