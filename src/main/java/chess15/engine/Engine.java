package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine implements EngineInterface {

    public Board board;
    private final RuleSet rules;
    private final Gamemode gamemode;
    private final UIInteface UIRef;

    private HashMap<Vector2, ArrayList<Vector2>> whiteMoves;
    private HashMap<Vector2, ArrayList<Vector2>> blackMoves;


    public ArrayList<Vector2> getMoves(Vector2 position){
        if(((Piece)board.at(position)).color == Piece.Color.WHITE)
        return whiteMoves.get(position);
        else return blackMoves.get(position);
    }

    private ArrayList<Vector2> calculateMoves(Vector2 position) {
        BoardElement e = board.elements[position.x][position.y];
        ArrayList<Vector2> possibleMoves = new ArrayList<>();
        if(e.isEmpty) return possibleMoves;
        Piece p = (Piece)e;

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
                        if (p.color != ((Piece) target).color) {
                            if(!p.movement.attackDifferent) {
                                possibleMoves.add(candidate);
                            }
                        } else break;
                    } else possibleMoves.add(candidate);
                }
            } else {
                Vector2 candidate = Vector2.add(position, direction);
                if (candidate.outOfBounds()) continue;
                BoardElement target = board.at(candidate);
                if (!target.isEmpty) {
                    if (p.color != ((Piece) target).color && !p.movement.attackDifferent) possibleMoves.add(candidate);
                } possibleMoves.add(candidate);
            }
        }

        if (p.movement.attackDifferent) {
            for (int i = 0; i < p.movement.attacks.size(); i++) {
                Vector2 candidate = Vector2.add(position, p.movement.attacks.get(i));
                if (candidate.outOfBounds()) continue;
                BoardElement target = board.at(candidate);
                if (!target.isEmpty) {
                    if (p.color != ((Piece) target).color) possibleMoves.add(candidate);
                }
            }
        }

        return possibleMoves;
    }

    private void calculateMoveMap(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 postition = new Vector2(i,j);
                if(postition != board.blackKing && postition != board.whiteKing){ //skip kings, calculate them last
                    BoardElement target = board.at(postition);
                    if(!target.isEmpty){
                        if(((Piece) target).color == Piece.Color.WHITE) whiteMoves.put(postition, calculateMoves(postition));
                        else blackMoves.put(postition, calculateMoves(postition));
                    }
                }
            }
        }
    }

    public void move(Vector2 from, Vector2 to) {
        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();
        calculateMoveMap();
    }

    public void reset(){
        board = gamemode.startState();
        whiteMoves = new HashMap<>();
        blackMoves = new HashMap<>();
        calculateMoveMap();
    }

    public Engine(Gamemode gamemode, RuleSet rules, UIInteface uiRef) {
        this.gamemode = gamemode;
        board = gamemode.startState();
        this.rules = rules;
        UIRef = uiRef;
        whiteMoves = new HashMap<>();
        blackMoves = new HashMap<>();
        calculateMoveMap();
    }
}
