package chess15.engine;

import chess15.board.*;
import chess15.gamemode.Gamemode;
import chess15.gui.interfaces.UIInteface;
import chess15.gui.util.Constants;
import chess15.board.Move;
import chess15.util.WinReason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Chess Engine class that handles the game logic
 */
public class Engine implements EngineInterface {

    public Board board;
    private final RuleSet rules;
    private final Gamemode gamemode;
    private final UIInteface UIRef;

    private HashMap<Vector2, ArrayList<Vector2>> possibleMoves;
    private HashMap<Vector2, ArrayList<Vector2>> previousAttacks;
    public Piece.Color nextPlayer;

    public boolean inCheck = false;
    private ArrayList<Vector2> pieces;
    private ArrayList<Vector2> opponentPieces;

    /**
     * Used to get the moves from a {@link Vector2} position
     * @param position The {@link Vector2} position to get the moves from
     * @return The list of moves from the position
     */
    public ArrayList<Vector2> getMoves(Vector2 position) {
        return possibleMoves.getOrDefault(position, new ArrayList<>());
    }

    /**
     * Gets a random move from all posible moves
     * Can be used for both black and white
     * @return A random {@link Move} from the list of possible moves
     */
    @Override
    public Move getRandomMove() {

        int totalMoves = 0;
        ArrayList<Integer> lengths = new ArrayList<>();
        ArrayList<Vector2> keys = new ArrayList<>();

        for (Map.Entry<Vector2, ArrayList<Vector2>> entry : possibleMoves.entrySet()) {
            int moveCount = entry.getValue().size();
            keys.add(entry.getKey());
            lengths.add(moveCount);
            totalMoves += moveCount;
        }

        int randomIndex = new Random().nextInt(totalMoves);
        int fromIndex = 0;

        for (Integer length : lengths) {
            if (randomIndex < length) {
                break;
            }
            randomIndex -= length;
            fromIndex++;
        }
        Vector2 from = keys.get(fromIndex);

        return new Move(from, possibleMoves.get(from).get(randomIndex));
    }

    /**
     * Gets all piece {@link Vector2} positions of the next player
     * @return The list of all {@link Vector2} positions of the next player's pieces
     */
    public ArrayList<Vector2> getPieces() {
        return new ArrayList<>(pieces);
    }

    /**
     * Gets all opponent's piece {@link Vector2} positions
     * @return The list of all {@link Vector2} positions of the opponent's pieces
     */
    public ArrayList<Vector2> getOpponentPieces() {
        return new ArrayList<>(opponentPieces);
    }

    /**
     * Perform a move on the board
     * @param from The {@link Vector2} position of the piece to move
     * @param to The {@link Vector2} position to move the piece to
     */
    public void move(Vector2 from, Vector2 to) {
        for (Vector2 p : pieces) {
            Piece piece = (Piece) board.at(p);
            if (piece.movement.getClass() == Pawn.class) piece.boolProperty = false;
        }

        Piece piece = (Piece) board.at(from);

        if (piece.movement.getClass() == Pawn.class) {
            if (rules.promotion && ((nextPlayer == Piece.Color.WHITE && to.y == 0) || (nextPlayer == Piece.Color.BLACK && to.y == 7))) {
                board.elements[from.x][from.y] = new BoardElement();
                if (UIRef != null) {
                    Constants.pauseForPromotion = true;
                    UIRef.promote(nextPlayer, to);
                } else {
                    setPiece(to, new Piece(nextPlayer, Piece.Type.QUEEN, Queen.getInstance(), false));
                }
                return;
            }

            if (Math.abs(from.y - to.y) == 2) piece.boolProperty = true;
            if (from.x != to.x && board.at(to).isEmpty) {
                Vector2 passed = new Vector2(to.x, from.y);

                if (UIRef != null) UIRef.remove(passed, (Piece) board.at(passed));
                board.elements[passed.x][passed.y] = new BoardElement();
            }
        }

        if (rules.castling) {
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
                    if (UIRef != null) UIRef.addPiece((Piece) board.at(rookOrigin), rookpos);
                    board.elements[rookOrigin.x][rookOrigin.y] = new BoardElement();

                    if (UIRef != null) {
                        UIRef.remove(rookOrigin, null);
                    }
                }
            }

            if (piece.movement.getClass() == Rook.class) piece.boolProperty = true;
        }

        board.elements[to.x][to.y] = board.at(from);
        board.elements[from.x][from.y] = new BoardElement();

        pieces = selectPieces(nextPlayer);
        opponentPieces = selectPieces(switchColor(nextPlayer));
        previousAttacks = calculateMoveMap(true);
        switchPlayer();
        possibleMoves = calculateMoveMap(false);
    }

    /**
     * Sets a piece on the board
     * @param position The {@link Vector2} position to set the piece on
     * @param piece The {@link Piece} to set
     */
    public void setPiece(Vector2 position, Piece piece) {
        board.elements[position.x][position.y] = piece;

        pieces = selectPieces(nextPlayer);
        opponentPieces = selectPieces(switchColor(nextPlayer));
        previousAttacks = calculateMoveMap(true);
        switchPlayer();
        possibleMoves = calculateMoveMap(false);
    }

    /**
     * Gets the {@link Board} of the game
     * @return The {@link Board} of the game
     */
    @Override
    public Board getBoard() {
        return new Board(board);
    }

    /**
     * Resets the game state to the starting position
     */
    public void reset() {
        board = gamemode.startState();
        possibleMoves = new HashMap<>();
        previousAttacks = new HashMap<>();
        pieces = new ArrayList<>();
        opponentPieces = new ArrayList<>();
        nextPlayer = Piece.Color.WHITE;
        switchPlayer();
        previousAttacks = calculateMoveMap(true);
        switchPlayer();
        possibleMoves = calculateMoveMap(false);
    }

    /**
     * Copy constructor for the {@link Engine} class,
     * that creates a copy of the {@link Engine} passed in
     * @param original The {@link Engine} to copy
     */
    public Engine(Engine original) {
        gamemode = null;
        UIRef = null;
        this.rules = original.rules;
        this.board = new Board(original.board);
        pieces = new ArrayList<>(original.pieces);
        opponentPieces = new ArrayList<>(original.opponentPieces);
        nextPlayer = original.nextPlayer;
        previousAttacks = new HashMap<>(original.previousAttacks);
        possibleMoves = new HashMap<>(original.possibleMoves);
        inCheck = original.inCheck;
    }

    /**
     * Main constructor for the {@link Engine} class
     * @param rules The {@link RuleSet} to use in the game
     * @param uiRef The {@link UIInteface} to use for callbacks
     */
    public Engine(RuleSet rules, UIInteface uiRef) {
        this.gamemode = rules.gamemode;
        this.rules = rules;
        UIRef = uiRef;
        reset();
    }

    /**
     * Constructor for the {@link Engine} class with player to move option
     * Used for {@link chess15.algorithm.ChessAlgorithm}
     * @param rules The {@link RuleSet} to use in the game
     * @param board The {@link Board} to use
     * @param playerToMove The {@link Piece.Color} of the player to move
     */
    public Engine(RuleSet rules, Board board, Piece.Color playerToMove) {
        gamemode = null;
        UIRef = null;
        this.rules = rules;
        this.board = board;
        possibleMoves = new HashMap<>();
        previousAttacks = new HashMap<>();

        nextPlayer = playerToMove;
        switchPlayer();
        previousAttacks = calculateMoveMap(true);
        switchPlayer();
        possibleMoves = calculateMoveMap(false);
    }

    /**
     * Switch a color from black to white or vice versa
     * @param color The {@link Piece.Color} to switch
     * @return The opposite color {@link Piece.Color}
     */
    public static Piece.Color switchColor(Piece.Color color) {
        if (color == Piece.Color.WHITE) return Piece.Color.BLACK;
        else return Piece.Color.WHITE;
    }

    /**
     * Calculate the move map for the current player
     * @param onlyAttacks Calculate only the positions the player can attack
     * @return The {@link HashMap} of the move map with the {@link Vector2} position as the key and the {@link ArrayList} of {@link Vector2} moves as the value
     */
    private HashMap<Vector2, ArrayList<Vector2>> calculateMoveMap(boolean onlyAttacks) {

        if (UIRef != null && opponentPieces.size() == 1 && pieces.size() == 1)
            UIRef.endGame(null, WinReason.NOMATERIAL);

        for (Vector2 p : opponentPieces) {
            ((Piece) board.at(p)).pin = null;
        }


        HashMap<Vector2, ArrayList<Vector2>> movemap = new HashMap<>();

        int last = pieces.size() - 1;
        Vector2 kingPos = pieces.get(last);

        Vector2 checkGivenby = null;
        boolean checkAvoidable = true; //by capturing or blocking the piece that's giving check, (if there are several it's inavoidable)

        ArrayList<Vector2> kingMoves = calculateMoves(kingPos, onlyAttacks);

        if (!onlyAttacks) {
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
        }


        inCheck = (checkGivenby != null);

        if (rules.castling) kingMoves.addAll(castlingMoves());

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
        Piece.Color winner = switchColor(nextPlayer);
        if (nomoves && UIRef != null) {
            if (inCheck) {
                UIRef.endGame(winner, WinReason.CHECKMATE);
            } else {
                UIRef.endGame(null, WinReason.STALEMATE);
            }
            return null;
        }

        return movemap;
    }

    /**
     * Calculate the moves for a piece at a position
     * @param position The {@link Vector2} position of the piece
     * @param onlyAttacks Calculate only the positions the piece can attack
     * @return The {@link ArrayList} of {@link Vector2} moves
     */
    private ArrayList<Vector2> calculateMoves(Vector2 position, boolean onlyAttacks) {
        BoardElement e = board.at(position);
        ArrayList<Vector2> moves = new ArrayList<>();
        if (e.isEmpty) return moves;
        Piece p = (Piece) e;
        boolean isAttackDifferent = p.movement.attackDifferent;

        ArrayList<Vector2> moveDirections = filterDirections(orient(p.movement.moves, p.movement.whiteDifferent, p.color), p.pin, onlyAttacks);

        if (p.movement instanceof Pawn) {
            Vector2 twoSquares = null;
            if ((position.y == 1 && p.color == Piece.Color.BLACK && board.at(new Vector2(position.x, 2)).isEmpty && board.at(new Vector2(position.x, 3)).isEmpty))
                twoSquares = new Vector2(0, 2);
            else if (position.y == 6 && p.color == Piece.Color.WHITE && board.at(new Vector2(position.x, 5)).isEmpty && board.at(new Vector2(position.x, 4)).isEmpty)
                twoSquares = new Vector2(0, -2);
            if (twoSquares != null && filterDirection(twoSquares, p.pin, onlyAttacks))
                moveDirections.add(twoSquares);
        }

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
            ArrayList<Vector2> attackDirections = filterDirections(orient(p.movement.attacks, p.movement.whiteDifferent, p.color), p.pin, onlyAttacks);

            for (Vector2 attackDirection : attackDirections) {
                Vector2 candidate = Vector2.add(position, attackDirection);
                if (evalMove(candidate, true, true, onlyAttacks)) moves.add(candidate);
            }

            if (rules.enpassant && p.movement instanceof Pawn) {
                Vector2 left = Vector2.add(position, new Vector2(-1, 0));
                Vector2 right = Vector2.add(position, new Vector2(1, 0));

                Vector2 enpassant = null;

                if (!left.outOfBounds() && !board.at(left).isEmpty) {
                    Piece leftPiece = (Piece) board.at(left);
                    if (leftPiece.movement.getClass() == Pawn.class && leftPiece.color != p.color && leftPiece.boolProperty) {
                        enpassant = new Vector2(-1, 1);

                    }
                }
                if (!right.outOfBounds() && !board.at(right).isEmpty) {
                    Piece rightPiece = (Piece) board.at(right);
                    if (rightPiece.movement.getClass() == Pawn.class && rightPiece.color != p.color && rightPiece.boolProperty) {
                        enpassant = new Vector2(1, 1);
                    }
                }
                if (enpassant != null) {
                    if (p.color == Piece.Color.WHITE) enpassant = enpassant.flip();
                    if (filterDirection(enpassant, p.pin, onlyAttacks)) moves.add(Vector2.add(position, enpassant));
                }

            }
        }


        return moves;
    }

    /**
     * Select the pieces of the given {@link Piece.Color}
     * @param color The {@link Piece.Color} of the pieces to select
     * @return The {@link ArrayList} of {@link Vector2} positions for selected pieces
     */
    public ArrayList<Vector2> selectPieces(Piece.Color color) {
        ArrayList<Vector2> selectedPieces = new ArrayList<>();
        Vector2 king = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Vector2 position = new Vector2(i, j);
                BoardElement target = board.at(position);
                if (!target.isEmpty) {
                    Piece piece = ((Piece) target);
                    if (piece.color == color) {
                        if (piece.isKing) king = position;
                        else selectedPieces.add(position);
                    }
                }
            }
        }
        selectedPieces.add(king);
        return selectedPieces;
    }

    /**
     * Evaluate a move
     * @param pos The {@link Vector2} to evaluate
     * @param isAttack Allow captures
     * @param onlyAttack Only allow captures
     * @param forceAttack Allow only moves that could be captures
     * @return Whether the move is valid
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

    /**
     * Repeat a move in a direction for sliding pieces
     * @param pos The {@link Vector2} position to start from
     * @param direction The {@link Vector2} direction to move in
     * @param allowAttack Allow captures
     * @param forceAttack Allow only moves that could be captures
     * @return The {@link ArrayList} of {@link Vector2} moves
     */
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
                if (target.color == nextPlayer) {
                    if (forceAttack) moves.add(candidate);
                    return moves;
                }
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

    /**
     * Orient the move based on whether the piece is white or black
     * @param rawDirections The {@link ArrayList} of {@link Vector2} directions to orient
     * @param whiteDifferent Whether the moveset is different when the piece is white
     * @param color The {@link Piece.Color} of the {@link Piece}
     * @return The {@link ArrayList} of {@link Vector2} moves with the correct direction
     */
    private ArrayList<Vector2> orient(ArrayList<Vector2> rawDirections, boolean whiteDifferent, Piece.Color color) {
        if (whiteDifferent && color == Piece.Color.WHITE) {
            ArrayList<Vector2> directions = new ArrayList<>();
            rawDirections.forEach(direction -> directions.add(direction.flip()));
            return directions;
        } else return rawDirections;

    }

    /**
     * Filter a direction based on a pin
     * @param direction The {@link Vector2} direction to filter
     * @param pinDirection The {@link Vector2} pin direction
     * @param onlyAttacks Whether to only allow attacks (pinned pieces can still give check)
     * @return Whether the direction is valid
     */
    private boolean filterDirection(Vector2 direction, Vector2 pinDirection, boolean onlyAttacks) {
        if (onlyAttacks || pinDirection == null) return true;

        return Vector2.sameDirection(direction, pinDirection);
    }

    /**
     * Filter moves based on a pin
     * @param directions The {@link ArrayList} of {@link Vector2} directions to filter
     * @param pinDirection The {@link Vector2} pin direction
     * @param onlyAttacks Whether to only allow attacks (pinned pieces can still give check)
     * @return The {@link ArrayList} of filtered {@link Vector2} directions
     */
    private ArrayList<Vector2> filterDirections(ArrayList<Vector2> directions, Vector2 pinDirection, boolean onlyAttacks) {
        if (onlyAttacks || pinDirection == null) return new ArrayList<>(directions);

        ArrayList<Vector2> filteredDirections = new ArrayList<>();
        for (Vector2 direction : directions) {
            if (filterDirection(direction, pinDirection, false)) filteredDirections.add(direction);
        }


        return filteredDirections;
    }

    /**
     * Get the castling moves
     * @return The {@link ArrayList} of {@link Vector2} castling moves for the king.
     */
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

                // short
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

    /**
     * Switch the color of the player that moves next
     */
    private void switchPlayer() {
        opponentPieces = selectPieces(nextPlayer);
        nextPlayer = switchColor(nextPlayer);
        pieces = selectPieces(nextPlayer);
    }
}
