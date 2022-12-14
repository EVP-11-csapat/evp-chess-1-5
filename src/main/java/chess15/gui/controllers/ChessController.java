package chess15.gui.controllers;

import chess15.algorithm.ChessAlgorithm;
import chess15.board.*;
import chess15.engine.Engine;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gamemode.Fastpaced;
import chess15.gui.images.ImageGrabber;
import chess15.gui.interfaces.UIInteface;
import chess15.gui.util.AudioPlayer;
import chess15.gui.util.Constants;
import chess15.gui.util.General;
import chess15.gui.util.TimerInit;
import chess15.util.CustomFormatter;
import chess15.board.Move;
import chess15.algorithm.PiecePoints;
import chess15.util.WinReason;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The ChessController is responsible for everything UI related in the main scene
 */
public class ChessController implements UIInteface {
    /**
     * List of pieces used for promotion
     */
    private final ArrayList<Piece> PROMOTIONPIECES = new ArrayList<>(List.of(
            new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false)
    ));
    /**
     * Engine used all game mechanics
     */
    private EngineInterface engine;
    /**
     * Chess board {@link Pane}
     */
    @FXML
    public Pane chessBoardPane;
    /**
     * Main {@link Pane} used to attach event handlers
     */
    @FXML
    private Pane main;
    /**
     * Move List {@link ListView<String>} used to display all moves in the game
     */
    @FXML
    public ListView<String> moveListElement;
    /**
     * The {@link ScrollPane} for the black taken pieces
     */
    @FXML
    private ScrollPane blackTakenScroll;
    /**
     * The {@link ScrollPane} for the white taken pieces
     */
    @FXML
    private ScrollPane whiteTakenScroll;
    /**
     * A {@link TextField} for chat input commands and moves
     */
    @FXML
    private TextField inputText;
    /**
     * A {@link Pane} to attach the clock to
     */
    @FXML
    public Pane clockPane;

    /**
     * Initialize is called when the scene is loading
     * Handles every setup action
     */
    public void initialize() {
        engine = new Engine(RuleSet.getInstance(), this);
        if (RuleSet.getInstance().isAiGame) Constants.alg = new ChessAlgorithm(RuleSet.getInstance(), Piece.Color.BLACK);
        Constants.board = engine.getBoard();
        setUpBoard();

        // Development command to reset the board
        EventHandler<KeyEvent> resetHandler = e -> General.guiReset(this, e, engine);

        // Development command to force checkmate ending
        EventHandler<KeyEvent> checkmateHandler = e -> {
            if (!chessBoardPane.getChildren().contains(Constants.endGameBase)) {
                if (e.getCode() == KeyCode.C && e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.WHITE, WinReason.CHECKMATE);
                } else if (e.getCode() == KeyCode.C && !e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.BLACK, WinReason.CHECKMATE);
                }
            }
        };

        // Development command to force timeout ending
        EventHandler<KeyEvent> timeoutHandler = e -> {
            if (!chessBoardPane.getChildren().contains(Constants.endGameBase)) {
                if (e.getCode() == KeyCode.T && e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.WHITE, WinReason.TIMEOUT);
                } else if (e.getCode() == KeyCode.T && !e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.BLACK, WinReason.TIMEOUT);
                }
            }
        };

        // Development command to force stalemate ending
        EventHandler<KeyEvent> drawHandler = e -> {
            if (!chessBoardPane.getChildren().contains(Constants.endGameBase)) {
                if (e.getCode() == KeyCode.S && e.isControlDown() && e.isAltDown()) {
                    endGame(null, WinReason.STALEMATE);
                } else if (e.getCode() == KeyCode.S && !e.isControlDown() && e.isAltDown()) {
                    endGame(null, WinReason.NOMATERIAL);
                }
            }
        };

        // Development command to force stalemate ending
        EventHandler<KeyEvent> warnErrTrigger = e -> {
            if (e.getCode() == KeyCode.E && e.isAltDown()) {
                Constants.logger.severe("Manual error triggered");
            }
            if (e.getCode() == KeyCode.W && e.isAltDown()) {
                Constants.logger.warning("Manual warning triggered");
            }
        };

        // Hook the handlers to the Key Pressed Event
        if (main.getScene() != null) {
            if (Constants.DEVMODE)
                main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
            main.getScene().getWindow().setOnCloseRequest(e -> {
                if (Constants.timerThread != null) {
                    General.threadStop();
                }
            });
            if (Constants.DEVMODE)
                General.addEventHandlers(main.getScene(), checkmateHandler, timeoutHandler,
                        drawHandler, warnErrTrigger);
        } else {
            main.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    if (Constants.DEVMODE)
                        main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
                    main.getScene().getWindow().setOnCloseRequest(e -> {
                        if (Constants.timerThread != null) {
                            General.threadStop();
                        }
                    });
                    if (Constants.DEVMODE)
                        General.addEventHandlers(main.getScene(), checkmateHandler, timeoutHandler,
                                drawHandler, warnErrTrigger);
                }
            });
        }

        // Set handleMove action to chat input
        inputText.setOnAction(e -> handleTextMove(inputText.getCharacters().toString()));

        // Set up timer if timer is enabled in the current game
        if (RuleSet.getInstance().timer) {
            Constants.whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            Constants.blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;

            if (!(RuleSet.getInstance().gamemode instanceof Fastpaced)) TimerInit.initStyles(this);

            if (RuleSet.getInstance().gamemode instanceof Fastpaced) TimerInit.initFastPacedThread(this, engine);
            else TimerInit.initThread(this);
        }
        if (Constants.DEVMODE) {
            Constants.logger.setUseParentHandlers(false);
            try {
                Constants.logFileHandler = new FileHandler(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Constants.logFileHandler.setFormatter(new CustomFormatter());
            Constants.logger.addHandler(Constants.logFileHandler);
            Constants.logger.info("Starting game");
            Constants.logger.info("With Rules: " + RuleSet.getInstance().toString());
            Constants.logger.info("Starting game");
        }

        AudioPlayer.playStartSound();
    }

    // #######################
    // # Board setup methods #
    // #######################

    /**
     * Method used to set up the board and the pieces
     * For every pieve in the stored board creates an ImageView
     */
    public void setUpBoard() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardElement element = Constants.board.elements[j][i];
                Piece piece;
                if (!(element instanceof Piece)) continue;
                piece = (Piece) element;
                Vector2 piecePos = new Vector2(j, i);
                addPiece(piece, piecePos);
            }
        }
        Constants.isRunning = true;
    }

    /**
     * Method used to create the ImageView and Position it on the board
     *
     * @param piece The {@link Piece} object that we want to create the {@link ImageView} for
     * @param pos   The {@link Vector2} used for positioning the {@link Piece}
     */
    @Override
    public void addPiece(Piece piece, Vector2 pos) {
        // Get the image path
        String imagePath = "pieces/" +
                General.getPieceColorString(piece) +
                "-" +
                General.getPieceTypeString(piece) +
                ".png";

        // Get the image from the path above
        Image image;
        try {
            image = new Image(Objects.requireNonNull(ImageGrabber.getInstance().getClass().getResource(imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("ERROR in ChessController (addPiece): Image not found");
            throw new RuntimeException(e);
        }
        ImageView pieceImage = new ImageView();
        // Position and scale the images at the correcto place on the board
        pieceImage.setImage(image);
        pieceImage.setFitHeight(90);
        pieceImage.setFitWidth(90);
        pieceImage.setX(90 * pos.x);
        pieceImage.setY(90 * pos.y);
        // Add the piece to the pieces list for later use
        Constants.pieces.put(pos, pieceImage);
        // Adds the event listener for click
        if (!RuleSet.getInstance().isAiGame || piece.color != Constants.AlgColor)
            addClickEventToPiece(pieceImage);
        // Adds the piece to the board as an image
        chessBoardPane.getChildren().add(pieceImage);

        if (Constants.DEVMODE)
            Constants.logger.info("Piece added: " + piece + " at: " + pos);
    }

    /**
     * Handle the event when the user clicks the {@link Piece} at a given position
     * @param pos The {@link Vector2} that the piece {@link ImageView} is at
     */
    private void handlePieceClick(Vector2 pos) {
        removePosibleMoves();
        displayPosibleMoves(engine.getMoves(pos), pos);
    }

    /**
     * Returns the key of a {@link Map} that is in connection with a value
     * This only works for a 1-1 connection so if one value is only connected to one key
     *
     * @param map   The {@link Map} that contains the key value pairs
     * @param value The value we want the key for
     * @return The key that is connected to the value provided
     */
    private static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Handle the mouse press event for a piece
     */
    EventHandler<MouseEvent> pressedHandler = mouseEvent -> {
        Vector2 handlePos = getKeysByValue(Constants.pieces, (ImageView) mouseEvent.getSource()).get(0);
        handlePieceClick(handlePos);
    };

    /**
     * Update the click event listener to the new position on the piece {@link ImageView}
     * @param pos The {@link Vector2} to update the location to
     */
    private void updateClickEventToPiece(Vector2 pos) {
        ImageView piece = Constants.pieces.get(pos);
        addClickEventToPiece(piece);
    }

    /**
     * Used to put the event listener to the piece {@link ImageView}
     *
     * @param piece The {@link ImageView} to put the listener on to
     */
    private void addClickEventToPiece(ImageView piece) {
        piece.setOnMousePressed(pressedHandler);
    }

    // #################################
    // # Possible Move Display Methods #
    // #################################

    /**
     * Remove the possible move marker from the board
     */
    public void removePosibleMoves() {
        for (Vector2 move : Constants.possibleMoves.keySet()) {
            ImageView imageView = Constants.possibleMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        Constants.possibleMoves.clear();
    }

    // ################################
    // # Move From To Display Methods #
    // ################################

    /**
     * Remove the from to move marker from the board
     */
    public void removeFromTo() {
        for (Vector2 move : Constants.fromToMoves.keySet()) {
            ImageView imageView = Constants.fromToMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        Constants.fromToMoves.clear();
    }

    /**
     * Add the possible move marker to the board at the given position list
     *
     * @param moves The {@link Vector2} move list that we want to put the marker to
     * @param piece The {@link Vector2} position of the piece, so we can assign move listener
     */
    private void displayPosibleMoves(ArrayList<Vector2> moves, Vector2 piece) {
        for (Vector2 move : moves) {
            Image image;
            try {
                image = new Image(Objects.requireNonNull(
                        ImageGrabber.getInstance().getClass().getResource("possibleMove.jpg")).openStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ImageView possibleMoveImage = new ImageView();
            possibleMoveImage.setImage(image);
            possibleMoveImage.setFitHeight(90);
            possibleMoveImage.setFitWidth(90);
            possibleMoveImage.setX(90 * move.x);
            possibleMoveImage.setY(90 * move.y);
            possibleMoveImage.setOpacity(0.5);
            possibleMoveImage.setOnMouseClicked(event -> {
                movePiece(piece, move);
                removePosibleMoves();
            });
            Constants.possibleMoves.put(move, possibleMoveImage);
            chessBoardPane.getChildren().add(possibleMoveImage);
        }
    }

    /**
     * Add the from to move marker to the board at the given position list
     *
     * @param from The {@link Vector2} move list that we want to put the marker to
     * @param to   The {@link Vector2} position of the piece, so we can assign move listener
     */
    private void displayFromTo(Vector2 from, Vector2 to) {
        Image image;
        try {
            image = new Image(Objects.requireNonNull(
                    ImageGrabber.getInstance().getClass().getResource("move.png")).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageView fromMove = new ImageView();
        fromMove.setImage(image);
        fromMove.setFitHeight(90);
        fromMove.setFitWidth(90);
        fromMove.setX(90 * from.x);
        fromMove.setY(90 * from.y);
        fromMove.setOpacity(0.2);
        Constants.fromToMoves.put(from, fromMove);
        chessBoardPane.getChildren().add(fromMove);
        ImageView toMove = new ImageView();
        toMove.setImage(image);
        toMove.setFitHeight(90);
        toMove.setFitWidth(90);
        toMove.setX(90 * to.x);
        toMove.setY(90 * to.y);
        toMove.setOpacity(0.2);
        Constants.fromToMoves.put(to, toMove);
        chessBoardPane.getChildren().add(toMove);
    }

    // ################
    // # Timer Method #
    // ################

    /**
     * Method to update the timer text and additional time
     *
     * @param color The {@link Piece.Color} of the side where we need to update the text
     */
    private void handleTimerUpdate(Piece.Color color) {
        Constants.whiteSide = color != Piece.Color.WHITE;
        if (RuleSet.getInstance().timeDelta != 0) {
            if (color == Piece.Color.WHITE) Constants.whiteTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
            if (color == Piece.Color.BLACK) Constants.blackTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
        }

        Constants.whiteTimerLabel.setText(TimerInit.formatTime(Constants.whiteTimeInMillis));
        Constants.blackTimerLabel.setText(TimerInit.formatTime(Constants.blackTimeInMillis));
    }

    // ################################
    // # Taken Pieces Display Methods #
    // ################################

    /**
     * Display the captured pieces on the side of the board
     */
    private void displayTaken() {
        int whiteSpacing = Constants.whiteTaken.getChildren().size();
        int blackSpacing = Constants.blackTaken.getChildren().size();
        int whiteTakenWidth = 550;
        int blackTakenWidth = 550;
        if (80 * whiteSpacing > whiteTakenWidth) whiteTakenWidth = 80 * whiteSpacing;
        if (80 * blackSpacing > blackTakenWidth) blackTakenWidth = 80 * blackSpacing;
        Constants.whiteTaken.setPrefWidth(whiteTakenWidth);
        Constants.blackTaken.setPrefWidth(blackTakenWidth);
        whiteTakenScroll.setContent(Constants.whiteTaken);
        blackTakenScroll.setContent(Constants.blackTaken);
    }

    /**
     * Add the {@link Piece} to the display based on color
     *
     * @param piece The {@link Piece} we want to put on the side of the board
     */
    private void addToTaken(Piece piece) {
        String imagePath = "pieces/" +
                General.getPieceColorString(piece) +
                "-" +
                General.getPieceTypeString(piece) +
                ".png";
        Image image;
        try {
            image = new Image(Objects.requireNonNull(
                    ImageGrabber.getInstance().getClass().getResource("" + imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("ERROR in ChessController (addToTaken): Image not found");
            throw new RuntimeException(e);
        }
        ImageView pieceImage = new ImageView();
        pieceImage.setImage(image);
        pieceImage.setFitHeight(80);
        pieceImage.setFitWidth(80);
        int spacign;
        if (piece.color == Piece.Color.WHITE) spacign = Constants.whiteTaken.getChildren().size();
        else spacign = Constants.blackTaken.getChildren().size();
        pieceImage.setX(80 * spacign);
        Constants.takenList.put(piece, pieceImage);
        if (piece.color == Piece.Color.WHITE) Constants.whiteTaken.getChildren().add(pieceImage);
        else Constants.blackTaken.getChildren().add(pieceImage);


    }

    /**
     * Clear the visual array to update it
     */
    private void clearTaken() {
        for (Piece piece : Constants.takenList.keySet()) {
            ImageView imageView = Constants.takenList.get(piece);
            if (piece.color == Piece.Color.WHITE) Constants.whiteTaken.getChildren().remove(imageView);
            else Constants.blackTaken.getChildren().remove(imageView);
        }
        Constants.takenList.clear();
        Constants.whiteTaken = new Pane();
        Constants.blackTaken = new Pane();
        Constants.whiteTaken.setPrefHeight(100);
        Constants.blackTaken.setPrefHeight(100);
        Constants.whiteTaken.setStyle("-fx-background-color: #555555");
        Constants.blackTaken.setStyle("-fx-background-color: #cdcdcd");
    }

    /**
     * Handle the creating and updating of the captured pieces list
     */
    private void handleTakenList() {
        clearTaken();
        for (Piece p : Constants.takenPieces) {
            addToTaken(p);
        }
        displayTaken();
    }

    // #############################
    // # Move List Display Methods #
    // #############################

    /**
     * Generate the material advantage text to put in the move display
     *
     * @param aditional The string builder we add our moves to
     * @param move      The {@link Move} that contains a from to and a color
     */
    private void generateAditional(StringBuilder aditional, Move move) {
        int whitePoints = 0;
        int blackPoints = 0;
        for (Piece p : Constants.takenPieces) {
            if (p.color == Piece.Color.WHITE) blackPoints += PiecePoints.evaluate(p);
            else whitePoints += PiecePoints.evaluate(p);
        }
        if (whitePoints != blackPoints) {
            aditional.append(" with a material ");
            String adv;
            if (whitePoints > blackPoints) {
                adv = move.color == Piece.Color.WHITE
                        ? "advantage of "
                        : "disadvantage of ";
                aditional.append(adv);
                aditional.append(whitePoints - blackPoints);
            } else {
                adv = move.color == Piece.Color.BLACK
                        ? "advantage of "
                        : "disadvantage of ";
                aditional.append(adv);
                aditional.append(blackPoints - whitePoints);
            }
        }
    }

    /**
     * Generate the move string which contains where the user went from and where to
     *
     * @param move The {@link Move} the move we want to create the string for
     * @return A string we can add to the list on the side of the board
     */
    private String generateMoveString(Move move) {
        String color = move.color == Piece.Color.WHITE ? "White" : "Black";
        String fromCoord = General.convertMoveToChessCord(move.from);
        String toCoord = General.convertMoveToChessCord(move.to);
        StringBuilder aditional = new StringBuilder();

        generateAditional(aditional, move);

        return color + " played " + fromCoord + " to " + toCoord + aditional;
    }

    /**
     * Update the list with the new {@link Move}
     *
     * @param moveToAdd The {@link Move} we want to add to the list
     */
    private void updateMoveList(Move moveToAdd) {
        Constants.playedMoves.add(moveToAdd);
        String moveString = generateMoveString(moveToAdd);
        moveListElement.getItems().add(moveString);
        if (Constants.DEVMODE)
            Constants.logger.info("Move String added: " + moveString);
        moveListElement.scrollTo(moveListElement.getItems().size());
    }

    // ####################
    // # Movement Methods #
    // ####################

    /**
     * Swaps the {@link Piece.Color} the alg uses
     */
    private void switchAiMoveColor() {
        if (Constants.AlgColor == Piece.Color.WHITE) Constants.AlgColor = Piece.Color.BLACK;
        else Constants.AlgColor = Piece.Color.WHITE;
    }

    /**
     * Gets the opposite color of the alg
     * @return The playes color
     */
    private Piece.Color playerColor() {
        if (Constants.AlgColor == Piece.Color.WHITE) return Piece.Color.BLACK;
        else return Piece.Color.WHITE;
    }

    /**
     * Text input where the user can type a move
     *
     * @param text The text we get from the input
     */
    private void handleTextMove(String text) {
        // handle resignation and restart
        if (!chessBoardPane.getChildren().contains(Constants.endGameBase)) {
            switch (text) {
                case "resign" -> {
                    // if resign is typed by itself, we resign the player that is about to move
                    if (Constants.whiteToMove) endGame(Piece.Color.BLACK, WinReason.RESIGNITION);
                    else endGame(Piece.Color.WHITE, WinReason.RESIGNITION);
                }
                case "rw", "resign white", "white resign" -> endGame(Piece.Color.BLACK, WinReason.RESIGNITION);
                case "rb", "resign black", "black resign" -> endGame(Piece.Color.WHITE, WinReason.RESIGNITION);
                case "draw" -> endGame(null, WinReason.DRAW);
            }
        }

        if (text.equals("reset") || text.equals("restart") || text.equals("rematch"))
            General.reset(this, engine);

        // handle AI color switch
        if (RuleSet.getInstance().isAiGame) {
            if (text.equals("color swap") || text.equals("color switch") || text.equals("swap color") || text.equals("switch color") ||
                    text.equals("ai switch") || text.equals("switch ai")) {
                switchAiMoveColor();
                General.reset(this, engine);
                if (Constants.AlgColor == Piece.Color.WHITE) {
                    Constants.algMoveThreads = new Thread(() -> {
                        Move computerMove = Constants.alg.move(engine.getBoard(), null);
                        Platform.runLater(() -> movePiece(computerMove.from, computerMove.to));
                    });
                    Constants.algMoveThreads.start();
                }
            }
        }

        Pattern testPattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
        Matcher patternMatcher = testPattern.matcher(text.toLowerCase());
        boolean correctInput = patternMatcher.matches();
        if (correctInput) {
            if (Constants.DEVMODE)
                System.out.println("Pattern FOUND");
            Move move = General.getMoveFromText(text.toLowerCase(), engine);
            if (move.from != null && move.from.x != -1) {
                movePiece(move.from, move.to);
                inputText.clear();
            } else {
                inputText.setText("Invalid Move");
            }

            if (Constants.DEVMODE)
                Constants.logger.info("Text move issued: " + text);
        } else {
            inputText.clear();
            if (Constants.DEVMODE)
                System.out.println("Pattern NOT FOUND");
        }
    }

    /**
     * Handles the piece movement on the board
     * Also handles adding the taken pieces to the list
     * And animates the piece movement
     *
     * @param from The {@link Vector2} from position. The origin of the piece
     * @param to   The {@link Vector2} to position. The Position we want to put the piece to
     */
    public void movePiece(Vector2 from, Vector2 to) {
        removeFromTo();
        Constants.whiteToMove = !Constants.whiteToMove;
        AtomicBoolean moveFinished = new AtomicBoolean(false);
        if (RuleSet.getInstance().gamemode instanceof Fastpaced) {
            Constants.fastPacedCounter = 0;
            removePosibleMoves();
        }
        ImageView pieceView = Constants.pieces.get(from);
        Piece piece = (Piece) engine.getBoard().getElement(from);

        boolean playedTake = false;

        // Handle the capturing of the pieces
        if (engine.getBoard().getElement(to) instanceof Piece) {
            Constants.takenPieces.add((Piece) engine.getBoard().getElement(to));
            handleTakenList();
            remove(to, null);
            AudioPlayer.playCaptureSound();
            playedTake = true;
        }

        if (!playedTake) AudioPlayer.playMoveSound();

        // Update the move list
        Move move = new Move(from, to, piece.color);
        updateMoveList(move);

        // Animate the piece movement with timeline
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        KeyValue kvx = new KeyValue(pieceView.xProperty(), 90 * to.x);
        KeyValue kvy = new KeyValue(pieceView.yProperty(), 90 * to.y);
        // One squeare on the board is 100 ms
        KeyFrame kfx = new KeyFrame(Duration.millis(100 * Math.abs(to.x - from.x)), kvx);
        KeyFrame kfy = new KeyFrame(Duration.millis(100 * Math.abs(to.y - from.y)), kvy);
        timeline.getKeyFrames().add(kfx);
        timeline.getKeyFrames().add(kfy);
        timeline.setOnFinished(event -> moveFinished.set(true));
        timeline.play();

        displayFromTo(from, to);

        // Make sure to correct placement because of inconsistancy
        pieceView.setX(90 * to.x);
        pieceView.setY(90 * to.y);

        Constants.pieces.remove(from);
        Constants.pieces.put(to, pieceView);

        // Call the move on the backend
        engine.move(from, to);
        if (!RuleSet.getInstance().isAiGame || piece.color != playerColor())
            updateClickEventToPiece(to);

        // Change the timer to the other color
        if (RuleSet.getInstance().timer && !(RuleSet.getInstance().gamemode instanceof Fastpaced)) {
            handleTimerUpdate(piece.color);
        }

        if (RuleSet.getInstance().isAiGame && piece.color == playerColor()) {
            AtomicBoolean testing = new AtomicBoolean(true);
            Constants.algMoveThreads = new Thread(() -> {
                while (testing.get()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (moveFinished.get() && Constants.isRunning && !Constants.pauseForPromotion) {
                        testing.set(false);
                        Move computerMove = Constants.alg.move(engine.getBoard(), new Move(from, to));
                        Platform.runLater(() -> movePiece(computerMove.from, computerMove.to));
                    }
                }
            });
            Constants.algMoveThreads.start();
        }
        if (Constants.DEVMODE)
            Constants.logger.info("Piece: " + piece + "\n\t\t Moved from: " + from +
                    " to: " + to);
    }

    /**
     * Remove a piece from the board and potentially add it to the taken pieces
     *
     * @param pieceToRemove The {@link Vector2} position of the piece we want to remove
     * @param taken         Can be null if we don't want the piece added to the taken list. If not null the provided piece will be added to the taken list
     */
    @Override
    public void remove(Vector2 pieceToRemove, Piece taken) {
        ImageView piece = Constants.pieces.get(pieceToRemove);
        chessBoardPane.getChildren().remove(piece);
        Constants.pieces.remove(pieceToRemove);
        if (taken != null) {
            Constants.takenPieces.add(taken);
            handleTakenList();
            if (Constants.DEVMODE)
                Constants.logger.info("Piece added to TAKEN LIST: " + piece.toString());
        }
        if (Constants.DEVMODE)
            Constants.logger.info("Piece removed at: " + pieceToRemove);
    }

    /**
     * Handles the promotion for the chess alg
     * Always chooses queen for simplicity
     * @param to The {@link Vector2} position of the promoting pawn
     */
    public void aiPromote(Vector2 to) {
        Piece p = PROMOTIONPIECES.get(0);
        p.color = Piece.Color.BLACK;
        remove(to, null);
        addPiece(p, to);
        engine.setPiece(to, p);
        Constants.pauseForPromotion = false;
    }

    /**
     * Handle the promotion of the pieces.
     * Called by the backend
     *
     * @param color The {@link Piece.Color} of the {@link Piece} being promoted
     * @param to    The current {@link Vector2} position of the pawn. Also, the spawn point for the new {@link Piece}
     */
    @Override
    public void promote(Piece.Color color, Vector2 to) {
        if (RuleSet.getInstance().isAiGame && color == Piece.Color.BLACK) {
            aiPromote(to);
            return;
        }
        // Prepare the promotion UI base
        Constants.promotionUIBase.setPrefWidth(90);
        Constants.promotionUIBase.setPrefHeight(90 * 4);
        Constants.promotionUIBase.setLayoutX(to.x * 90);
        Constants.promotionUIBase.setStyle("-fx-background-color: #2a2a2a");

        // Flip the position of the UI for when the black pawn promotes
        if (color == Piece.Color.WHITE) {
            Constants.promotionUIBase.setLayoutY(to.y * 90);
        } else {
            Constants.promotionUIBase.setLayoutY((to.y - 3) * 90);
        }

        for (int i = 0; i < 4; i++) {
            Piece p = PROMOTIONPIECES.get(i);
            p.color = color;
            String imagePath = "pieces/" +
                    General.getPieceColorString(p) +
                    "-" +
                    General.getPieceTypeString(p) +
                    ".png";

            Image image = null;
            try {
                image = new Image(Objects.requireNonNull(
                        ImageGrabber.getInstance().getClass().getResource("" + imagePath)).openStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ImageView pieceImage = new ImageView();
            pieceImage.setImage(image);
            pieceImage.setFitHeight(90);
            pieceImage.setFitWidth(90);
            pieceImage.setX(0);
            pieceImage.setY(90 * i);

            // On click, we spawn the correct piece from the list and remove the UI
            pieceImage.setOnMouseClicked(event -> {
                remove(to, null);
                addPiece(Constants.promotionList.get(pieceImage), to);
                chessBoardPane.getChildren().remove(Constants.promotionUIBase);
                Constants.promotionList.clear();
                engine.setPiece(to, p);
                Constants.pauseForPromotion = false;
                Constants.fastPacedCounter = 0;
                if (Constants.DEVMODE)
                    Constants.logger.info("Promotion finished at: " + to + " with piece: " + p);
            });

            Constants.promotionList.put(pieceImage, p);
            Constants.promotionUIBase.getChildren().add(pieceImage);
        }
        chessBoardPane.getChildren().add(Constants.promotionUIBase);
        if (Constants.DEVMODE)
            Constants.logger.info("Promotion dialog prompted at: " + to);
    }

    // ###################
    // # End Game Method #
    // ###################

    /**
     * The end game is called when we want to finist the current game.
     * Pauses the timer and puts an overlay on the board with the ending condition
     *
     * @param won    The {@link Piece.Color} of the winning side
     * @param reason The {@link WinReason} we ended the game
     */
    @Override
    public void endGame(Piece.Color won, WinReason reason) {
        Constants.endGameBase = new StackPane();
        Constants.isRunning = false;
        Constants.endGameBase.setPrefHeight(90 * 8);
        Constants.endGameBase.setPrefWidth(90 * 8);
        Constants.endGameBase.setLayoutX(0);
        Constants.endGameBase.setLayoutY(0);
        Constants.endGameBase.setStyle("-fx-background-color: #2a2a2a");

        Label winLabel = new Label();
        Label winDescriptionLabel = new Label();

        if (won != null) {
            winLabel.setText("The winner is " + (won == Piece.Color.WHITE ? "WHITE" : "BLACK"));
        } else {
            winLabel.setText("DRAW");
        }

        switch (reason) {
            case TIMEOUT -> winDescriptionLabel.setText("Won by timeout");
            case CHECKMATE -> winDescriptionLabel.setText("Won by checkmate");
            case STALEMATE -> winDescriptionLabel.setText("Drawn by stalemate");
            case NOMATERIAL -> winDescriptionLabel.setText("Draw by lack of material");
            case RESIGNITION -> winDescriptionLabel.setText("Won by resignation");
            case DRAW -> winDescriptionLabel.setText("Draw by agreement");
        }

        winLabel.setFont(new Font("Ariel", 70));
        winDescriptionLabel.setFont(new Font("Ariel", 40));
        winLabel.setTextFill(Color.WHITE);
        winDescriptionLabel.setTextFill(Color.WHITE);

        Button backToMenu = new Button();
        backToMenu.setText("Back to menu");
        backToMenu.setFont(new Font("Ariel", 30));
        backToMenu.setTextFill(Color.WHITE);
        backToMenu.setStyle("-fx-background-color: #2a2a2a");

        Button rematch = new Button();
        rematch.setText("Rematch");
        rematch.setFont(new Font("Ariel", 30));
        rematch.setTextFill(Color.WHITE);
        rematch.setStyle("-fx-background-color: #2a2a2a");

        rematch.setOnMousePressed(e -> General.reset(this, engine));

        backToMenu.setOnMousePressed(e -> General.changeScene((Stage) chessBoardPane.getScene().getWindow()));

        VBox labelHolder = new VBox();
        labelHolder.getChildren().addAll(winLabel, winDescriptionLabel, backToMenu, rematch);
        labelHolder.setSpacing(10);
        labelHolder.setAlignment(Pos.CENTER);

        Constants.endGameBase.getChildren().add(labelHolder);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> chessBoardPane.getChildren().add(Constants.endGameBase));
        });

        thread.start();
        if (Constants.DEVMODE)
            Constants.logger.info("Game ended with reason: " + winDescriptionLabel + " the winner is: " + winLabel);
    }

    /**
     * Used to set the check marker on the kings current position
     *
     * @param checkCoord The {@link Vector2} position of the king
     * @deprecated Because we currently don't use this method
     */
    @Override
    public void setCheck(Vector2 checkCoord) {

    }
}
