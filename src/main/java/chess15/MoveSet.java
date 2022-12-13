package chess15;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Abstract class represents the set of moves that a piece can make.
 * Used to extend the MoveSet class to create a specific set of moves.
 */
public abstract class MoveSet{
    public ArrayList<Vector2> moves;
    public ArrayList<Vector2> attacks;

    public BiFunction<Vector2, Board, Vector2> special = (a,b) -> (null);

    public boolean attackDifferent;
    public boolean whiteDifferent;
    public boolean repeating;

    @Override
    public String toString() {
        return "MoveSet{" +
                "moves=" + moves +
                ", attacks=" + attacks +
                ", attackDifferent=" + attackDifferent +
                ", whiteDifferent=" + whiteDifferent +
                ", repeating=" + repeating +
                '}';
    }
}