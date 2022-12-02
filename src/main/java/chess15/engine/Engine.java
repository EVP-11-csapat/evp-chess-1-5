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

    private HashMap<Vector2, ArrayList<Vector2>> possibleMoves;
    private Piece.Color nextPlayer;
    private ArrayList<Vector2> pieces;


    public ArrayList<Vector2> getMoves(Vector2 position) {
        return possibleMoves.getOrDefault(position, new ArrayList<>());
    }

    public void move(Vector2 from, Vector2 to) {
        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();
        if (nextPlayer == Piece.Color.WHITE) nextPlayer = Piece.Color.BLACK;
        else nextPlayer = Piece.Color.WHITE;
        calculateMoveMap();
    }

    public void reset() {
        board = gamemode.startState();
        possibleMoves = new HashMap<>();
        calculateMoveMap();
    }

    public Engine(Gamemode gamemode, RuleSet rules, UIInteface uiRef) {
        this.gamemode = gamemode;
        board = gamemode.startState();
        this.rules = rules;
        UIRef = uiRef;
        nextPlayer = Piece.Color.WHITE;
        possibleMoves = new HashMap<>();
        pieces = new ArrayList<>();
        calculateMoveMap();
    }


    private ArrayList<Vector2> calculateMoves(Vector2 position) {
        BoardElement e = board.elements[position.x][position.y];
        ArrayList<Vector2> moves = new ArrayList<>();
        if (e.isEmpty) return moves;
        Piece p = (Piece) e;

        Vector2 special = p.movement.special.apply(position, nextPlayer);
        if(special != null) moves.add(special);

        for (int i = 0; i < p.movement.moves.size(); i++) {
            Vector2 direction = orient(p.movement.moves.get(i), p.movement.whiteDifferent);

            if (p.movement.repeating) {
                for (int j = 1; j < 9; j++) {
                    Vector2 candidate = Vector2.add(position, direction.scaleBy(j));
                    if (candidate.outOfBounds()) break;
                    if (evalMove(candidate, !p.movement.attackDifferent, false)) moves.add(candidate);
                }
            } else {
                Vector2 candidate = Vector2.add(position, direction);
                if (evalMove(candidate, !p.movement.attackDifferent, false)) moves.add(candidate);
            }
        }

        if (p.movement.attackDifferent) {
            for (int i = 0; i < p.movement.attacks.size(); i++) {
                Vector2 candidate = Vector2.add(position, orient(p.movement.attacks.get(i), p.movement.whiteDifferent));
                if (evalMove(candidate, true, true)) moves.add(candidate);
            }
        }

        return moves;
    }

    private void calculateMoveMap() {
        selectPieces();
        possibleMoves = new HashMap<>();

        for (int i = 0; i < pieces.size(); i++) {
            Vector2 pos = pieces.get(i);
            possibleMoves.put(pos, calculateMoves(pos));
        }
    }

    private void selectPieces() {
        pieces = new ArrayList<>();
        Vector2 king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 position = new Vector2(i, j);
                BoardElement target = board.at(position);
                if (!target.isEmpty) {
                    if (((Piece) target).color == nextPlayer) {
                        if (((Piece) target).isKing) king = position;
                        else pieces.add(position);
                    }
                }
            }
        }
        pieces.add(king);
    }

    private boolean evalMove(Vector2 pos, boolean isAttack, boolean onlyAttack) {
        if (pos.outOfBounds()) return false;
        if (!board.at(pos).isEmpty) {
            if (!isAttack) return false;
            Piece target = (Piece) board.at(pos);
            return target.color != nextPlayer;
        }
        return !onlyAttack;
    }

    private Vector2 orient(Vector2 direction, boolean whiteDifferent){
        if(nextPlayer == Piece.Color.WHITE && whiteDifferent) return direction.inverse();
        return direction;
    }
}
