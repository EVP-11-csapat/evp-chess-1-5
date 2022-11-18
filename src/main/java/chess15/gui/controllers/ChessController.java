package chess15.gui.controllers;

import chess15.Pawn;
import chess15.Piece;
import chess15.Vector2;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

//import java.awt.*;

public class ChessController {
    Scene scene;

    @FXML
    private ImageView chessBoard;
    @FXML
    private HBox chessBoardHBox;

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
        chessBoardHBox.getChildren().add(imageView);
    }
}
