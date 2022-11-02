package chess15.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;

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

    private void setSelectedGameModeButton() {
        if (selectedGameMode.equals("classical")) {
            // TODO: Set the classical button to selected
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            classicalButton.setStyle("-fx-background-color: #5A5A5A;");
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
        } else if (selectedGameMode.equals("fastpaced")) {
            // TODO: Set the fastpaced button to selected
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle("-fx-background-color: #5A5A5A;");
        }
    }

    private void disableEverything() {
        playButton.setDisable(true);
        timerCheckBox.setDisable(true);
        minutesSpinner.setDisable(true);
        secondsSpinner.setDisable(true);
        castlingCheckBox.setDisable(true);
        enpassantCheckBox.setDisable(true);
        promotionCheckBox.setDisable(true);
    }

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

    @FXML
    protected void onCastlingEnableTicked()
    {
        isCastlingEnabled = !castlingCheckBox.isSelected();
    }

    @FXML
    protected void onEnPassantEnableTicked()
    {
        isEnPassantEnabled = !enpassantCheckBox.isSelected();
    }

    @FXML
    protected void onPromotionEnableEnableTick()
    {
        isPromotionEnabled = !promotionCheckBox.isSelected();
    }

    @FXML
    protected void onPlayButtonPressed()
    {
        if (!selectedGameMode.equals("")) {
            System.out.println("Play button pressed");
            System.out.println("Game mode: " + selectedGameMode);
            System.out.println("Game timer enabled: " + gameTimerEnabled);
            System.out.println("Game timer minutes: " + gameTimerMinutes);
            System.out.println("Game timer seconds: " + gameTimerSeconds);
            System.out.println("Castling enabled: " + isCastlingEnabled);
            System.out.println("En passant enabled: " + isEnPassantEnabled);
            System.out.println("Promotion enabled: " + isPromotionEnabled);
        } else {
            playButton.setText("Select a game mode");
        }
    }
}