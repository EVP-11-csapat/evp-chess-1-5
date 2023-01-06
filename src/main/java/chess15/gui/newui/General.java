package chess15.gui.newui;

import chess15.algorithm.ChessAlgorithm;
import chess15.board.BoardElement;
import chess15.board.Move;
import chess15.board.Piece;
import chess15.board.Vector2;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gui.controllers.NewChessController;
import chess15.gui.scenes.ResourceGrabber;
import chess15.util.WinReason;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chess15.gui.newui.Variables.*;

public class General {
    public static void onExitCleanup() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread.stop();
        }
    }

    public static void setUpBoard(NewChessController chessController) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardElement element = engine.getBoard().elements[j][i];
                Piece piece;
                if (!(element instanceof Piece)) continue;
                piece = (Piece) element;
                Vector2 pos = new Vector2(j, i);
                chessController.addPiece(piece, pos);
            }
        }
        pausedForSetup = false;
    }

    public static String getPieceColorString(Piece p) {
        if (p.color == Piece.Color.WHITE) return "white";
        else return "black";
    }

    public static String getPieceTypeString(Piece p) {
        switch (p.look) {
            case KING -> {
                return "king";
            }
            case PAWN -> {
                return "pawn";
            }
            case ROOK -> {
                return "rook";
            }
            case QUEEN -> {
                return "queen";
            }
            case BISHOP -> {
                return "bishop";
            }
            case KNIGHT -> {
                return "knight";
            }
            default -> {
                return "";
            }
        }
    }

    public static void handleTextMove(String text, NewChessController chessController) {
        // handle resignation and restart
        if (!chessController.chessBoardPane.getChildren().contains(endGameBase)) {
            switch (text) {
                case "resign" -> {
                    // if resign is typed by itself, we resign the player that is about to move
                    if (whiteToMove) chessController.endGame(Piece.Color.BLACK, WinReason.RESIGNITION);
                    else chessController.endGame(Piece.Color.WHITE, WinReason.RESIGNITION);
                }
                case "rw", "resign white", "white resign" -> chessController.endGame(Piece.Color.BLACK, WinReason.RESIGNITION);
                case "rb", "resign black", "black resign" -> chessController.endGame(Piece.Color.WHITE, WinReason.RESIGNITION);
                case "draw" -> chessController.endGame(null, WinReason.DRAW);
            }
        }

        if (text.equals("reset") || text.equals("restart") || text.equals("rematch"))
            reset(chessController, engine);

        // handle AI color switch
        if (RuleSet.getInstance().isAiGame) {
            if (text.equals("color swap") || text.equals("color switch") || text.equals("swap color") || text.equals("switch color") ||
                    text.equals("ai switch") || text.equals("switch ai")) {
                switchAiMoveColor();
                reset(chessController, engine);
                if (algColor == Piece.Color.WHITE) {
                    algMoveThreads = new Thread(() -> {
                        Move computerMove = alg.move(engine.getBoard(), null);
                        Platform.runLater(() -> chessController.movePiece(computerMove));
                    });
                    algMoveThreads.start();
                }
            }
        }

        Pattern testPattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
        Matcher patternMatcher = testPattern.matcher(text.toLowerCase());
        boolean correctInput = patternMatcher.matches();
        if (correctInput) {
            if (DEVMODE)
                System.out.println("Pattern FOUND");
            Move move = getMoveFromText(text.toLowerCase(), engine);
            if (move.from != null && move.from.x != -1) {
                chessController.movePiece(move);
                userInput.clear();
            } else {
                chatPrevList.getItems().add("Invalid Move");
                chatPrevList.scrollTo(chatPrevList.getItems().size());
                userInput.clear();
            }

            if (DEVMODE)
                logger.info("Text move issued: " + text);
        } else {
            userInput.clear();
            if (DEVMODE)
                System.out.println("Pattern NOT FOUND");
        }
        chatPrevList.getItems().add(text);
        chatPrevList.scrollTo(chatPrevList.getItems().size());
    }

    public static void reset(NewChessController chessController, EngineInterface engine) {
        pausedForSetup = true;
        engine.reset();
        if (RuleSet.getInstance().isAiGame) alg = new ChessAlgorithm(RuleSet.getInstance(), algColor);
        chessController.removePosibleMoves();
        chessController.removeFromTo();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessController.remove(new Vector2(j, i), null);
            }
        }
        moveTable.getItems().clear();

        if (whiteTaken != null) whiteTaken.getChildren().clear();
        if (blackTaken != null) blackTaken.getChildren().clear();

        pieces.clear();
        takenPieces = new ArrayList<>();
        takenList = new HashMap<>();
        if (RuleSet.getInstance().timer) {
            whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            whiteTimerLabel.setText(Initializer.formatTime(whiteTimeInMillis));
            blackTimerLabel.setText(Initializer.formatTime(blackTimeInMillis));
            timerWhiteSide = true;
        }
        chessController.chessBoardPane.getChildren().remove(promotionUIBase);
        promotionList.clear();
        chessController.chessBoardPane.getChildren().remove(endGameBase);
        endGameBase = new StackPane();
        whiteToMove = true;
        chatPrevList.getItems().clear();
        userInput.clear();
        setUpBoard(chessController);
        pausedForSetup = false;
    }

    private static void switchAiMoveColor() {
        if (algColor == Piece.Color.WHITE) algColor = Piece.Color.BLACK;
        else algColor = Piece.Color.WHITE;
    }

    public static void changeScene(Stage stage) {
        try {
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(ResourceGrabber.getInstance().getClass().getResource("newMainMenu.fxml")));
            stage.getScene().setRoot(newRoot);
            stage.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertMoveToChessCord(Vector2 move) {
        switch (move.x) {
            case 0 -> {
                return "A" + (8 - move.y);
            }
            case 1 -> {
                return "B" + (8 - move.y);
            }
            case 2 -> {
                return "C" + (8 - move.y);
            }
            case 3 -> {
                return "D" + (8 - move.y);
            }
            case 4 -> {
                return "E" + (8 - move.y);
            }
            case 5 -> {
                return "F" + (8 - move.y);
            }
            case 6 -> {
                return "G" + (8 - move.y);
            }
            case 7 -> {
                return "H" + (8 - move.y);
            }
        }
        return null;
    }

    public static Vector2 convertChessCoordToMove(String move) {
        String x = move.substring(0, 1);
        String y = move.substring(1, 2);

        return switch (x) {
            case "a" -> new Vector2(0, 8 - Integer.parseInt(y));
            case "b" -> new Vector2(1, 8 - Integer.parseInt(y));
            case "c" -> new Vector2(2, 8 - Integer.parseInt(y));
            case "d" -> new Vector2(3, 8 - Integer.parseInt(y));
            case "e" -> new Vector2(4, 8 - Integer.parseInt(y));
            case "f" -> new Vector2(5, 8 - Integer.parseInt(y));
            case "g" -> new Vector2(6, 8 - Integer.parseInt(y));
            case "h" -> new Vector2(7, 8 - Integer.parseInt(y));
            default -> new Vector2(-1, -1);
        };
    }

    public static Move getMoveFromText(String moveText, EngineInterface engine) {
        Vector2 from = convertChessCoordToMove(moveText.substring(0,2));
        Vector2 to = convertChessCoordToMove(moveText.substring(2, 4));
        if (engine.getMoves(from).contains(to)) {
            return new Move(from, to);
        }
        return new Move(null, null);
    }

    @SafeVarargs
    public static void addEventHandlers(Scene scene, EventHandler<KeyEvent>... eventHandlers) {
        for (EventHandler<KeyEvent> eventHandler : eventHandlers) {
            scene.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        }
    }

    public static void guiReset(NewChessController chessController, KeyEvent e, EngineInterface engine) {
        if (e.getCode() == KeyCode.R && e.isControlDown() && e.isAltDown()) {
            reset(chessController, engine);
        }
    }
}
