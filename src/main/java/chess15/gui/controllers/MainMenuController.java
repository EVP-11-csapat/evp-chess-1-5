package chess15.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    @FXML
    protected Button multiplayerButton;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: #424242;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";



    public void initialize() {
        multiplayerButton.setStyle(IDLE_BUTTON_STYLE);
        multiplayerButton.setOnMouseEntered(event -> multiplayerButton.setStyle(HOVERED_BUTTON_STYLE));
        multiplayerButton.setOnMouseExited(event -> multiplayerButton.setStyle(IDLE_BUTTON_STYLE));
    }

    @FXML
    protected void onMultiplayerButtonPressed() throws IOException {
        System.out.println("Multiplayer button pressed");
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/settingsMenu.fxml")));
        Stage primaryStage = (Stage) multiplayerButton.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }
}
