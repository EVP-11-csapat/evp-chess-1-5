package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine implements EngineInterface {

    private final Board board;
    private final RuleSet rules;
    private final UIInteface UIRef;

    private HashMap<Vector2, ArrayList<Vector2>> whiteMoves;
    private HashMap<Vector2, ArrayList<Vector2>> blackMoves;


    public ArrayList<Vector2> getMoves(Vector2 position){
        if(((Piece)board.at(position)).color == Piece.Color.WHITE)
        return whiteMoves.get(position);
        else return blackMoves.get(position);
    }

    private ArrayList<Vector2> calculateMoves(Vector2 position) {
        Piece p = (Piece) board.elements[position.x][position.y];
        ArrayList<Vector2> possibleMoves = new ArrayList<>();
//        System.out.println(p);

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
                Vector2 direction = p.movement.attacks.get(i);
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
        Vector2 blackKing;
        Vector2 whiteKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 postition = new Vector2(i,j);
                BoardElement target = board.at(postition);
                if(!target.isEmpty){

                    if(((Piece) target).color == Piece.Color.WHITE){
                        whiteMoves.put(postition, calculateMoves(postition));
                        if(((Piece) target).isKing) whiteKing = postition;
                    }else {
                        blackMoves.put(postition, calculateMoves(postition));
                        if(((Piece) target).isKing) blackKing = postition;
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

    public Engine(Gamemode gamemode, RuleSet rules, UIInteface uiRef) {
        board = gamemode.startState();
        this.rules = rules;
        UIRef = uiRef;
        whiteMoves = new HashMap<>();
        blackMoves = new HashMap<>();
        calculateMoveMap();
    }
}
