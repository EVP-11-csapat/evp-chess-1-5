package chess15;

import chess15.engine.RuleSet;
import kotlin.jvm.functions.Function3;

import java.util.ArrayList;

/**
 * Abstract class represents the set of moves that a piece can make.
 * Used to extend the MoveSet class to create a specific set of moves.
 */
public abstract class MoveSet{
    public ArrayList<Vector2> moves;
    public ArrayList<Vector2> attacks;

    public Function3<Vector2, Board, RuleSet,ArrayList<Vector2>> special = (a, b, c) -> (null);


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