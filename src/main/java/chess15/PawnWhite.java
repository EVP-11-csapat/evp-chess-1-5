package chess15;

import java.util.ArrayList;

//Singleton class
public class PawnWhite extends MoveSet
{
    ArrayList<Coords> attack;
    private PawnWhite(ArrayList<Coords> moves, ArrayList<Coords> attack, boolean repeating) 
    {
        this.moves = moves;
        this.repeating = repeating;
        this.attack = attack;
    }
    private static PawnWhite PawnWhiteSingleton= null;
    public  static PawnWhite getPawnWhiteSingleton()
    {
        if(PawnWhiteSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(1,0));
            m.add(new Coords(2,0));
            ArrayList<Coords> a = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(-1,1));
            PawnWhiteSingleton = new PawnWhite( m,a,true);
        }

        return PawnWhiteSingleton;
    }
    @Override public PawnWhite getSingleton()
    {
        if(PawnWhiteSingleton == null)
        {
            ArrayList<Coords> m = new ArrayList<Coords>();
            m.add(new Coords(0,1));
            m.add(new Coords(0,2));
            ArrayList<Coords> a = new ArrayList<Coords>();
            m.add(new Coords(1,1));
            m.add(new Coords(-1,1));
            PawnWhiteSingleton = new PawnWhite( m,a,true);
        }

        return PawnWhiteSingleton;
    }
    @Override 
    public ArrayList<Coords> possibleMoves(final Coords positionOfPiece)
    {
        ArrayList<Coords> result = new ArrayList<Coords>();
        if(positionOfPiece.getY()==1)
        { 
            Coords currentTile;
            for(int i=0; i<getSingleton().moves.size();i++)
            {
                final Coords currentMove = getSingleton().moves.get(i);

                currentTile = new Coords(0,0).add(positionOfPiece).add(currentMove);
                if(currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0)
                {
                    result.add(new Coords(currentTile));
                }
                currentTile = new Coords(0,0).add(positionOfPiece).subtract(currentMove);
                if(currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0)
                {
                    result.add(new Coords(currentTile));
                }
    
            }
        }else{
            Coords currentTile;
            final Coords currentMove = getSingleton().moves.get(0);
            currentTile = new Coords(0,0).add(positionOfPiece).add(currentMove);
            if(currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0)
            {
                result.add(new Coords(currentTile));
            }
            currentTile = new Coords(0,0).add(positionOfPiece).subtract(currentMove);
            if(currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0)
            {
                result.add(new Coords(currentTile));
            }
        }
        return result;
    }
    public ArrayList<Coords> possibleAttack(final Coords positionOfPiece)
    {
        ArrayList<Coords> result = new ArrayList<Coords>();
        Coords currentTile;
        for(int i=0; i<getSingleton().attack.size();i++)
        {
            final Coords currentAttack = getSingleton().attack.get(i);
            currentTile = new Coords(0,0).add(positionOfPiece).add(currentAttack);
            if(currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0)
            {
                result.add(new Coords(currentTile));
            }
        }
        return result;
    }
}