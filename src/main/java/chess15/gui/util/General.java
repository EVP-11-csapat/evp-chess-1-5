package chess15.gui.util;

import chess15.Piece;
import chess15.Vector2;
import chess15.algorithm.ChessAlgorithm;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gui.controllers.ChessController;
import chess15.gui.scenes.ResourceGrabber;
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

/**
 * General class is used to store methods
 */
public class General {
    /**
     * Stops the timer thread on exit
     */
    @SuppressWarnings("deprecation")
    public static void threadStop() {
        Constants.timerThread.interrupt();
        Constants.timerThread.stop();
        if (Constants.algMoveThreads != null) {
            Constants.algMoveThreads.interrupt();
            Constants.algMoveThreads.stop();
        }
    }

    /**
     * Adds the dev command event listeners to the scene
     * @param scene The scene we want to add the listeners to
     * @param eventHandlers The event handlers we want to apply
     */
    @SafeVarargs
    public static void addEventHandlers(Scene scene, EventHandler<KeyEvent>... eventHandlers) {
        for (EventHandler<KeyEvent> eventHandler : eventHandlers) {
            scene.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        }
    }

    /**
     * Resets the GUI. Used to play the same settings again after the game ends
     * @param chessController The {@link ChessController}. Used for access.
     * @param engine The engine. Also gets reset form here.
     */
    public static void reset(ChessController chessController, EngineInterface engine) {
        engine.reset();
        if (RuleSet.getInstance().isAiGame) Constants.alg = new ChessAlgorithm(RuleSet.getInstance(), Constants.AlgColor);
        Constants.board = engine.getBoard();
        chessController.removePosibleMoves();
        chessController.removeFromTo();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessController.remove(new Vector2(j, i), null);
            }
        }
        chessController.moveListElement.getItems().clear();

        if (Constants.whiteTaken != null) Constants.whiteTaken.getChildren().clear();
        if (Constants.blackTaken != null) Constants.blackTaken.getChildren().clear();

        Constants.pieces.clear();
        Constants.takenPieces = new ArrayList<>();
        Constants.takenList = new HashMap<>();
        if (RuleSet.getInstance().timer) {
            Constants.whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            Constants.blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            Constants.whiteTimerLabel.setText(TimerInit.formatTime(Constants.whiteTimeInMillis));
            Constants.blackTimerLabel.setText(TimerInit.formatTime(Constants.blackTimeInMillis));
            Constants.whiteSide = true;
        }
        chessController.chessBoardPane.getChildren().remove(Constants.promotionUIBase);
        Constants.promotionList.clear();
        chessController.chessBoardPane.getChildren().remove(Constants.endGameBase);
        Constants.endGameBase = new StackPane();
        Constants.playedMoves.clear();
        Constants.whiteToMove = true;
        chessController.setUpBoard();
    }

    /**
     * Resets the GUI. Used to play the same settings again after the game ends<br />
     * This is the Key Event version
     * @param chessController The {@link ChessController}. Used for access.
     * @param e The key event
     * @param engine The engine. Also gets reset form here.
     */
    public static void guiReset(ChessController chessController, KeyEvent e, EngineInterface engine) {
        if (e.getCode() == KeyCode.R && e.isControlDown() && e.isAltDown()) {
            reset(chessController, engine);
        }
    }

    /**
     * Convert the move coordiantes to chess coordinates. Used for the move list
     * @param move The move {@link Vector2} position we want to convert.
     * @return The converted {@link String}
     */
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

    /**
     * Returns a given pieces type in a string from. Used for image path generation
     * @param p The {@link Piece} we want the type of
     * @return The {@link String} for the generator
     */
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

    /**
     * Returns a given pieces color in a string from. Used for image path generation
     * @param p The {@link Piece} we want the color of
     * @return The {@link String} for the generator
     */
    public static String getPieceColorString(Piece p) {
        if (p.color == Piece.Color.WHITE) return "white";
        else return "black";
    }

    /**
     * Change the scene to the main menu when the game is finished
     * @param stage The stage to switch the scene
     */
    public static void changeScene(Stage stage) {
        try {
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(ResourceGrabber.getInstance().getClass().getResource("mainMenu.fxml")));
            stage.getScene().setRoot(newRoot);
            stage.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
