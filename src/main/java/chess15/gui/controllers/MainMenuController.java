package chess15.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The controller for the main menu.
 */
public class MainMenuController {
    @FXML
    protected Button multiplayerButton;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: #424242;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";

    /**
     * Initializes the controller.
     * Sets the button styles for mouse interaction.
     */
    public void initialize() {
        multiplayerButton.setStyle(IDLE_BUTTON_STYLE);
        multiplayerButton.setOnMouseEntered(event -> multiplayerButton.setStyle(HOVERED_BUTTON_STYLE));
        multiplayerButton.setOnMouseExited(event -> multiplayerButton.setStyle(IDLE_BUTTON_STYLE));
    }

    /**
     * Opens the multiplayer menu.
     * @throws IOException if the fxml file cannot be found.
     */
    @FXML
    protected void onMultiplayerButtonPressed() throws IOException {
        System.out.println("Multiplayer button pressed");
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/settingsMenu.fxml")));
        Stage primaryStage = (Stage) multiplayerButton.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }

    @FXML
    protected void onAiButtonPressed() throws IOException {
        System.out.println("AI button pressed");
        Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/chess.fxml")));
        Stage primaryStage = (Stage) multiplayerButton.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }
}
