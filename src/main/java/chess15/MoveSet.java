package chess15;

import java.util.ArrayList;

public abstract class MoveSet{
    public ArrayList<Vector2> moves;
    public ArrayList<Vector2> attacks;

    public boolean attackDifferent;
    public boolean whiteDifferent;
    public boolean repeating;
}