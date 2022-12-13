package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Engine implements EngineInterface {

    public Board board;
    private final RuleSet rules;
    private final Gamemode gamemode;
    private final UIInteface UIRef;

    private HashMap<Vector2, ArrayList<Vector2>> possibleMoves;
    private HashMap<Vector2, ArrayList<Vector2>> previousAttacks;
    private Piece.Color nextPlayer;
    private ArrayList<Vector2> pieces;


    public ArrayList<Vector2> getMoves(Vector2 position) {
        return possibleMoves.getOrDefault(position, new ArrayList<>());
    }

    public void move(Vector2 from, Vector2 to) {
        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();

        previousAttacks = new HashMap<>();
        previousAttacks = calculateMoveMap(true);
        if (nextPlayer == Piece.Color.WHITE) nextPlayer = Piece.Color.BLACK;
        else nextPlayer = Piece.Color.WHITE;
        possibleMoves = calculateMoveMap(false);
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public void reset() {
        board = gamemode.startState();
        nextPlayer = Piece.Color.WHITE;
        possibleMoves = new HashMap<>();
        previousAttacks = new HashMap<>();
        pieces = new ArrayList<>();
        possibleMoves = calculateMoveMap(false);
    }

    public Engine(RuleSet rules, UIInteface uiRef) {
        this.gamemode = rules.gamemode;
        this.rules = rules;
        UIRef = uiRef;
        reset();
    }

    private HashMap<Vector2, ArrayList<Vector2>> calculateMoveMap(boolean onlyAttacks) {
        selectPieces();
        HashMap<Vector2, ArrayList<Vector2>> movemap = new HashMap<>();

        int last = pieces.size() - 1;
        Vector2 kingPos = pieces.get(last);

        Vector2 checkGivenby = null;
        boolean checkAvoidable = true; //by capturing or blocking the piece that's giving check, (if there are several it's inavoidable)

        ArrayList<Vector2> kingMoves = calculateMoves(kingPos, onlyAttacks);

        for(Map.Entry<Vector2, ArrayList<Vector2>> entry : previousAttacks.entrySet()){
            if(entry.getValue().contains(kingPos)){
                // check
                if(checkGivenby == null) checkGivenby = entry.getKey();
                else checkAvoidable = false;
            }
            for (Vector2 pos : entry.getValue()) {
                kingMoves.remove(pos);
            }
        }
        movemap.put(kingPos, kingMoves);

        if(checkAvoidable){
            for (int i = 0; i < last; i++) {
                Vector2 pos = pieces.get(i);
                ArrayList<Vector2> rawMoves = calculateMoves(pos, onlyAttacks);
                ArrayList<Vector2> moves = new ArrayList<>();
                if(checkGivenby != null){
                    for (int j = 0; j < rawMoves.size(); j++) {
                        Vector2 checkVector = Vector2.add(kingPos, checkGivenby.invert()).normalize();

                        for (Vector2 attack : previousAttacks.get(checkGivenby)) {
                            if(attack.normalize().equals(checkVector)){
                                moves.add(rawMoves.get(j));
                            }
                        }
                    }
                    if(rawMoves.contains(checkGivenby)) moves.add(checkGivenby);
                    movemap.put(pos,moves);
                }else movemap.put(pos, rawMoves);
            }
        }

        boolean checkmate = true;
        for (Map.Entry<Vector2, ArrayList<Vector2>> entry: movemap.entrySet()){
            if(!entry.getValue().isEmpty()) {
                checkmate = false;
                break;
            }
        }

        return movemap;
    }

    private ArrayList<Vector2> calculateMoves(Vector2 position, boolean onlyAttacks) {
        BoardElement e = board.elements[position.x][position.y];
        ArrayList<Vector2> moves = new ArrayList<>();
        if (e.isEmpty) return moves;
        Piece p = (Piece) e;
        boolean isAttackDifferent = p.movement.attackDifferent;

        Vector2 special = p.movement.special.apply(position, board);
        if (special != null && filterDirection(special, p.pin)) moves.add(special);

        ArrayList<Vector2> moveDirections = filterDirections(p.movement.moves, p.pin);

        if(!isAttackDifferent || !onlyAttacks){
            for (int i = 0; i < moveDirections.size(); i++) {

                Vector2 direction = orient(moveDirections.get(i), p.movement.whiteDifferent);

                if (p.movement.repeating) {

                    moves.addAll(repeatMove(position, direction, !p.movement.attackDifferent));
                } else {
                    Vector2 candidate = Vector2.add(position, direction);
                    if (evalMove(candidate, !p.movement.attackDifferent, false, onlyAttacks)) moves.add(candidate);
                }
            }
        }

        if (isAttackDifferent) {
            ArrayList<Vector2> attackDirections = filterDirections(p.movement.attacks, p.pin);
            for (Vector2 attackDirection : attackDirections) {
                Vector2 candidate = Vector2.add(position, orient(attackDirection, p.movement.whiteDifferent));
                if (evalMove(candidate, true, true, onlyAttacks)) moves.add(candidate);
            }
        }

        return moves;
    }

    private void selectPieces() {
        pieces = new ArrayList<>();
        Vector2 king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 position = new Vector2(i, j);
                BoardElement target = board.at(position);
                if (!target.isEmpty) {
                    Piece piece = ((Piece) target);
                    if (piece.color == nextPlayer) {
                        if (piece.isKing) king = position;
                        else pieces.add(position);
                    } else piece.pin = null;

                }
            }
        }
        pieces.add(king);
    }

    private boolean evalMove(Vector2 pos, boolean isAttack, boolean onlyAttack, boolean forceAttack) {
        if (pos.outOfBounds()) return false;
        if (!board.at(pos).isEmpty) {
            if (!isAttack) return false;
            Piece target = (Piece) board.at(pos);
            return target.color != nextPlayer;
        }
        return !onlyAttack || forceAttack;
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
            if (filterDirection(direction, pinDirection)) filteredDirections.add(direction);
        }

        return filteredDirections;
    }
}
