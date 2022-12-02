package chess15;

import java.util.ArrayList;
import java.util.function.Function;

public class Pawn extends MoveSet
{
    private static Pawn instance;

    private Pawn(){
        moves = new ArrayList<>();
        moves.add(new Vector2(0,1));

        attacks = new ArrayList<>();
        attacks.add(new Vector2(1,1));
        attacks.add(new Vector2(-1,1));

        repeating = false;
        attackDifferent = true;
        whiteDifferent = true;



        special = (pos, color) -> (
                (pos.y == 1 && color == Piece.Color.BLACK) ? new Vector2(pos.x, 3) : (pos.y == 6 && color == Piece.Color.WHITE) ? new Vector2(pos.x, 4) : null
        );


    }

    public static MoveSet getInstance(){
        if(instance == null) instance = new Pawn();
        return instance;
    }

}