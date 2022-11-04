package chess15;

import java.util.ArrayList;

public class Pawn extends MoveSet
{
    private static Pawn instance;

    private Pawn(){
        moves = new ArrayList<>();
        moves.add(new Vector2(0,-1));



    }

    public static MoveSet getInstance(){
        if(instance == null) instance = new Pawn();
        return instance;
    }

}