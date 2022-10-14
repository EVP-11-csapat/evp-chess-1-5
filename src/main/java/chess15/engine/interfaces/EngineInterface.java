package chess15.engine.interfaces;

import chess15.Coords;

import java.util.ArrayList;

public interface EngineInterface {
    ArrayList<Coords> getMoves(Coords coords);
    void move(Coords from, Coords to);
}