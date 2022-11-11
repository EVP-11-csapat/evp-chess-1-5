package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine implements EngineInterface {

    private final Board board;
    private final RuleSet rules;

    private HashMap<Vector2, ArrayList<Vector2>> moveMap;


    public ArrayList<Vector2> getMoves(Vector2 position){
        return moveMap.get(position);
    }

    private ArrayList<Vector2> calculateMoves(Vector2 position) {
        Piece p = (Piece) board.elements[position.x][position.y];
        ArrayList<Vector2> possibleMoves = new ArrayList<>();

        for (int i = 0; i < p.movement.moves.size(); i++) {
            Vector2 direction = p.movement.moves.get(i);
            if (p.movement.whiteDifferent && p.color == Piece.Color.WHITE) {
                direction = direction.inverse();
            }

            if (p.movement.repeating) {
                for (int j = 1; j < 9; j++) {
                    Vector2 candidate = Vector2.add(position, direction.scaleBy(j));
                    if (candidate.outOfBounds()) break;
                    BoardElement target = board.at(candidate);
                    if (!target.isEmpty) {
                        if (p.color != ((Piece) target).color && !p.movement.attackDifferent) {
                            possibleMoves.add(candidate);
                        } else break;
                    } else possibleMoves.add(candidate);
                }
            } else {
                Vector2 candidate = Vector2.add(position, direction);
                if (!candidate.outOfBounds()) {
                    if (!p.movement.attackDifferent || board.at(candidate).isEmpty) possibleMoves.add(candidate);
                }
            }
        }

        if (p.movement.attackDifferent) {
            for (int i = 0; i < p.movement.attacks.size(); i++) {
                Vector2 direction = p.movement.moves.get(i);
                if (p.movement.whiteDifferent && p.color == Piece.Color.WHITE) {
                    direction = direction.inverse();
                }
                Vector2 candidate = Vector2.add(position, direction);
                if (!board.at(candidate).isEmpty) possibleMoves.add(candidate);
            }
        }

        return possibleMoves;
    }

    private void calculateMoveMap(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 postition = new Vector2(i,j);
                if(!board.at(postition).isEmpty) moveMap.put(postition, calculateMoves(postition));
            }
        }
    }

    public void move(Vector2 from, Vector2 to) {
        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();
        calculateMoveMap();
    }

    public Engine(Gamemode gamemode, RuleSet rules) {
        board = gamemode.startState();
        this.rules = rules;
        moveMap = new HashMap<>();
        calculateMoveMap();
    }
}
