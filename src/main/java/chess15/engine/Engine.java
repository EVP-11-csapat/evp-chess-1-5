package chess15.engine;

import chess15.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;
import chess15.util.WinReason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        pieces.forEach(p -> {
            if (((Piece) board.at(p)).movement.getClass() == Pawn.class) ((Piece) board.at(p)).boolProperty = false;
        });
        Piece piece = (Piece) board.at(from);


        if (piece.movement.getClass() == Pawn.class) {
            if (rules.promotion && ((nextPlayer == Piece.Color.WHITE && to.y == 0) || (nextPlayer == Piece.Color.BLACK && to.y == 7))) {
                UIRef.promote(from, to);
                return;
            }

            if (Math.abs(from.y - to.y) == 2) piece.boolProperty = true;
            if (from.x != to.x && board.at(to).isEmpty) {
                System.out.println("en passant");
                Vector2 passed = new Vector2(to.x, from.y);

                UIRef.remove(passed, (Piece) board.at(passed));
                board.elements[passed.x][passed.y] = new BoardElement();
            }
        }
        if (piece.movement.getClass() == King.class) {
            piece.boolProperty = true;

            if (Math.abs(to.x - from.x) > 1) {
                int kingOffset = (to.x > from.x) ? 2 : -2;
                int rookOriginColumn = (to.x > from.x) ? 7 : 0;
                int rookOffset = kingOffset / 2;

                Vector2 rookOrigin = new Vector2(rookOriginColumn, from.y);

                Vector2 kingpos = new Vector2(from.x + kingOffset, from.y);
                Vector2 rookpos = new Vector2(from.x + rookOffset, from.y);

                board.elements[kingpos.x][kingpos.y] = board.at(from);
                board.elements[rookpos.x][rookpos.y] = board.at(rookOrigin);
                UIRef.addPiece((Piece) board.at(rookOrigin), rookpos);
                board.elements[rookOrigin.x][rookOrigin.y] = new BoardElement();

                UIRef.remove(from, null);
                UIRef.remove(rookOrigin, null);
            }
        }

        if (piece.movement.getClass() == Rook.class) piece.boolProperty = true;


        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();


        previousAttacks = new HashMap<>();
        previousAttacks = calculateMoveMap(true);

        switchPlayer();

        possibleMoves = calculateMoveMap(false);

    }

    public void setPiece(Vector2 position, Piece piece) {
        board.elements[position.x][position.y] = piece;
        previousAttacks = calculateMoveMap(true);
        switchPlayer();
        possibleMoves = calculateMoveMap(false);
    }

    @Override
    public Board getBoard() {
        return board.clone();
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
        if (pieces.size() == 1) {
            selectPieces();
            if (pieces.size() == 1) UIRef.endGame(null, WinReason.NOMATERIAL);
        } else selectPieces();
        HashMap<Vector2, ArrayList<Vector2>> movemap = new HashMap<>();

        int last = pieces.size() - 1;
        Vector2 kingPos = pieces.get(last);

        Vector2 checkGivenby = null;
        boolean checkAvoidable = true; //by capturing or blocking the piece that's giving check, (if there are several it's inavoidable)

        ArrayList<Vector2> kingMoves = calculateMoves(kingPos, onlyAttacks);

        for (Map.Entry<Vector2, ArrayList<Vector2>> entry : previousAttacks.entrySet()) {
            if (entry.getValue().contains(kingPos)) {
                // check
                if (checkGivenby == null) checkGivenby = entry.getKey();
                else checkAvoidable = false;
            }
            for (Vector2 pos : entry.getValue()) {
                kingMoves.remove(pos);
            }
        }
        kingMoves.addAll(castlingMoves());

        movemap.put(kingPos, kingMoves);

        if (checkAvoidable) {
            for (int i = 0; i < last; i++) {
                Vector2 pos = pieces.get(i);
                ArrayList<Vector2> rawMoves = calculateMoves(pos, onlyAttacks);
                ArrayList<Vector2> moves = new ArrayList<>();
                if (checkGivenby != null) {

                    Vector2 checkVector = Vector2.add(kingPos, checkGivenby.invert()).normalize();

                    if (((Piece) board.at(checkGivenby)).movement.repeating) {
                        for (int k = 1; k < 8; k++) {
                            Vector2 offset = checkVector.scaleBy(k);
                            Vector2 candidate = Vector2.add(checkGivenby, offset);
                            if (candidate.equals(kingPos)) break;
                            else if (rawMoves.contains(candidate)) moves.add(candidate);
                        }
                    }
                    if (rawMoves.contains(checkGivenby)) moves.add(checkGivenby);
                    movemap.put(pos, moves);

                } else movemap.put(pos, rawMoves);
            }
        }

        boolean nomoves = true;
        for (Map.Entry<Vector2, ArrayList<Vector2>> entry : movemap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                nomoves = false;
                break;
            }
        }
        Piece.Color winner = (nextPlayer == Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        if (nomoves) {
            if (checkGivenby != null) {
                UIRef.endGame(winner, WinReason.CHECKMATE);
            } else {
                UIRef.endGame(null, WinReason.STALEMATE);
            }
            return null;
        }

        return movemap;
    }

    private ArrayList<Vector2> calculateMoves(Vector2 position, boolean onlyAttacks) {
        BoardElement e = board.elements[position.x][position.y];
        ArrayList<Vector2> moves = new ArrayList<>();
        if (e.isEmpty) return moves;
        Piece p = (Piece) e;
        boolean isAttackDifferent = p.movement.attackDifferent;

        ArrayList<Vector2> special = p.movement.special.invoke(position, board, rules);
        if (special != null) {
            special = filterDirections(special, p.pin);
            special.forEach(dir -> moves.add(Vector2.add(position, dir)));
        }


        ArrayList<Vector2> moveDirections = filterDirections(orient(p.movement.moves, p.movement.whiteDifferent, p.color), p.pin);

        if (!isAttackDifferent || !onlyAttacks) {
            for (Vector2 direction : moveDirections) {
                if (p.movement.repeating) {
                    moves.addAll(repeatMove(position, direction, !p.movement.attackDifferent, onlyAttacks));
                } else {
                    Vector2 candidate = Vector2.add(position, direction);
                    if (evalMove(candidate, !p.movement.attackDifferent, false, onlyAttacks)) moves.add(candidate);
                }
            }
        }

        if (isAttackDifferent) {
            ArrayList<Vector2> attackDirections = filterDirections(orient(p.movement.attacks, p.movement.whiteDifferent, p.color), p.pin);
            for (Vector2 attackDirection : attackDirections) {
                Vector2 candidate = Vector2.add(position, attackDirection);
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


    /*
        isAttack: allow attacks
        onlyAttack: only allow attacks
        forceAttack: allow attacks to empty squares
     */
    private boolean evalMove(Vector2 pos, boolean isAttack, boolean onlyAttack, boolean forceAttack) {
        if (pos.outOfBounds()) return false;
        if (!board.at(pos).isEmpty) {
            if (forceAttack) return true;
            if (!isAttack) return false;
            Piece target = (Piece) board.at(pos);
            return target.color != nextPlayer;
        }
        return !onlyAttack || forceAttack;
    }

    private ArrayList<Vector2> repeatMove(Vector2 pos, Vector2 direction, boolean allowAttack, boolean forceAttack) {
        ArrayList<Vector2> moves = new ArrayList<>();

        Piece target = null;
        Vector2 targetpos = null;

        Vector2 candidate = Vector2.add(pos, direction);

        for (int i = 0; i < 9; i++) {
            if (candidate.outOfBounds()) return moves;

            boolean empty = board.at(candidate).isEmpty;
            if (!empty && forceAttack) {
                Piece potentialKing = (Piece) board.at(candidate);
                if (potentialKing.isKing && potentialKing.color != nextPlayer) empty = true;
            }

            if (!allowAttack && !empty) return moves;

            if (empty) moves.add(candidate);
            else {
                target = (Piece) board.at(candidate);
                targetpos = candidate;
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
                    target.pin = Vector2.add(pos, targetpos.invert());
                }
                return moves;
            }

            candidate = Vector2.add(candidate, direction);
        }

        return moves;
    }

    private ArrayList<Vector2> orient(ArrayList<Vector2> rawDirections, boolean whiteDifferent, Piece.Color color) {
        System.out.println("\n");
        if (whiteDifferent && color == Piece.Color.WHITE) {
            ArrayList<Vector2> directions = new ArrayList<>();
            rawDirections.forEach(direction -> directions.add(direction.flip()));
            return directions;
        } else return rawDirections;

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

    ArrayList<Vector2> castlingMoves() {
        ArrayList<Vector2> castlingMoves = new ArrayList<>();
        int row = 0;
        if (nextPlayer == Piece.Color.WHITE) row = 7;

        Vector2 kingPos = new Vector2(4, row);
        Vector2 leftRookPos = new Vector2(0, row);
        Vector2 rightRookPos = new Vector2(7, row);

        if (!board.at(kingPos).isEmpty) {
            Piece king = ((Piece) board.at(kingPos));
            if (king.movement.getClass() == King.class && !king.boolProperty) {

                // long
                if (!board.at(leftRookPos).isEmpty) {
                    Piece leftRook = ((Piece) board.at(leftRookPos));
                    if (leftRook.movement.getClass() == Rook.class && !leftRook.boolProperty) {
                        if (board.at(new Vector2(1, row)).isEmpty && board.at(new Vector2(2, row)).isEmpty && board.at(new Vector2(3, row)).isEmpty) {
                            boolean castlingPossible = true;
                            for (Map.Entry<Vector2, ArrayList<Vector2>> entry : previousAttacks.entrySet()) {
                                for (int i = 2; i < 5; i++) {
                                    if (entry.getValue().contains(new Vector2(i, row))) {
                                        castlingPossible = false;
                                        break;
                                    }
                                }
                            }
                            if (castlingPossible) castlingMoves.add(new Vector2(2, row));
                        }
                    }
                }

                if (!board.at(rightRookPos).isEmpty) {
                    Piece rightRook = ((Piece) board.at(rightRookPos));
                    if (rightRook.movement.getClass() == Rook.class && !rightRook.boolProperty) {
                        if (board.at(new Vector2(5, row)).isEmpty && board.at(new Vector2(6, row)).isEmpty) {
                            boolean castlingPossible = true;
                            for (Map.Entry<Vector2, ArrayList<Vector2>> entry : previousAttacks.entrySet()) {
                                for (int i = 4; i < 7; i++) {
                                    if (entry.getValue().contains(new Vector2(i, row))) {
                                        castlingPossible = false;
                                        break;
                                    }
                                }
                            }
                            if (castlingPossible) castlingMoves.add(new Vector2(6, row));
                        }
                    }
                }
            }
        }

        return castlingMoves;
    }

    private void switchPlayer() {
        if (nextPlayer == Piece.Color.WHITE) nextPlayer = Piece.Color.BLACK;
        else nextPlayer = Piece.Color.WHITE;
    }
}
