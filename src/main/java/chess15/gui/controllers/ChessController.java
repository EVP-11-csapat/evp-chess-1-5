package chess15.gui.controllers;

import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//import java.awt.*;

public class ChessController {
    private HashMap<Vector2, ImageView> pieces = new HashMap<>();
    private HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();

    Scene scene;

    @FXML
    private ImageView chessBoard;

    @FXML
    private Pane chessBoardPane;

    @FXML
    private StackPane chessBoardStackPane;

    public void initialize() throws IOException {
        scene = chessBoard.getScene();
        System.out.println("Chess controller initialized");
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.PAWN, Pawn.getInstance(), false), new Vector2(1, 1));
        addClickEventToPiece(new Vector2(1, 1));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.PAWN, Pawn.getInstance(), false), new Vector2(0, 0));
        addClickEventToPiece(new Vector2(0, 0));
        movePiece(new Vector2(0, 0), new Vector2(0, 1));
    }

    private void createPiece(Piece p, Vector2 pos) throws IOException {
        System.out.println("Creating piece");
        Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/ai.png")).openStream());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(90);
        imageView.setFitWidth(90);
        imageView.setX(90 * pos.x);
        imageView.setY(90 * pos.y);
        pieces.put(pos, imageView);
        chessBoardPane.getChildren().add(imageView);
    }

    private void handlePieceClick(Vector2 pos, MouseEvent event) throws IOException {
        System.out.println("Clicked on piece at " + pos + " with event " + event);
        displayPosibleMoves(new ArrayList<>() {
            {
                add(new Vector2(0, 2));
                add(new Vector2(1, 2));
            }
        }, pos);
    }

    private void updateClickEventToPiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        piece.setOnMouseClicked(event -> {
            try {
                handlePieceClick(pos, event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addClickEventToPiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        piece.setOnMouseClicked(event -> {
            try {
                handlePieceClick(pos, event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void movePiece(Vector2 from, Vector2 to) {
        ImageView piece = pieces.get(from);
        piece.setX(90 * to.x);
        piece.setY(90 * to.y);
        pieces.remove(from);
        pieces.put(to, piece);
        updateClickEventToPiece(to);
    }

    private void removePiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        chessBoardPane.getChildren().remove(piece);
        pieces.remove(pos);
    }

    private void handleMove(Vector2 fromPos, Vector2 toPos, MouseEvent event) throws IOException {
        System.out.println("Clicked on move at " + fromPos + " with event " + event);
        movePiece(fromPos, toPos);
        removePosibleMoves();
    }

    private void displayPosibleMoves(ArrayList<Vector2> moves, Vector2 piece) throws IOException {
        System.out.println("Displaying posible moves");
        for (Vector2 move : moves) {
            Image image = new Image(Objects.requireNonNull(getClass().getResource("../images/possibleMove.jpg")).openStream());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitHeight(90);
            imageView.setFitWidth(90);
            imageView.setX(90 * move.x);
            imageView.setY(90 * move.y);
            imageView.setOpacity(0.5);
            imageView.setOnMouseClicked(event -> {
                try {
                    handleMove(piece, move, event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            possibleMoves.put(move, imageView);
            chessBoardPane.getChildren().add(imageView);
        }
    }

    private void removePosibleMoves() {
        for (Vector2 move : possibleMoves.keySet()) {
            ImageView imageView = possibleMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        possibleMoves.clear();
    }
}
