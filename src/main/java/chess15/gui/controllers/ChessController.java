package chess15.gui.controllers;

import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

//import java.awt.*;

public class ChessController {
    private ArrayList<ImageView> pieces = new ArrayList<>();

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
        imageView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Clicked");
        });
        pieces.add(imageView);
        chessBoardPane.getChildren().add(imageView);
    }
}
