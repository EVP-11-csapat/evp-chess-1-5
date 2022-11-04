package chess15.engine.interfaces;

import chess15.Vector2;

import java.util.ArrayList;

public interface EngineInterface {
    ArrayList<Vector2> getMoves(Vector2 vector2);
    void move(Vector2 from, Vector2 to);
}