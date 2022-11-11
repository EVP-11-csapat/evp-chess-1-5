package chess15;

import java.util.ArrayList;

//Singleton class
public class King extends MoveSet
{
    private static King instance;

    private King()
    {
        moves = new ArrayList<Vector2>();
        moves.add(new Vector2(-1, -1));
        moves.add(new Vector2(0, -1));
        moves.add(new Vector2(1, -1));
        moves.add(new Vector2(-1, 0));
        moves.add(new Vector2(1, 0));
        moves.add(new Vector2(-1, 1));
        moves.add(new Vector2(0, 1));
        moves.add(new Vector2(1, 1));

        repeating = false;
        attackDifferent = false;
        whiteDifferent = false;
    }
    public static MoveSet getInstance()
    {
        if(instance == null) instance = new King();
        return instance;
    }
}
