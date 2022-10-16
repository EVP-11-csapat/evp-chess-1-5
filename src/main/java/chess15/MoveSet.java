package chess15;

import java.util.ArrayList;

public abstract class MoveSet{
    public static ArrayList<Coords> moves;
    public static boolean repeating;
    public abstract MoveSet getSingleton();
    public ArrayList<Coords> possibleMoves(final Coords positionOfPiece)
    {
        ArrayList<Coords> result=new ArrayList<Coords>();
        if(repeating){
            for(int i=0; i<getSingleton().moves.size();i++)
            {
                final Coords currentMove = getSingleton().moves.get(i);
                for(Coords currentTile = new Coords(0,0).add(positionOfPiece).add(currentMove);
                    (currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0);
                    currentTile.add(currentMove))
                {
                    result.add(new Coords(currentTile));
                }
                for(Coords currentTile = new Coords(0,0).add(positionOfPiece).subtract(currentMove);
                    (currentTile.getX()<=7 && currentTile.getX()>=0 && currentTile.getY()<=7 && currentTile.getY()>=0);
                    currentTile.subtract(currentMove))
                {
                    result.add(new Coords(currentTile));
                }
            }
        }else{
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
        }
        return result;
    }
}