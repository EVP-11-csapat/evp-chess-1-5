package chess15.gui.controllers;

import chess15.*;
import chess15.algorithm.AlgorithmInterface;
import chess15.algorithm.ChessAlgorithm;
import chess15.engine.Engine;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gamemode.Fastpaced;
import chess15.gui.interfaces.UIInteface;
import chess15.gui.util.Constants;
import chess15.gui.util.General;
import chess15.gui.util.TimerInit;
import chess15.util.CustomFormatter;
import chess15.util.Move;
import chess15.util.PiecePoints;
import chess15.util.WinReason;
import javafx.animation.*;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The ChessController is responsible for everything UI related in the main scene
 */
public class ChessController implements UIInteface {
    // A list of pieces we can use to promote pawns
    private final ArrayList<Piece> PROMOTIONPIECES = new ArrayList<>(List.of(
            new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false)
    ));
    private EngineInterface engine;
    private AlgorithmInterface alg;
    @FXML
    public Pane chessBoardPane;
    @FXML
    private Pane main;
    @FXML
    public ListView<String> moveListElement;
    @FXML
    private ScrollPane blackTakenScroll;
    @FXML
    private ScrollPane whiteTakenScroll;
    @FXML
    private TextField inputText;
    @FXML
    public Pane clockPane;

    /**
     * Initialize is called when the scene is loading
     * Handles every setup action
     */
    public void initialize() {
        engine = new Engine(RuleSet.getInstance(), this);
        if (RuleSet.getInstance().isAiGame) alg = new ChessAlgorithm(RuleSet.getInstance(), Piece.Color.BLACK);
        Constants.board = engine.getBoard();
        setUpBoard();

        // Development command to reset the board
        EventHandler<KeyEvent> resetHandler = e -> {
            General.guiReset(this, e, engine);
        };

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
            main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
            ((Stage) main.getScene().getWindow()).setOnCloseRequest(e -> {
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
                    main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
                    ((Stage) main.getScene().getWindow()).setOnCloseRequest(e -> {
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
        inputText.setOnAction(e -> {
            handleTextMove(inputText.getCharacters().toString());
        });

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
    }

    // Board setup methods

    /**
     * Method used to set up the board and the pieces
     * For every pieve in the stored board creates an ImageView
     */
    public void setUpBoard() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardElement element = Constants.board.elements[j][i];
                Piece piece = null;
                if (!(element instanceof Piece))  continue;
                piece = (Piece) element;
                Vector2 piecePos = new Vector2(j, i);
                addPiece(piece, piecePos);
            }
        }
        Constants.isRunning = true;
    }

    /**
     * Method used to create the ImageView and Position it on the board
     * @param piece The {@link Piece} object that we want to create the {@link ImageView} for
     * @param pos The {@link Vector2} used for positioning the {@link Piece}
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
        Image image = null;
        try {
            image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("Image not found");
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
        addClickEventToPiece(pieceImage);
        // Adds the piece to the board as an image
        chessBoardPane.getChildren().add(pieceImage);

        if (Constants.DEVMODE)
            Constants.logger.info("Piece added: " + piece.toString() + " at: " + pos.toString());
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
     * @param map The {@link Map} that contains the key value pairs
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

    // Handle the click event on the piece
    EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Vector2 handlePos = getKeysByValue(Constants.pieces, (ImageView) mouseEvent.getSource()).get(0);
            handlePieceClick(handlePos);
        }
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
     * @param piece The {@link ImageView} to put the listener on to
     */
    private void addClickEventToPiece(ImageView piece) {
        piece.setOnMousePressed(pressedHandler);
    }

    // Possible Move Display Methods
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

    /**
     * Add the possible move marker to the board at the given position list
     * @param moves The {@link Vector2} move list that we want to put the marker to
     * @param piece The {@link Vector2} position of the piece, so we can assign move listener
     */
    private void displayPosibleMoves(ArrayList<Vector2> moves, Vector2 piece) {
        for (Vector2 move : moves) {
            Image image = null;
            try {
                image = new Image(Objects.requireNonNull(getClass().getResource("../images/possibleMove.jpg")).openStream());
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

    // Timer Method

    /**
     * Method to update the timer text and additional time
     * @param color The {@link chess15.Piece.Color} of the side where we need to update the text
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

    // Taken Pieces Display Methods
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
     * @param piece The {@link Piece} we want to put on the side of the board
     */
    private void addToTaken(Piece piece) {
        String imagePath = "pieces/" +
                General.getPieceColorString(piece) +
                "-" +
                General.getPieceTypeString(piece) +
                ".png";
        Image image = null;
        try {
            image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("Image not found");
            throw new RuntimeException(e);
        }
        ImageView pieceImage = new ImageView();
        pieceImage.setImage(image);
        pieceImage.setFitHeight(80);
        pieceImage.setFitWidth(80);
        int spacign = 0;
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

    // Move List Display Methods

    /**
     * Generate the material advantage text to put in the move display
     * @param aditional The string builder we add our moves to
     * @param move The {@link Move} that contains a from to and a color
     */
    private void generateAditional(StringBuilder aditional, Move move) {
        int whitePoints = 0;
        int blackPoints = 0;
        for (Piece p : Constants.takenPieces) {
            if (p.color == Piece.Color.WHITE) blackPoints += PiecePoints.evaluate(p);
            else whitePoints += PiecePoints.evaluate(p);
        }
        if (whitePoints == blackPoints) {
            return;
        } else if (whitePoints > blackPoints) {
            aditional.append(" with a material ");
            String adv = move.color == Piece.Color.WHITE
                    ? "advantage of "
                    : "disadvantage of ";
            aditional.append(adv);
            aditional.append(whitePoints - blackPoints);
        } else {
            aditional.append(" with a material ");
            String adv = move.color == Piece.Color.BLACK
                    ? "advantage of "
                    : "disadvantage of ";
            aditional.append(adv);
            aditional.append(blackPoints - whitePoints);
        }
    }

    /**
     * Generate the move string which contains where the user went from and where to
     * @param move The {@link Move} the move we want to create the string for
     * @return A string we can add to the list on the side of the board
     */
    private String generateMoveString(Move move) {
        String color = move.color == Piece.Color.WHITE ? "White" : "Black";
        String fromCoord = General.convertMoveToChessCord(move.from);
        String toCoord = General.convertMoveToChessCord(move.to);
        StringBuilder aditional = new StringBuilder();

        generateAditional(aditional, move);

        return color + " played " + fromCoord + " to " + toCoord + aditional.toString();
    }

    /**
     * Update the list with the new {@link Move}
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

    // Movement Methods
    /**
     * Text input where the user can type a move
     * @param text The text we get from the input
     */
    private void handleTextMove(String text) {
        Pattern testPattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
        Matcher patternMatcher = testPattern.matcher(text.toLowerCase());
        boolean correctInput = patternMatcher.matches();
        if (correctInput) {
            System.out.println("Pattern FOUND");
            System.out.println(text);
            //TODO: Parse to move and call move method
            if (Constants.DEVMODE)
                Constants.logger.info("Text move issued: " + text);
            inputText.clear();
        } else {
            System.out.println("Pattern NOT FOUND");
        }
    }

    /**
     * Handles the piece movement on the board
     * Also handles adding the taken pieces to the list
     * And animates the piece movement
     * @param from The {@link Vector2} from position. The origin of the piece
     * @param to The {@link Vector2} to position. The Position we want to put the piece to
     */
    public void movePiece(Vector2 from, Vector2 to) {
        if (RuleSet.getInstance().gamemode instanceof Fastpaced) {
            Constants.fastPacedCounter = 0;
            removePosibleMoves();
        }
        ImageView pieceView = Constants.pieces.get(from);
        Piece piece = (Piece) engine.getBoard().getElement(from);

        // Handle the capturing of the pieces
        if (engine.getBoard().getElement(to) instanceof Piece) {
            Constants.takenPieces.add((Piece) engine.getBoard().getElement(to));
            handleTakenList();
            remove(to, null);
        }

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
        timeline.play();

        // Make sure to correct placement because of inconsistancy
        pieceView.setX(90 * to.x);
        pieceView.setY(90 * to.y);

        Constants.pieces.remove(from);
        Constants.pieces.put(to, pieceView);

        // Call the move on the backend
        engine.move(from, to);
        updateClickEventToPiece(to);

        // Change the timer to the other color
        if (RuleSet.getInstance().timer && !(RuleSet.getInstance().gamemode instanceof Fastpaced)) {
            handleTimerUpdate(piece.color);
        }

        if (RuleSet.getInstance().isAiGame && piece.color == Piece.Color.WHITE) {
            Vector2[] fromtopair = alg.move(engine.getBoard());
            movePiece(fromtopair[0], fromtopair[1]);
        }
        if (Constants.DEVMODE)
            Constants.logger.info("Piece: " + piece.toString() + "\n\t\t Moved from: " + from.toString() +
                    " to: " + to.toString());
    }

    /**
     * Remove a piece from the board and potentially add it to the taken pieces
     * @param pieceToRemove The {@link Vector2} position of the piece we want to remove
     * @param taken Can be null if we don't want the piece added to the taken list. If not null the provided piece will be added to the taken list
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
     * Handle the promotion of the pieces.
     * Called by the backend
     * @param from The previous {@link Vector2} position of the pawn, so we can get the piece from the board due to syncronisation problems
     * @param to The current {@link Vector2} position of the pawn. Also, the spawn point for the new {@link Piece}
     */
    @Override
    public void promote(Vector2 from, Vector2 to) {
        Constants.pausedForPromotion = true;
        // Prepare the promotion UI base
        Constants.promotionUIBase.setPrefWidth(90);
        Constants.promotionUIBase.setPrefHeight(90 * 4);
        Constants.promotionUIBase.setLayoutX(to.x * 90);
        Constants.promotionUIBase.setStyle("-fx-background-color: #2a2a2a");
        Piece.Color pieceColor = ((Piece) engine.getBoard().at(from)).color;

        // Flip the position of the UI for when the black pawn promotes
        if (pieceColor == Piece.Color.WHITE) {
            Constants.promotionUIBase.setLayoutY(to.y * 90);
        } else {
            Constants.promotionUIBase.setLayoutY((to.y - 3) * 90);
        }

        for (int i = 0; i < 4; i++) {
            Piece p = PROMOTIONPIECES.get(i);
            p.color = pieceColor;
            String imagePath = "pieces/" +
                    General.getPieceColorString(p) +
                    "-" +
                    General.getPieceTypeString(p) +
                    ".png";

            Image image = null;
            try {
                image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath)).openStream());
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
                Constants.pausedForPromotion = false;
                Constants.fastPacedCounter = 0;
                if (Constants.DEVMODE)
                    Constants.logger.info("Promotion finished at: " + to.toString() + " with piece: " + p.toString());
            });

            Constants.promotionList.put(pieceImage, p);
            Constants.promotionUIBase.getChildren().add(pieceImage);
        }
        chessBoardPane.getChildren().add(Constants.promotionUIBase);
        if (Constants.DEVMODE)
            Constants.logger.info("Promotion dialog prompted at: " + to.toString());
    }

    // End Game Method

    /**
     * The end game is called when we want to finist the current game.
     * Pauses the timer and puts an overlay on the board with the ending condition
     * @param won The {@link chess15.Piece.Color} of the winning side
     * @param reason The {@link WinReason} we ended the game
     */
    @Override
    public void endGame(Piece.Color won, WinReason reason) {
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
        }

        winLabel.setFont(new Font("Ariel", 70));
        winDescriptionLabel.setFont(new Font("Ariel", 40));
        winLabel.setTextFill(Color.WHITE);
        winDescriptionLabel.setTextFill(Color.WHITE);

        VBox labelHolder = new VBox();
        labelHolder.getChildren().addAll(winLabel, winDescriptionLabel);
        labelHolder.setSpacing(10);
        labelHolder.setAlignment(Pos.CENTER);

        Constants.endGameBase.getChildren().add(labelHolder);
        chessBoardPane.getChildren().add(Constants.endGameBase);
        if (Constants.DEVMODE)
            Constants.logger.info("Game ended with reason: " + winDescriptionLabel + " the winner is: " + winLabel);
    }

    /**
     * Used to set the check marker on the kings current position
     * @deprecated Because we currently don't use this method
     * @param checkCoord The {@link Vector2} position of the king
     */
    @Override
    public void setCheck(Vector2 checkCoord) {

    }
}
