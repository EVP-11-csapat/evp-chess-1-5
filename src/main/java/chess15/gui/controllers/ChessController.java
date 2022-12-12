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
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.ROOK, Pawn.getInstance(), false), new Vector2(0, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.KNIGHT, Pawn.getInstance(), false), new Vector2(1, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.BISHOP, Pawn.getInstance(), false), new Vector2(2, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.QUEEN, Pawn.getInstance(), false), new Vector2(3, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.KING, Pawn.getInstance(), false), new Vector2(4, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.BISHOP, Pawn.getInstance(), false), new Vector2(5, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.KNIGHT, Pawn.getInstance(), false), new Vector2(6, 0));
        createPiece(new Piece(Piece.Color.BLACK, Piece.Type.ROOK, Pawn.getInstance(), false), new Vector2(7, 0));
        for (int i = 0; i < 8; i++) {
            createPiece(new Piece(Piece.Color.BLACK, Piece.Type.PAWN, Pawn.getInstance(), false), new Vector2(i, 1));
        }

        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Pawn.getInstance(), false), new Vector2(0, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Pawn.getInstance(), false), new Vector2(1, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Pawn.getInstance(), false), new Vector2(2, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Pawn.getInstance(), false), new Vector2(3, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.KING, Pawn.getInstance(), false), new Vector2(4, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Pawn.getInstance(), false), new Vector2(5, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Pawn.getInstance(), false), new Vector2(6, 7));
        createPiece(new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Pawn.getInstance(), false), new Vector2(7, 7));
        for (int i = 0; i < 8; i++) {
            createPiece(new Piece(Piece.Color.WHITE, Piece.Type.PAWN, Pawn.getInstance(), false), new Vector2(i, 6));
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
        displayPosibleMoves(new ArrayList<>() {
            {
                add(new Vector2(0, 2));
                add(new Vector2(1, 2));
            }
        }, pos);
    }

    private void updateClickEventToPiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        piece.setOnMousePressed(event -> {
            try {
                handlePieceClick(pos, event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addClickEventToPiece(Vector2 pos, ImageView piece) {
        System.out.println("Creating click listener for: " + pos );
        piece.setOnMousePressed(event -> {
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
}
