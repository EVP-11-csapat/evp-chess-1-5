package chess15.gui.controllers;

import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import org.junit.Rule;

import java.io.IOException;
import java.util.Objects;

/**
 * The controller for the settings menu.
 */
public class SettingsMenuController {
    @FXML
    private Button playButton;
    @FXML
    private CheckBox timerCheckBox;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private Spinner<Integer> secondsSpinner;
    @FXML
    private CheckBox castlingCheckBox;
    @FXML
    private CheckBox enpassantCheckBox;
    @FXML
    private CheckBox promotionCheckBox;
    @FXML
    private Button fastpacedButton;
    @FXML
    private Button classicalButton;

    private String selectedGameMode = "";
    private boolean gameTimerEnabled = false;
    private Integer gameTimerMinutes = 5;
    private Integer gameTimerSeconds = 1;
    private boolean isCastlingEnabled = true;
    private boolean isEnPassantEnabled = true;
    private boolean isPromotionEnabled = true;

    private String IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";

    /**
     * Sets the gamemode to what the user pressed to be later used in the game.
     */
    private void setSelectedGameModeButton() {
        if (selectedGameMode.equals("classical")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            classicalButton.setStyle("-fx-background-color: #5A5A5A;");
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
        } else if (selectedGameMode.equals("fastpaced")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle("-fx-background-color: #5A5A5A;");
        }
    }

    /**
     * Disables evry input option
     * This is a utility method for the settings.
     */
    private void disableEverything() {
        playButton.setDisable(true);
        timerCheckBox.setDisable(true);
        minutesSpinner.setDisable(true);
        secondsSpinner.setDisable(true);
        castlingCheckBox.setDisable(true);
        enpassantCheckBox.setDisable(true);
        promotionCheckBox.setDisable(true);
    }

    /**
     * Initializes the settings menu.
     * Sets up the styles and disables everything.
     */
    public void initialize() {
        System.out.println("Settings menu loaded");

        minutesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            gameTimerMinutes = newVal;
        });

        secondsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            gameTimerSeconds = newVal;
        });

        classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
        classicalButton.setOnMouseEntered(e -> classicalButton.setStyle(HOVERED_BUTTON_STYLE));
        classicalButton.setOnMouseExited(e -> classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE));

        fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
        fastpacedButton.setOnMouseEntered(e -> fastpacedButton.setStyle(HOVERED_BUTTON_STYLE));
        fastpacedButton.setOnMouseExited(e -> fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE));

        minutesSpinner.setDisable(true);
        secondsSpinner.setDisable(true);

        timerCheckBox.setDisable(true);
        castlingCheckBox.setDisable(true);
        enpassantCheckBox.setDisable(true);
        promotionCheckBox.setDisable(true);
        playButton.setDisable(true);
    }

    /**
     * When the user preses the classical button. Sets the game mode to classical.
     * And enables the timer and the other options.
     */
    @FXML
    protected void onClassicalSelected() {
        selectedGameMode = "classical";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Classical selected");
        playButton.setText("Play Classical");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
    }

    /**
     * When the user preses the fastpaced button. Sets the game mode to fastpaced.
     * Enables the 3 options
     */
    @FXML
    protected void onFastpacedSelected() {
        selectedGameMode = "fastpaced";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Fast-paced selected");
        playButton.setText("Play Fast-Paced");
        castlingCheckBox.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        playButton.setDisable(false);
    }

    /**
     * When the user enables the timer checkbox, we enable the spinners.
     */
    @FXML
    protected void onTimerEnableTicked()
    {
        gameTimerEnabled = !timerCheckBox.isSelected();
        if (gameTimerEnabled) {
            minutesSpinner.setDisable(false);
            secondsSpinner.setDisable(false);
        } else {
            minutesSpinner.setDisable(true);
            secondsSpinner.setDisable(true);
        }
    }

    /**
     * When the user enables the castling checkbox, we set the castling to true, so we can later use it for the engine.
     */
    @FXML
    protected void onCastlingEnableTicked()
    {
        isCastlingEnabled = !castlingCheckBox.isSelected();
    }

    /**
     * When the user enables the enpassant checkbox, we set the enpassant to true, so we can later use it for the engine.
     */
    @FXML
    protected void onEnPassantEnableTicked()
    {
        isEnPassantEnabled = !enpassantCheckBox.isSelected();
    }

    /**
     * When the user enables the promotion checkbox, we set the promotion to true, so we can later use it for the engine.
     */
    @FXML
    protected void onPromotionEnableEnableTick()
    {
        isPromotionEnabled = !promotionCheckBox.isSelected();
    }

    /**
     * When the user presses the play button, we load the game window, and generate the engine with the specified inputs.
     */
    @FXML
    protected void onPlayButtonPressed() throws IOException {
        if (!selectedGameMode.equals("")) {
            RuleSet rules = RuleSet.getInstance();
            if (Objects.equals(selectedGameMode, "classical")) rules.gamemode = new Classical();
            rules.timer = gameTimerEnabled;
            rules.startTime = gameTimerMinutes;
            rules.timeDelta = gameTimerSeconds;
            rules.castling = isCastlingEnabled;
            rules.promotion = isPromotionEnabled;
            rules.enpassant = isEnPassantEnabled;
            System.out.println("Play button pressed");
            System.out.println("Game mode: " + selectedGameMode);
            System.out.println("Game timer enabled: " + gameTimerEnabled);
            System.out.println("Game timer minutes: " + gameTimerMinutes);
            System.out.println("Game timer seconds: " + gameTimerSeconds);
            System.out.println("Castling enabled: " + isCastlingEnabled);
            System.out.println("En passant enabled: " + isEnPassantEnabled);
            System.out.println("Promotion enabled: " + isPromotionEnabled);
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/chess.fxml")));
            Stage primarStage = (Stage) playButton.getScene().getWindow();
            primarStage.getScene().setRoot(newRoot);
            primarStage.requestFocus();
        } else {
            playButton.setText("Select a game mode");
        }
    }
}