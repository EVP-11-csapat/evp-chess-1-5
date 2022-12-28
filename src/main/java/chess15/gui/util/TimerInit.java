package chess15.gui.util;

import chess15.Piece;
import chess15.Vector2;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gamemode.Fastpaced;
import chess15.gui.controllers.ChessController;
import chess15.util.Move;
import chess15.util.WinReason;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * Initializes the timer
 */
public class TimerInit {
    /**
     * Initialize the style of the timer
     * @param chessController The {@link ChessController} is used for access.
     */
    public static void initStyles(ChessController chessController) {
        // Set up Timer Environment
        Constants.timerHBox = new HBox();
        Constants.timerHBox.setAlignment(Pos.CENTER);

        Constants.whiteTimerBox = new Pane();
        Constants.blackTimerBox = new Pane();
        Constants.whiteTimerBox.setStyle("-fx-background-color: #cdcdcd");
        Constants.blackTimerBox.setStyle("-fx-background-color: #cdcdcd");
        Constants.whiteTimerBox.setPrefWidth(250);
        Constants.whiteTimerBox.setPrefHeight(200);
        Constants.blackTimerBox.setPrefWidth(250);
        Constants.blackTimerBox.setPrefHeight(200);

        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep.setPrefWidth(47);
        sep.setOpacity(0);

        Constants.whiteTimerLabel = new Label();
        Constants.whiteTimerLabel.setText(formatTime(Constants.whiteTimeInMillis));
        Constants.blackTimerLabel = new Label();
        Constants.blackTimerLabel.setText(formatTime(Constants.blackTimeInMillis));

        Constants.whiteTimerLabel.setFont(new Font("Arial", 100));
        Constants.blackTimerLabel.setFont(new Font("Arial", 100));

        Constants.whiteTimerLabel.layoutXProperty().bind(Constants.whiteTimerBox.widthProperty()
                .subtract(Constants.whiteTimerLabel.widthProperty()).divide(2));
        Constants.whiteTimerLabel.layoutYProperty().bind(Constants.whiteTimerBox.heightProperty()
                .subtract(Constants.whiteTimerLabel.heightProperty()).divide(2));

        Constants.blackTimerLabel.layoutXProperty().bind(Constants.blackTimerBox.widthProperty()
                .subtract(Constants.blackTimerLabel.widthProperty()).divide(2));
        Constants.blackTimerLabel.layoutYProperty().bind(Constants.blackTimerBox.heightProperty()
                .subtract(Constants.blackTimerLabel.heightProperty()).divide(2));

        Constants.whiteTimerBox.getChildren().add(Constants.whiteTimerLabel);
        Constants.blackTimerBox.getChildren().add(Constants.blackTimerLabel);

        Constants.timerHBox.getChildren().addAll(Constants.whiteTimerBox,
                sep, Constants.blackTimerBox);

        chessController.clockPane.getChildren().add(Constants.timerHBox);
    }

    /**
     * Initializes the timer thread used for counting down
     * @param chessController The {@link ChessController} is used for access
     */
    public static void initThread(ChessController chessController) {
        Constants.whiteSide = true;

        Constants.timerThread = new Thread(() -> {
            while (Constants.isRunning && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Constants.whiteSide) {
                    Constants.whiteTimeInMillis -= 10;

                    if (Constants.whiteTimeInMillis <= 0) {
                        Constants.isRunning = false;
                        Constants.whiteTimeRanOut = true;
                        Platform.runLater(() -> chessController.endGame(Piece.Color.BLACK, WinReason.TIMEOUT));
                    }

                    Platform.runLater(() -> Constants.whiteTimerLabel.setText(TimerInit.formatTime(Constants.whiteTimeInMillis)));
                } else {
                    Constants.blackTimeInMillis -= 10;
                    if (Constants.blackTimeInMillis <= 0) {
                        Constants.isRunning = false;
                        Constants.blackTimeRanOut = true;
                        Platform.runLater(() -> chessController.endGame(Piece.Color.WHITE, WinReason.TIMEOUT));
                    }

                    Platform.runLater(() -> Constants.blackTimerLabel.setText(TimerInit.formatTime(Constants.blackTimeInMillis)));
                }
            }
        });

        Constants.timerThread.start();
    }

    /**
     * Initializes the timer thread used for fast-paced mode
     * @param chessController The {@link ChessController} is used for access
     * @param engine The {@link EngineInterface} is used for random moves
     */
    public static void initFastPacedThread(ChessController chessController, EngineInterface engine) {
        Constants.timerThread = new Thread(() -> {
            while (Constants.isRunning && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!Constants.pauseForPromotion) {
                    Constants.fastPacedCounter += 10;
                    if (Constants.fastPacedCounter >= Constants.FASTPACEDTIMEOUT) { // Fire every 2 seconds
                        Constants.fastPacedCounter = 0;
                        Move randomMove = engine.getRandomMove();
                        Platform.runLater(() -> chessController.movePiece(randomMove.from, randomMove.to));
                    }
                }
            }
        });

        Constants.timerThread.start();
    }

    /**
     * Used to format the internally stored long to a readable time format
     * @param timeInMillis The time stored in milliseconds
     * @return A String of readable time
     */
    public static String formatTime(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) ((timeInMillis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
