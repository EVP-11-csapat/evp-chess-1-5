package chess15.gui.controllers;

import chess15.engine.RuleSet;
import chess15.gamemode.*;
import chess15.gui.images.ImageGrabber;
import chess15.gui.scenes.ResourceGrabber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    private ScrollPane gamemodesScroll;
    @FXML
    private CheckBox promotionCheckBox;
    private Button fastpacedButton;
    private Button classicalButton;
    private Button testingButton;
    private Button pawnAttackButton;
    private Button chess960Button;
    private Button chaosModeButton;

    private Pane bgPane;

    private String selectedGameMode = "";
    private boolean gameTimerEnabled = false;
    private Integer gameTimerMinutes = 5;
    private Integer gameTimerSeconds = 1;
    private boolean isCastlingEnabled = true;
    private boolean isEnPassantEnabled = true;
    private boolean isPromotionEnabled = true;

    private String IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
    private String IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";

    private int height = 619;
    private int calcHeight = 0;

    /**
     * Sets the gamemode to what the user pressed to be later used in the game.
     */
    private void setSelectedGameModeButton() {
        if (selectedGameMode.equals("classical")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";

            classicalButton.setStyle("-fx-background-color: #5A5A5A;");
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
            chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
            chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        } else if (selectedGameMode.equals("fastpaced")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";

            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle("-fx-background-color: #5A5A5A;");
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
            chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
            chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        } else if (selectedGameMode.equals("testing")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";

            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
            testingButton.setStyle("-fx-background-color: #5A5A5A;");
            pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
            chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
            chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        } else if (selectedGameMode.equals("pawnattack")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";

            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            pawnAttackButton.setStyle("-fx-background-color: #5A5A5A;");
            chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
            chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        } else if (selectedGameMode.equals("chess960")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";

            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
            chess960Button.setStyle("-fx-background-color: #5A5A5A;");
            chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        } else if (selectedGameMode.equals("chaos")) {
            IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
            IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";

            classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
            fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
            chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
            chaosModeButton.setStyle("-fx-background-color: #5A5A5A;");
        }
    }

    /**
     * Disables evry input option
     * This is a utility method for the settings.
     */
    private void disableEverything() {
        playButton.setDisable(true);
        playButton.setText("Play");
        timerCheckBox.setDisable(true);
        timerCheckBox.setSelected(false);
        gameTimerEnabled = false;
        minutesSpinner.setDisable(true);
        minutesSpinner.getValueFactory().setValue(5);
        gameTimerMinutes = 5;
        secondsSpinner.setDisable(true);
        secondsSpinner.getValueFactory().setValue(1);
        gameTimerSeconds = 1;
        castlingCheckBox.setDisable(true);
        castlingCheckBox.setSelected(true);
        isCastlingEnabled = true;
        enpassantCheckBox.setDisable(true);
        enpassantCheckBox.setSelected(true);
        isEnPassantEnabled = true;
        promotionCheckBox.setDisable(true);
        promotionCheckBox.setSelected(true);
        isPromotionEnabled = true;
    }

    private Button setUpButton(String imageName, String title, String description, int index) {
        Button button = new Button();
        button.setPrefHeight(150);
        button.setPrefWidth(678);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setLayoutY(158 * index);

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_LEFT);

        ImageView icon = new ImageView(String.valueOf(ImageGrabber.getInstance().getClass().getResource("" + imageName + ".png")));
        icon.setFitHeight(150);
        icon.setFitWidth(150);

        Separator separator1 = new Separator();
        separator1.setOpacity(0);
        separator1.setPrefWidth(33);
        separator1.setPrefHeight(150);

        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.TOP_LEFT);
        textVBox.setPrefHeight(150);
        textVBox.setPrefWidth(452);

        Label titleLabel = new Label();
        titleLabel.setText(title);
        titleLabel.setFont(new Font("System", 45));
        titleLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        titleLabel.setAlignment(Pos.CENTER_LEFT);

        Separator separator2 = new Separator();
        separator2.setOpacity(0);
        separator2.setOrientation(Orientation.HORIZONTAL);
        separator2.setPrefWidth(200);

        Label descriptionLabel = new Label();
        descriptionLabel.setText(description);
        descriptionLabel.setFont(new Font("System", 19));
        descriptionLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        descriptionLabel.setAlignment(Pos.CENTER_LEFT);

        textVBox.getChildren().add(titleLabel);
        textVBox.getChildren().add(separator2);
        textVBox.getChildren().add(descriptionLabel);

        buttonBox.getChildren().add(icon);
        buttonBox.getChildren().add(separator1);
        buttonBox.getChildren().add(textVBox);

        button.setGraphic(buttonBox);

        calcHeight += 158;
        bgPane.getChildren().add(button);
        return button;
    }

    /**
     * Initializes the settings menu.
     * Sets up the styles and disables everything.
     */
    public void initialize() {
        System.out.println("Settings menu loaded");

        // Set up button background pane
        bgPane = new Pane();
        bgPane.setStyle("-fx-background-color: #2A2A2A;");

        // Create buttons
        classicalButton =  setUpButton("classical", "Classical",
                "Just a standard game of chess. \nWith regular rules and a timer.", 0);
        classicalButton.setOnMousePressed(e -> onClassicalSelected());

        fastpacedButton =  setUpButton("fastpaced", "Fast-Paced",
                "No time to think! \nEvery 2 seconds a random move is made.", 1);
        fastpacedButton.setOnMousePressed(e -> onFastpacedSelected());

        testingButton =  setUpButton("fastpaced", "Testing",
                "Made for testing", 2);
        testingButton.setOnMousePressed(e -> onTestingSelected());

        pawnAttackButton =  setUpButton("fastpaced", "Pawn attack",
                "Just a bunch of pawns and their king", 3);
        pawnAttackButton.setOnMousePressed(e -> onPawnAttackSelected());

        chess960Button = setUpButton("fastpaced", "Chess960",
                "A chess variant where the back row is randomized", 4);
        chess960Button.setOnMousePressed(e -> onChess960Selected());

        chaosModeButton = setUpButton("fastpaced", "Chaos Mode",
                "WARNING: This mode is chaotic", 5);
        chaosModeButton.setOnMousePressed(e -> onChaosModeSelected());


        // Set button background onto scroll pane
        if (calcHeight > height) height = calcHeight;
        bgPane.setPrefHeight(height);
        gamemodesScroll.setContent(bgPane);
        gamemodesScroll.setFitToWidth(true);

        // Set scroll speed
        final double SPEED = 0.01;
        gamemodesScroll.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SPEED;
            gamemodesScroll.setVvalue(gamemodesScroll.getVvalue() - deltaY);
        });

        minutesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            gameTimerMinutes = newVal;
        });

        secondsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            gameTimerSeconds = newVal;
        });

        // Set up button styles
        classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
        classicalButton.setOnMouseEntered(e -> classicalButton.setStyle(HOVERED_BUTTON_STYLE));
        classicalButton.setOnMouseExited(e -> classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE));

        fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
        fastpacedButton.setOnMouseEntered(e -> fastpacedButton.setStyle(HOVERED_BUTTON_STYLE));
        fastpacedButton.setOnMouseExited(e -> fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE));

        testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
        testingButton.setOnMouseEntered(e -> testingButton.setStyle(HOVERED_BUTTON_STYLE));
        testingButton.setOnMouseExited(e -> testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE));

        pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
        pawnAttackButton.setOnMouseEntered(e -> pawnAttackButton.setStyle(HOVERED_BUTTON_STYLE));
        pawnAttackButton.setOnMouseExited(e -> pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE));

        chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
        chess960Button.setOnMouseEntered(e -> chess960Button.setStyle(HOVERED_BUTTON_STYLE));
        chess960Button.setOnMouseExited(e -> chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE));

        chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
        chaosModeButton.setOnMouseEntered(e -> chaosModeButton.setStyle(HOVERED_BUTTON_STYLE));
        chaosModeButton.setOnMouseExited(e -> chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE));

        disableEverything();
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

    protected void onPawnAttackSelected() {
        selectedGameMode = "pawnattack";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Pawn attack selected");
        playButton.setText("Play Pawn attack");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        isCastlingEnabled = false;
        castlingCheckBox.setSelected(false);
        castlingCheckBox.setDisable(true);
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
        timerCheckBox.setDisable(true);
        timerCheckBox.setSelected(true);
        gameTimerEnabled = true;
        gameTimerMinutes = 90;
        gameTimerSeconds = 0;
        minutesSpinner.getValueFactory().setValue(90);
        secondsSpinner.getValueFactory().setValue(0);
        castlingCheckBox.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        playButton.setDisable(false);
    }

    @FXML
    protected void onTestingSelected() {
        selectedGameMode = "testing";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Testing selected");
        playButton.setText("Play Testing");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
    }

    protected void onChess960Selected() {
        selectedGameMode = "chess960";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Chess960 selected");
        playButton.setText("Play Chess96");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
    }

    protected void onChaosModeSelected() {
        selectedGameMode = "chaos";
        disableEverything();
        setSelectedGameModeButton();
        System.out.println("Chaos mode selected");
        playButton.setText("Play Chaos Mode");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
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
            if (Objects.equals(selectedGameMode, "fastpaced")) rules.gamemode = new Fastpaced();
            if (Objects.equals(selectedGameMode, "testing")) rules.gamemode = new Testing();
            if (Objects.equals(selectedGameMode, "pawnattack")) rules.gamemode = new PawnAttack();
            if (Objects.equals(selectedGameMode, "chess960")) rules.gamemode = new Chess960();
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
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(ResourceGrabber.getInstance().getClass().getResource("chess.fxml")));
//            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../scenes/chess.fxml")));
            Stage primarStage = (Stage) playButton.getScene().getWindow();
            primarStage.getScene().setRoot(newRoot);
            primarStage.requestFocus();
        } else {
            playButton.setText("Select a game mode");
        }
    }
}