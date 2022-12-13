package chess15.gui.controllers;

import chess15.*;
import chess15.engine.Engine;
import chess15.engine.EngineInterface;
import chess15.engine.RuleSet;
import chess15.gui.interfaces.UIInteface;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//import java.awt.*;

public class ChessController implements UIInteface {
    private HashMap<Vector2, ImageView> pieces = new HashMap<>();
    private HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();
    private Board board = null;
    private EngineInterface engine;

    private Vector2 handlePos;

    Scene scene;

    @FXML
    private ImageView chessBoard;

    @FXML
    private Pane chessBoardPane;

    @FXML
    private StackPane chessBoardStackPane;

    @FXML
    Pane main;

    public void initialize() throws IOException {
        engine = new Engine(RuleSet.getInstance(), this);
        System.out.println("Chess controller initialized");
        System.out.println(RuleSet.getInstance().toString());
        board = engine.getBoard();
        setUpBoard();
        EventHandler<KeyEvent> resetHandler = e -> {
            if (e.getCode() == KeyCode.R && e.isControlDown() && e.isAltDown()) {
                engine.reset();
                removePosibleMoves();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        removePiece(new Vector2(j, i));
                    }
                }
                pieces.clear();
                setUpBoard();
            }
        };

        if (main.getScene() != null) {
            main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
        } else {
            main.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, resetHandler);
                }
            });
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
        StringBuilder imagePath = new StringBuilder();

        imagePath.append("pieces/");
        imagePath.append(getPieceColorString(p));
        imagePath.append("-");
        imagePath.append(getPieceTypeString(p));
        imagePath.append(".png");
//        imagePath.append("test.png");

        Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/" + imagePath.toString())).openStream());
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

    private void movePiece(Vector2 from, Vector2 to) {
        ImageView piece = pieces.get(from);
        piece.setX(90 * to.x);
        piece.setY(90 * to.y);
        pieces.remove(from);
        pieces.put(to, piece);
        engine.move(from, to);
        updateClickEventToPiece(to);
    }

    private void removePiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        chessBoardPane.getChildren().remove(piece);
        pieces.remove(pos);
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
    public void endGame(Piece.Color won) {

    }

    @Override
    public void setCheck(Vector2 checkCoord) {

    }

    @Override
    public void remove(Piece pieceToRemove) {

    }
}
