package chess15.gui.controllers;

import chess15.*;
import chess15.engine.Engine;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gui.interfaces.UIInteface;
import chess15.util.Move;
import chess15.util.PiecePoints;
import chess15.util.WinReason;
import com.sun.source.tree.WhileLoopTree;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import org.junit.Rule;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChessController implements UIInteface {
    StackPane endGameBase = new StackPane();
    Pane promotionUIBase = new Pane();
    private final ArrayList<Piece> PROMOTIONPIECES = new ArrayList<>(List.of(
            new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false)
    ));
    private HashMap<ImageView, Piece> promotionList = new HashMap<>();
    private Boolean aditionalString = true;
    private HashMap<Vector2, ImageView> pieces = new HashMap<>();
    private HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();
    private ArrayList<Piece> takenPieces = new ArrayList<>();
    private HashMap<Piece, ImageView> takenList = new HashMap<>();
    public ArrayList<Move> playedMoves = new ArrayList<>();
    private Board board = null;
    private EngineInterface engine;

    private Label whiteTimerLabel;
    private Label blackTimerLabel;

    private Vector2 handlePos;

    Scene scene;

    @FXML
    private ImageView chessBoard;

    @FXML
    private Pane chessBoardPane;

    @FXML
    private StackPane chessBoardStackPane;

    @FXML
    private Pane main;

    @FXML
    private ListView moveListElement;

    @FXML
    private ScrollPane blackTakenScroll;

    @FXML
    private ScrollPane whiteTakenScroll;

    @FXML
    private TextField inputText;

    @FXML
    private Pane clockPane;

    private Pane whiteTaken;
    private Pane blackTaken;

    private HBox timerHBox;

    private Pane whiteTimerBox;
    private Pane blackTimerBox;

    private long whiteTimeInMillis;
    private long blackTimeInMillis;
    private boolean isRunning;
    private boolean whiteSide;
    private Thread timerThread;

    private boolean whiteTimeRanOut = false;
    private boolean blackTimeRanOut = false;

    public void initialize() throws IOException {
        engine = new Engine(RuleSet.getInstance(), this);
        System.out.println("Chess controller initialized");
        System.out.println(RuleSet.getInstance().toString());
        board = engine.getBoard();
        setUpBoard();
        EventHandler<KeyEvent> resetHandler = e -> {
            if (e.getCode() == KeyCode.R && e.isControlDown() && e.isAltDown()) {
                engine.reset();
                board = engine.getBoard();
                removePosibleMoves();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        removePiece(new Vector2(j, i), null);
                    }
                }
                moveListElement.getItems().clear();

                if (whiteTaken != null) whiteTaken.getChildren().clear();
                if (blackTaken != null) blackTaken.getChildren().clear();

                pieces.clear();
                takenPieces = new ArrayList<>();
                takenList = new HashMap<>();
                if (RuleSet.getInstance().timer) {
                    whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
                    blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
                    whiteTimerLabel.setText(formatTime(whiteTimeInMillis));
                    blackTimerLabel.setText(formatTime(blackTimeInMillis));
                }
                chessBoardPane.getChildren().remove(promotionUIBase);
                promotionList.clear();
                chessBoardPane.getChildren().remove(endGameBase);
                endGameBase = new StackPane();
                setUpBoard();
            }
        };

        EventHandler<KeyEvent> checkmateHandler = e -> {
            if (!chessBoardPane.getChildren().contains(endGameBase)) {
                if (e.getCode() == KeyCode.C && e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.WHITE, WinReason.CHECKMATE);
                } else if (e.getCode() == KeyCode.C && !e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.BLACK, WinReason.CHECKMATE);
                }
            }
        };

        EventHandler<KeyEvent> timeoutHandler = e -> {
            if (!chessBoardPane.getChildren().contains(endGameBase)) {
                if (e.getCode() == KeyCode.T && e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.WHITE, WinReason.TIMEOUT);
                } else if (e.getCode() == KeyCode.T && !e.isControlDown() && e.isAltDown()) {
                    endGame(Piece.Color.BLACK, WinReason.TIMEOUT);
                }
            }
        };

        if (main.getScene() != null) {
            main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
            ((Stage) main.getScene().getWindow()).setOnCloseRequest(e -> {
                if (timerThread != null) {
                    timerThread.interrupt();
                    timerThread.stop();
                }
            });
            main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, checkmateHandler);
            main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, timeoutHandler);
        } else {
            main.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
                    ((Stage) main.getScene().getWindow()).setOnCloseRequest(e -> {
                        if (timerThread != null) {
                            timerThread.interrupt();
                            timerThread.stop();
                        }
                    });
                    main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, checkmateHandler);
                    main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, timeoutHandler);
                }
            });
        }

        inputText.setOnAction(e -> {
            handleTextMove(inputText.getCharacters().toString());
        });

        if (RuleSet.getInstance().timer) {
            whiteTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;
            blackTimeInMillis = (long) RuleSet.getInstance().startTime * 60 * 1000;

            // Set up Timer Environment
            timerHBox = new HBox();
            timerHBox.setAlignment(Pos.CENTER);
            whiteTimerBox = new Pane();
            blackTimerBox = new Pane();
            whiteTimerBox.setStyle("-fx-background-color: #cdcdcd");
            blackTimerBox.setStyle("-fx-background-color: #cdcdcd");
            whiteTimerBox.setPrefWidth(250);
            whiteTimerBox.setPrefHeight(200);
            blackTimerBox.setPrefWidth(250);
            blackTimerBox.setPrefHeight(200);
            Separator sep = new Separator();
            sep.setOrientation(Orientation.VERTICAL);
            sep.setPrefWidth(47);
            sep.setOpacity(0);
            whiteTimerLabel = new Label();
            whiteTimerLabel.setText(formatTime(whiteTimeInMillis));
            blackTimerLabel = new Label();
            blackTimerLabel.setText(formatTime(blackTimeInMillis));

            whiteTimerLabel.setFont(new Font("Arial", 100));
            blackTimerLabel.setFont(new Font("Arial", 100));

            whiteTimerLabel.layoutXProperty().bind(whiteTimerBox.widthProperty().subtract(whiteTimerLabel.widthProperty()).divide(2));
            whiteTimerLabel.layoutYProperty().bind(whiteTimerBox.heightProperty().subtract(whiteTimerLabel.heightProperty()).divide(2));

            blackTimerLabel.layoutXProperty().bind(blackTimerBox.widthProperty().subtract(blackTimerLabel.widthProperty()).divide(2));
            blackTimerLabel.layoutYProperty().bind(blackTimerBox.heightProperty().subtract(blackTimerLabel.heightProperty()).divide(2));

            whiteTimerBox.getChildren().add(whiteTimerLabel);
            blackTimerBox.getChildren().add(blackTimerLabel);
            timerHBox.getChildren().addAll(whiteTimerBox, sep, blackTimerBox);
            clockPane.getChildren().add(timerHBox);

            // Set up timer
            isRunning = true;
            whiteSide = true;
            timerThread = new Thread(() -> {
                while (isRunning && !Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (whiteSide) {
                        whiteTimeInMillis -= 10;
                        if (whiteTimeInMillis <= 0) {
                            isRunning = false;
                            whiteTimeRanOut = true;
                            Platform.runLater(() -> endGame(Piece.Color.BLACK, WinReason.TIMEOUT));
                        }

                        Platform.runLater(() -> whiteTimerLabel.setText(formatTime(whiteTimeInMillis)));
                    } else {
                        blackTimeInMillis -= 10;
                        if (blackTimeInMillis <= 0) {
                            isRunning = false;
                            blackTimeRanOut = true;
                            Platform.runLater(() -> endGame(Piece.Color.WHITE, WinReason.TIMEOUT));
                        }

                        Platform.runLater(() -> blackTimerLabel.setText(formatTime(blackTimeInMillis)));
                    }
                }
            });

            timerThread.start();
        }
    }

    private String formatTime(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) ((timeInMillis / 1000) % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    private void handleTextMove(String text) {
        System.out.println(text);
        Pattern testPattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
        Matcher patternMatcher = testPattern.matcher(text.toLowerCase());
        Boolean correctInput = patternMatcher.matches();
        if (correctInput) {
            System.out.println("Pattern FOUND");
            System.out.println(text);
            //TODO: Parse to move and call move method
            inputText.clear();
        } else {
            System.out.println("Pattern NOT FOUND");
        }
    }

    private void setUpBoard() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardElement element = board.elements[j][i];
                Piece piece = null;
                if (!(element instanceof Piece))  continue;
                piece = (Piece) element;
                Vector2 piecePos = new Vector2(j, i);

                try {
                    createPiece(piece, piecePos);
                } catch (IOException e) {
                    System.out.println(e.toString());
                    System.out.println("Image NOT Found");
                }
            }
        }
    }

    private String getPieceColorString(Piece p) {
        if (p.color == Piece.Color.WHITE) return "white";
        else return "black";
    }

    private String getPieceColorString(Piece.Color color) {
        if (color == Piece.Color.WHITE) return "white";
        else return "black";
    }

    private String getPieceTypeString(Piece p) {
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

    private void createPiece(Piece p, Vector2 pos) throws IOException {
        System.out.println("Creating piece");

        String imagePath = "pieces/" +
                getPieceColorString(p) +
                "-" +
                getPieceTypeString(p) +
                ".png";

        Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath)).openStream());
        ImageView pieceImage = new ImageView();
        pieceImage.setImage(image);
        pieceImage.setFitHeight(90);
        pieceImage.setFitWidth(90);
        pieceImage.setX(90 * pos.x);
        pieceImage.setY(90 * pos.y);
        pieces.put(pos, pieceImage);
        addClickEventToPiece(pos, pieceImage);
        chessBoardPane.getChildren().add(pieceImage);
    }

    private void handlePieceClick(Vector2 pos, MouseEvent event) throws IOException {
        System.out.println("Clicked on piece at " + pos + " with event " + event);
        removePosibleMoves();
        displayPosibleMoves(engine.getMoves(pos), pos);
    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            try {
                System.out.println(mouseEvent.getSource());
                Vector2 handlePos = getKeysByValue(pieces, (ImageView) mouseEvent.getSource()).get(0);
                handlePieceClick(handlePos, mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private void updateClickEventToPiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        piece.setOnMousePressed(pressedHandler);
    }

    private void addClickEventToPiece(Vector2 pos, ImageView piece) {
        System.out.println("Creating click listener for: " + pos );
        piece.setOnMousePressed(pressedHandler);
    }

    public String convertMoveToChessCord(Vector2 move) {
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

    private void generateAditional(StringBuilder aditional, Move move) {
        int whitePoints = 0;
        int blackPoints = 0;
        for (Piece p : takenPieces) {
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
        } else if (whitePoints < blackPoints) {
            aditional.append(" with a material ");
            String adv = move.color == Piece.Color.BLACK
                    ? "advantage of "
                    : "disadvantage of ";
            aditional.append(adv);
            aditional.append(blackPoints - whitePoints);
        }
    }

    private String generateMoveString(Move move) {
        String color = move.color == Piece.Color.WHITE ? "White" : "Black";
        String fromCoord = convertMoveToChessCord(move.from);
        String toCoord = convertMoveToChessCord(move.to);
        if (aditionalString) {
            StringBuilder aditional = new StringBuilder();

            generateAditional(aditional, move);

            return color + " played " + fromCoord + " to " + toCoord + aditional.toString();
        } else {
            return color + " played " + fromCoord + " to " + toCoord;
        }
    }

    /**
     * @deprecated
     * @param moveToRemove
     * @deprecated
     */
    private void removeElementFromList(Move moveToRemove) {
        moveListElement.getItems().remove(generateMoveString(moveToRemove));
    }

    private void updateMoveList(Move moveToAdd) {
        playedMoves.add(moveToAdd);
        moveListElement.getItems().add(generateMoveString(moveToAdd));
        moveListElement.scrollTo(moveListElement.getItems().size());
        System.out.println("PLAYED: " + generateMoveString(moveToAdd));
    }

    private void displayTaken() {
        int whiteSpacing = whiteTaken.getChildren().size();
        int blackSpacing = blackTaken.getChildren().size();
        int whiteTakenWidth = 550;
        int blackTakenWidth = 550;
        if (80 * whiteSpacing > whiteTakenWidth) whiteTakenWidth = 80 * whiteSpacing;
        if (80 * blackSpacing > blackTakenWidth) blackTakenWidth = 80 * blackSpacing;
        whiteTaken.setPrefWidth(whiteTakenWidth);
        blackTaken.setPrefWidth(blackTakenWidth);
        whiteTakenScroll.setContent(whiteTaken);
        blackTakenScroll.setContent(blackTaken);
    }

    private void addToTaken(Piece piece) throws IOException {
        String imagePath = "pieces/" +
                getPieceColorString(piece) +
                "-" +
                getPieceTypeString(piece) +
                ".png";
        Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath)).openStream());
        ImageView pieceImage = new ImageView();
        pieceImage.setImage(image);
        pieceImage.setFitHeight(80);
        pieceImage.setFitWidth(80);
        int spacign = 0;
        if (piece.color == Piece.Color.WHITE) spacign = whiteTaken.getChildren().size();
        else spacign = blackTaken.getChildren().size();
        pieceImage.setX(80 * spacign);
        takenList.put(piece, pieceImage);
        if (piece.color == Piece.Color.WHITE) whiteTaken.getChildren().add(pieceImage);
        else blackTaken.getChildren().add(pieceImage);
    }

    private void clearTaken() {
        for (Piece piece : takenList.keySet()) {
            ImageView imageView = takenList.get(piece);
            if (piece.color == Piece.Color.WHITE) whiteTaken.getChildren().remove(imageView);
            else blackTaken.getChildren().remove(imageView);
        }
        takenList.clear();
        whiteTaken = new Pane();
        blackTaken = new Pane();
        whiteTaken.setPrefHeight(100);
        blackTaken.setPrefHeight(100);
        whiteTaken.setStyle("-fx-background-color: #555555");
        blackTaken.setStyle("-fx-background-color: #cdcdcd");
    }

    private void handleTakenList() {
        clearTaken();
        for (Piece p : takenPieces) {
            try {
                addToTaken(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        displayTaken();
    }

    private void movePiece(Vector2 from, Vector2 to) {
        ImageView pieceView = pieces.get(from);
        Piece piece = (Piece) engine.getBoard().getElement(from);
        System.out.println(takenPieces);
        if (engine.getBoard().getElement(to) instanceof Piece) {
            takenPieces.add((Piece) engine.getBoard().getElement(to));
            handleTakenList();
            removePiece(to, null);
            System.out.println(takenPieces);
        }
        Move move = new Move(from, to, piece.color);
        updateMoveList(move);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        KeyValue kvx = new KeyValue(pieceView.xProperty(), 90 * to.x);
        KeyValue kvy = new KeyValue(pieceView.yProperty(), 90 * to.y);
        KeyFrame kfx = new KeyFrame(Duration.millis(100 * Math.abs(to.x - from.x)), kvx);
        KeyFrame kfy = new KeyFrame(Duration.millis(100 * Math.abs(to.y - from.y)), kvy);
        timeline.getKeyFrames().add(kfx);
        timeline.getKeyFrames().add(kfy);
        timeline.play();
        pieceView.setX(90 * to.x);
        pieceView.setY(90 * to.y);
        pieces.remove(from);
        pieces.put(to, pieceView);
        engine.move(from, to);
        updateClickEventToPiece(to);
        if (RuleSet.getInstance().timer) {
            handleTimerUpdate(piece.color);
        }
    }

    private void handleTimerUpdate(Piece.Color color) {
        if (color == Piece.Color.WHITE) whiteSide = false;
        else whiteSide = true;
        if (RuleSet.getInstance().timeDelta != 0) {
            if (color == Piece.Color.WHITE) whiteTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
            if (color == Piece.Color.BLACK) blackTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
        }

        whiteTimerLabel.setText(formatTime(whiteTimeInMillis));
        blackTimerLabel.setText(formatTime(blackTimeInMillis));
    }

    private void removePiece(Vector2 pos, Piece taken) {
        ImageView piece = pieces.get(pos);
        chessBoardPane.getChildren().remove(piece);
        pieces.remove(pos);
        if (taken != null) {
            takenPieces.add(taken);
            handleTakenList();
        }
    }

    private void displayPosibleMoves(ArrayList<Vector2> moves, Vector2 piece) throws IOException {
        System.out.println("Displaying posible moves");
        for (Vector2 move : moves) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/possibleMove.jpg")).openStream());
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
            possibleMoves.put(move, possibleMoveImage);
            chessBoardPane.getChildren().add(possibleMoveImage);
        }
    }

    private void removePosibleMoves() {
        for (Vector2 move : possibleMoves.keySet()) {
            ImageView imageView = possibleMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        possibleMoves.clear();
    }

    @Override
    public void endGame(Piece.Color won, WinReason reason) {
        endGameBase.setPrefHeight(90 * 8);
        endGameBase.setPrefWidth(90 * 8);
        endGameBase.setLayoutX(0);
        endGameBase.setLayoutY(0);
        endGameBase.setStyle("-fx-background-color: #2a2a2a");
        Label winLabel = new Label();
        Label winDescriptionLabel = new Label();
        winLabel.setText("The winner is " + (won == Piece.Color.WHITE ? "WHITE" : "BLACK"));
        switch (reason) {
            case TIMEOUT -> winDescriptionLabel.setText("Won by timeout");
            case CHECKMATE -> winDescriptionLabel.setText("Won by checkmate");
        }

        winLabel.setFont(new Font("Ariel", 70));
        winDescriptionLabel.setFont(new Font("Ariel", 40));
        winLabel.setTextFill(Color.WHITE);
        winDescriptionLabel.setTextFill(Color.WHITE);

        VBox labelHolder = new VBox();
        labelHolder.getChildren().addAll(winLabel, winDescriptionLabel);
        labelHolder.setSpacing(10);
        labelHolder.setAlignment(Pos.CENTER);

        endGameBase.getChildren().add(labelHolder);
        chessBoardPane.getChildren().add(endGameBase);
    }

    @Override
    public void setCheck(Vector2 checkCoord) {

    }

    @Override
    public void remove(Vector2 pieceToRemove, Piece taken) {
        removePiece(pieceToRemove, taken);
    }

    @Override
    public void promote(Vector2 pos) {
        promotionUIBase.setPrefWidth(90);
        promotionUIBase.setPrefHeight(90 * 4);
        promotionUIBase.setLayoutX(pos.x * 90);
        promotionUIBase.setLayoutY(pos.y * 90);
        promotionUIBase.setStyle("-fx-background-color: #2a2a2a");
        Piece.Color pieceColor = ((Piece) engine.getBoard().at(from)).color;
        if (pieceColor == Piece.Color.WHITE) {
            promotionUIBase.setLayoutY(to.y * 90);
        } else {
            promotionUIBase.setLayoutY((to.y - 3) * 90);
        }
        for (int i = 0; i < 4; i++) {
            Piece p = PROMOTIONPIECES.get(i);
            p.color = pieceColor;
            String imagePath = "pieces/" +
                    getPieceColorString(p) +
                    "-" +
                    getPieceTypeString(p) +
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
            pieceImage.setOnMouseClicked(event -> {
                remove(pos, null);
                addPiece(promotionList.get(pieceImage), pos);
                chessBoardPane.getChildren().remove(promotionUIBase);
                promotionList.clear();
                engine.setPiece(pos, p);
            });
            promotionList.put(pieceImage, p);
            promotionUIBase.getChildren().add(pieceImage);
        }
        chessBoardPane.getChildren().add(promotionUIBase);
    }

    @Override
    public void addPiece(Piece piece, Vector2 pos) {
        try {
            createPiece(piece, pos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
