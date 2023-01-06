package chess15.gui.newui;

import chess15.algorithm.ChessAlgorithm;
import chess15.board.Move;
import chess15.board.Piece;
import chess15.engine.Engine;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gamemode.Fastpaced;
import chess15.gui.controllers.NewChessController;
import chess15.gui.util.AudioPlayer;
import chess15.util.CustomFormatter;
import chess15.util.WinReason;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;

import static chess15.gui.newui.Variables.*;

public class Initializer {
    public void initGameElements(NewChessController chessController) {
        engine = new Engine(RuleSet.getInstance(), chessController);
        if (RuleSet.getInstance().isAiGame) alg = new ChessAlgorithm(RuleSet.getInstance(), algColor);
        Pane timerPane = setUpTimerLook();
        if (RuleSet.getInstance().timer) setUpTimer(chessController);

        Pane takenBgPane = setUpTakenPieces();

        Pane bottomPane = setUpBottomPane();

        chessController.rightPanel.getChildren().add(timerPane);
        chessController.rightPanel.getChildren().add(takenBgPane);
        chessController.rightPanel.getChildren().add(bottomPane);

        General.setUpBoard(chessController);

        userInput.setOnAction(e -> General.handleTextMove(userInput.getCharacters().toString(), chessController));

        if (DEVMODE) {
            devInit(chessController);
            logger.setUseParentHandlers(false);
            try {
                logFileHandler = new FileHandler(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logFileHandler.setFormatter(new CustomFormatter());
            logger.addHandler(logFileHandler);
            logger.info("Starting game");
            logger.info("With Rules: " + RuleSet.getInstance().toString());
            logger.info("Starting game");
        }

        if (chessController.rightPanel.getScene() != null) {
            chessController.rightPanel.getScene().getWindow().setOnCloseRequest(e -> {
                General.onExitCleanup();
            });
        } else {
            chessController.rightPanel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getWindow().setOnCloseRequest(e -> {
                        General.onExitCleanup();
                    });
                }
            });
        }

        AudioPlayer.playStartSound();

        pausedForSetup = false;
    }

    private Pane setUpBottomPane() {
        Pane basePane = new Pane();
        basePane.setPrefHeight(230);
        basePane.setPrefWidth(560);

        Pane movePane = generateMovePane();
        Pane chatPane = generateChatPane();

        movePane.setLayoutY(30);
        movePane.setLayoutX(30);

        chatPane.setLayoutX(310);
        chatPane.setLayoutY(30);

        basePane.getChildren().add(movePane);
        basePane.getChildren().add(chatPane);
        return basePane;
    }

    private Pane generateMovePane() {
        Pane basePane = new Pane();

        basePane.setPrefWidth(225);
        basePane.setPrefHeight(200);

        DropShadow ds = new DropShadow();
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.25));
        ds.setOffsetX(0);
        ds.setOffsetY(4);

        TableColumn<MoveRepr, String> whiteMoves = new TableColumn<MoveRepr, String>("White");
        TableColumn<MoveRepr, String> blackMoves = new TableColumn<MoveRepr, String>("Black");

        whiteMoves.setCellValueFactory(new PropertyValueFactory<MoveRepr, String>("whiteMove"));
        blackMoves.setCellValueFactory(new PropertyValueFactory<MoveRepr, String>("blackMove"));

        whiteMoves.prefWidthProperty().bind(moveTable.widthProperty().divide(2).subtract(2));
        blackMoves.prefWidthProperty().bind(moveTable.widthProperty().divide(2).subtract(2));

        whiteMoves.setReorderable(false);
        whiteMoves.setResizable(false);

        blackMoves.setReorderable(false);
        blackMoves.setResizable(false);

        moveTable.getColumns().addAll(whiteMoves, blackMoves);
        moveTable.setEditable(false);
        moveTable.setPrefHeight(200);
        moveTable.setPrefWidth(225);
        moveTable.setEffect(ds);

        basePane.getChildren().add(moveTable);
        return basePane;
    }

    private Pane generateChatPane() {
        Pane basePane = new Pane();

        basePane.setPrefWidth(225);
        basePane.setPrefHeight(200);

        DropShadow ds = new DropShadow();
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.25));
        ds.setOffsetX(0);
        ds.setOffsetY(4);

        chatPrevList = new ListView<>();
        chatPrevList.setPrefHeight(150);
        chatPrevList.setPrefWidth(220);
        chatPrevList.setEditable(false);
        chatPrevList.setEffect(ds);

        userInput = new TextField();
        userInput.setLayoutY(150+20);
        userInput.setPrefHeight(30);
        userInput.setPrefWidth(220);
        userInput.setEffect(ds);
        userInput.requestFocus();

        basePane.getChildren().add(chatPrevList);
        basePane.getChildren().add(userInput);
        return basePane;
    }

    private Pane setUpTakenPieces() {
        Pane basePane = new Pane();
        Pane takenMain = new Pane();

        takenMain.setPrefWidth(560);
        takenMain.setPrefHeight(240);

        whiteTakenScroll = new ScrollPane();
        blackTakenScroll = new ScrollPane();

        takenMain.setPrefWidth(500);
        takenMain.setPrefHeight(200);

        Background bg = new Background(new BackgroundFill(Paint.valueOf("#4a4a4a"),
                CornerRadii.EMPTY, Insets.EMPTY));

        DropShadow ds = new DropShadow();
        ds.setRadius(4);
        ds.setColor(Color.color(0, 0, 0, 0.25));
        ds.setOffsetX(0);
        ds.setOffsetY(4);

        whiteTakenScroll.setPrefHeight(90);
        whiteTakenScroll.setPrefWidth(500);
        whiteTakenScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        whiteTakenScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        whiteTaken = new Pane();
        whiteTaken.setPrefWidth(500);
        whiteTaken.setPrefHeight(90);
        whiteTaken.setBackground(bg);
        whiteTakenScroll.setContent(whiteTaken);
        whiteTakenScroll.setEffect(ds);

        blackTakenScroll.setPrefHeight(90);
        blackTakenScroll.setPrefWidth(500);
        blackTakenScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        blackTakenScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        blackTaken = new Pane();
        blackTaken.setPrefWidth(500);
        blackTaken.setPrefHeight(90);
        blackTaken.setBackground(bg);
        blackTakenScroll.setContent(blackTaken);
        blackTakenScroll.setEffect(ds);

        whiteTakenScroll.setLayoutY(0);
        blackTakenScroll.setLayoutY(110);

        takenMain.getChildren().add(whiteTakenScroll);
        takenMain.getChildren().add(blackTakenScroll);

        takenMain.setLayoutX(30);
        takenMain.setLayoutY(30);
        basePane.getChildren().add(takenMain);
        return basePane;
    }

    private Pane setUpTimerLook() {
        Pane timerMain = new Pane();

        // Setup Timer main panel
        Pane timerBackground = new Pane();
        timerBackground.setPrefWidth(500);
        timerBackground.setPrefHeight(200);
        Background timerBG = new Background(new BackgroundFill(Paint.valueOf("#3a3a3a"),
                new CornerRadii(20), Insets.EMPTY));
        timerBackground.setBackground(timerBG);
        timerBackground.setLayoutX(30);
        timerBackground.setLayoutY(30);
        DropShadow timerBGDropShadow = new DropShadow();
        timerBGDropShadow.setRadius(10);
        timerBGDropShadow.setOffsetX(0);
        timerBGDropShadow.setOffsetY(4);
        timerBGDropShadow.setColor(Color.color(0, 0, 0, 0.25));
        timerBackground.setEffect(timerBGDropShadow);

        // Setup timer boxes
        Pane whiteTimerBox = generateWhiteBox();
        Pane blackTimerBox = generateBlackBox();

        // Position timer boxes
        whiteTimerBox.setLayoutX(25);
        whiteTimerBox.setLayoutY(25);

        blackTimerBox.setLayoutX(25 + 200 + 50);
        blackTimerBox.setLayoutY(25);

        timerBackground.getChildren().add(whiteTimerBox);
        timerBackground.getChildren().add(blackTimerBox);
        timerMain.getChildren().add(timerBackground);
        return timerMain;
    }

    private Pane generateWhiteBox() {
        Pane baseBox = generateBox();

        whiteTimerLabel = new Label();
        whiteTimerLabel.setText("0:0");
        whiteTimerLabel.setFont(timerFont);
        whiteTimerLabel.setTextFill(Paint.valueOf("#2a2a2a"));
        DropShadow labelShadow = new DropShadow();
        labelShadow.setRadius(4);
        labelShadow.setOffsetY(4);
        labelShadow.setOffsetX(0);
        labelShadow.setColor(Color.color(0,0,0,0.25));
        whiteTimerLabel.setEffect(labelShadow);

        whiteTimerLabel.layoutXProperty().bind(baseBox.widthProperty().subtract(whiteTimerLabel.widthProperty()).divide(2));
        whiteTimerLabel.layoutYProperty().bind(baseBox.heightProperty().subtract(whiteTimerLabel.heightProperty()).divide(2));
        baseBox.getChildren().add(whiteTimerLabel);
        return baseBox;
    }

    private Pane generateBlackBox() {
        Pane baseBox = generateBox();

        blackTimerLabel = new Label();
        blackTimerLabel.setText("0:0");
        blackTimerLabel.setFont(timerFont);
        blackTimerLabel.setTextFill(Paint.valueOf("#2a2a2a"));
        DropShadow labelShadow = new DropShadow();
        labelShadow.setRadius(4);
        labelShadow.setOffsetY(4);
        labelShadow.setOffsetX(0);
        labelShadow.setColor(Color.color(0,0,0,0.25));
        blackTimerLabel.setEffect(labelShadow);

        blackTimerLabel.layoutXProperty().bind(baseBox.widthProperty().subtract(blackTimerLabel.widthProperty()).divide(2));
        blackTimerLabel.layoutYProperty().bind(baseBox.heightProperty().subtract(blackTimerLabel.heightProperty()).divide(2));
        baseBox.getChildren().add(blackTimerLabel);
        return baseBox;
    }

    private Pane generateBox() {
        Pane baseBox = new Pane();

        baseBox.setPrefWidth(200);
        baseBox.setPrefHeight(150);
        Background baseBoxBG = new Background(new BackgroundFill(Paint.valueOf("#d9d9d9"),
                new CornerRadii(10), Insets.EMPTY));
        baseBox.setBackground(baseBoxBG);
        DropShadow baseboxDropShadow = new DropShadow();
        baseboxDropShadow.setColor(Color.color(0, 0, 0, 0.25));
        baseboxDropShadow.setOffsetX(0);
        baseboxDropShadow.setOffsetY(4);
        baseboxDropShadow.setRadius(4);
        baseBox.setEffect(baseboxDropShadow);

        return baseBox;
    }

    private void setUpTimer(NewChessController chessController) {
        whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
        blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;

        if (RuleSet.getInstance().gamemode instanceof Fastpaced) initFastPacedThread(chessController, engine);
        else initTimerThread(chessController);
    }

    private void initFastPacedThread(NewChessController chessController, EngineInterface engine) {
        timerThread = new Thread(() -> {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!pausedForPromotion && !pausedForSetup) {
                    if (fastPacedCounter >= FASTPACEDTIMEOUT) {
                        fastPacedCounter = 0;
                        Move randomMove = engine.getRandomMove();
                        Platform.runLater(() -> chessController.movePiece(randomMove));
                    }
                }
            }
        });

        timerThread.start();
    }

    private void initTimerThread(NewChessController chessController) {
        whiteTimerLabel.setText(formatTime(whiteTimeInMillis));
        blackTimerLabel.setText(formatTime(blackTimeInMillis));
        timerWhiteSide = true;
        timerThread = new Thread(() -> {
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (timerWhiteSide && !pausedForSetup) {
                    whiteTimeInMillis -= 10;

                    if (whiteTimeInMillis <= 0) {
                        isRunning = false;
                        Platform.runLater(() -> chessController.endGame(Piece.Color.BLACK, WinReason.TIMEOUT));
                    }

                    Platform.runLater(() -> whiteTimerLabel.setText(formatTime(whiteTimeInMillis)));
                } else if (!timerWhiteSide && !pausedForSetup) {
                    blackTimeInMillis -= 10;

                    if (blackTimeInMillis <= 0) {
                        isRunning = false;
                        Platform.runLater(() -> chessController.endGame(Piece.Color.WHITE, WinReason.TIMEOUT));
                    }

                    Platform.runLater(() -> blackTimerLabel.setText(formatTime(blackTimeInMillis)));
                }
            }
        });

        timerThread.start();
    }

    public static String formatTime(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) ((timeInMillis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    private static void devInit(NewChessController chessController) {
        // Development command to reset the board
        EventHandler<KeyEvent> resetHandler = e -> General.guiReset(chessController, e, engine);

        // Development command to force checkmate ending
        EventHandler<KeyEvent> checkmateHandler = e -> {
            if (!chessController.chessBoardPane.getChildren().contains(endGameBase)) {
                if (e.getCode() == KeyCode.C && e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(Piece.Color.WHITE, WinReason.CHECKMATE);
                } else if (e.getCode() == KeyCode.C && !e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(Piece.Color.BLACK, WinReason.CHECKMATE);
                }
            }
        };

        // Development command to force timeout ending
        EventHandler<KeyEvent> timeoutHandler = e -> {
            if (!chessController.chessBoardPane.getChildren().contains(endGameBase)) {
                if (e.getCode() == KeyCode.T && e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(Piece.Color.WHITE, WinReason.TIMEOUT);
                } else if (e.getCode() == KeyCode.T && !e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(Piece.Color.BLACK, WinReason.TIMEOUT);
                }
            }
        };

        // Development command to force stalemate ending
        EventHandler<KeyEvent> drawHandler = e -> {
            if (!chessController.chessBoardPane.getChildren().contains(endGameBase)) {
                if (e.getCode() == KeyCode.S && e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(null, WinReason.STALEMATE);
                } else if (e.getCode() == KeyCode.S && !e.isControlDown() && e.isAltDown()) {
                    chessController.endGame(null, WinReason.NOMATERIAL);
                }
            }
        };

        // Development command to force stalemate ending
        EventHandler<KeyEvent> warnErrTrigger = e -> {
            if (e.getCode() == KeyCode.E && e.isAltDown()) {
                logger.severe("Manual error triggered");
            }
            if (e.getCode() == KeyCode.W && e.isAltDown()) {
                logger.warning("Manual warning triggered");
            }
        };

        if (chessController.rightPanel.getScene() != null) {
            chessController.rightPanel.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
            General.addEventHandlers(chessController.rightPanel.getScene(),
                    checkmateHandler, timeoutHandler, drawHandler, warnErrTrigger);
        } else {
            chessController.rightPanel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
                    General.addEventHandlers(newScene,
                            checkmateHandler, timeoutHandler, drawHandler, warnErrTrigger);
                }
            });
        }
    }
}
