package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (special != null && filterDirection(special, p.pin)) moves.add(special);

        ArrayList<Vector2> moveDirections = filterDirections(p.movement.moves, p.pin);

        for (int i = 0; i < moveDirections.size(); i++) {

            Vector2 direction = orient(moveDirections.get(i), p.movement.whiteDifferent);

            if (p.movement.repeating) {

                moves.addAll(repeatMove(position, direction, !p.movement.attackDifferent));
            } else {
                Vector2 candidate = Vector2.add(position, direction);
                if (evalMove(candidate, !p.movement.attackDifferent, false)) moves.add(candidate);
            }
        }

        if (p.movement.attackDifferent) {
            ArrayList<Vector2> attackDirections = filterDirections(p.movement.attacks, p.pin);
            for (Vector2 attackDirection : attackDirections) {
                Vector2 candidate = Vector2.add(position, orient(attackDirection, p.movement.whiteDifferent));
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 position = new Vector2(i, j);
                BoardElement target = board.at(position);
                if (!target.isEmpty) {
                    if (((Piece) target).color == nextPlayer) pieces.add(position);
                }
            }
        }
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

    private ArrayList<Vector2> repeatMove(Vector2 pos, Vector2 direction, boolean allowAttack) {
        ArrayList<Vector2> moves = new ArrayList<>();

        Piece target = null;

        Vector2 candidate = Vector2.add(pos, direction);
        for (int i = 0; i < 9; i++) {
            if (candidate.outOfBounds()) return moves;

            boolean empty = board.at(candidate).isEmpty;
            if (!allowAttack && !empty) return moves;

            if (empty) moves.add(candidate);
            else {
                target = (Piece) board.at(candidate);
                if (target.color == nextPlayer) return moves;
                if (target.isKing) return moves; //impossible situation
                moves.add(candidate);
                break;
            }
            candidate = Vector2.add(candidate, direction);
        }

        candidate = Vector2.add(candidate, direction);

        for (int i = 0; i < 9; i++) {

            if (candidate.outOfBounds()) return moves;
            if (!board.at(candidate).isEmpty) {
                Piece p = ((Piece) board.at(candidate));

                if (p.isKing && p.color != nextPlayer) {
                    assert target != null;
                    target.pin = direction;
                }
                return moves;
            }

            candidate = Vector2.add(candidate, direction);
        }

        return moves;
    }

    private Vector2 orient(Vector2 direction, boolean whiteDifferent) {
        if (nextPlayer == Piece.Color.WHITE && whiteDifferent) return direction.flip();
        return direction;
    }

    private boolean filterDirection(Vector2 direction, Vector2 pinDirection) {
        if (pinDirection == null) return true;

        return Vector2.sameDirection(direction, pinDirection);
    }

    private ArrayList<Vector2> filterDirections(ArrayList<Vector2> directions, Vector2 pinDirection) {
        if (pinDirection == null) return directions;

        ArrayList<Vector2> filteredDirections = new ArrayList<>();
        for (Vector2 direction : directions) {
            if (filterDirection(direction,pinDirection)) filteredDirections.add(direction);
        }

        return filteredDirections;
    }
}
