package chess15.gui.controllers;

import chess15.engine.RuleSet;
import chess15.gamemode.*;
import chess15.gui.images.ImageGrabber;
import chess15.gui.scenes.ResourceGrabber;
import chess15.gui.util.Constants;
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
    // FXML variables used by ui
    @FXML
    private Button playButton;
    @FXML
    private Button backButton;
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

    // Button variables added to ui dynamically
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

    // Style buttons separated per button
    // Used to keep style after pressing so hover effect doesn't override it
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
        // Styles are set here so hover effect won't override them
        switch (selectedGameMode) {
            case "classical" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
                classicalButton.setStyle("-fx-background-color: #5A5A5A;");
                fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
                if (Constants.DEVMODE)
                    testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
                pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
                chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
                chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
            }
            case "fastpaced" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
                classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
                fastpacedButton.setStyle("-fx-background-color: #5A5A5A;");
                if (Constants.DEVMODE)
                    testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
                pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
                chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
                chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
            }
            case "testing" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
                classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
                fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
                if (Constants.DEVMODE)
                    testingButton.setStyle("-fx-background-color: #5A5A5A;");
                pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
                chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
                chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
            }
            case "pawnattack" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
                classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
                fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
                if (Constants.DEVMODE)
                    testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
                pawnAttackButton.setStyle("-fx-background-color: #5A5A5A;");
                chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
                chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
            }
            case "chess960" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: transparent;";
                classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
                fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
                if (Constants.DEVMODE)
                    testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
                pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
                chess960Button.setStyle("-fx-background-color: #5A5A5A;");
                chaosModeButton.setStyle(IDLE_CHAOS_MODE_BUTTON_STYLE);
            }
            case "chaos" -> {
                IDLE_CLASSICAL_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_FASTPACED_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_TESTING_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_PAWNATTACK_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHESS960_BUTTON_STYLE = "-fx-background-color: transparent;";
                IDLE_CHAOS_MODE_BUTTON_STYLE = "-fx-background-color: #5A5A5A;";
                classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
                fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
                if (Constants.DEVMODE)
                    testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
                pawnAttackButton.setStyle(IDLE_PAWNATTACK_BUTTON_STYLE);
                chess960Button.setStyle(IDLE_CHESS960_BUTTON_STYLE);
                chaosModeButton.setStyle("-fx-background-color: #5A5A5A;");
            }
        }
    }

    /**
     * Disables evry input option
     * Sets default values for every option
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

    /**
     * Creates the button with the correct image, title, description, and position
     * @param imageName The name of the image file without the .png extension
     * @param title The title that should be displayed on top of the button
     * @param description The description under the button
     * @param index The index in the list, used to calculate the y offset position
     * @return The {@link Button} that has been created and added to the ui
     */
    private Button setUpButton(String imageName, String title, String description, int index) {
        // Default button dimensions
        Button button = new Button();
        button.setPrefHeight(150);
        button.setPrefWidth(678);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setLayoutY(158 * index);

        // Hbox to separate the image from the text
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_LEFT);

        // Image of the button
        ImageView icon = new ImageView(String.valueOf(ImageGrabber.getInstance().getClass().getResource("" + imageName + ".png")));
        icon.setFitHeight(150);
        icon.setFitWidth(150);

        // Separator to fine control space between the image and the text
        Separator separator1 = new Separator();
        separator1.setOpacity(0);
        separator1.setPrefWidth(33);
        separator1.setPrefHeight(150);

        // Vbox to give the title and the description place
        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.TOP_LEFT);
        textVBox.setPrefHeight(150);
        textVBox.setPrefWidth(452);

        // The title of the button
        Label titleLabel = new Label();
        titleLabel.setText(title);
        titleLabel.setFont(new Font("System", 45));
        titleLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        titleLabel.setAlignment(Pos.CENTER_LEFT);

        // Separator to fine control space between the title and the description
        Separator separator2 = new Separator();
        separator2.setOpacity(0);
        separator2.setOrientation(Orientation.HORIZONTAL);
        separator2.setPrefWidth(200);

        // The description of the gamemode
        Label descriptionLabel = new Label();
        descriptionLabel.setText(description);
        descriptionLabel.setFont(new Font("System", 19));
        descriptionLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        descriptionLabel.setAlignment(Pos.CENTER_LEFT);

        // Add the title separator and description to the Vbox
        textVBox.getChildren().add(titleLabel);
        textVBox.getChildren().add(separator2);
        textVBox.getChildren().add(descriptionLabel);

        // Add the image separator and textVbox to the HBox
        buttonBox.getChildren().add(icon);
        buttonBox.getChildren().add(separator1);
        buttonBox.getChildren().add(textVBox);

        // Set the button graphics to the HBox
        button.setGraphic(buttonBox);

        // calculate the height dinamically
        calcHeight += 158;
        // Add the button to the scroll pane
        bgPane.getChildren().add(button);
        return button;
    }

    /**
     * Initializes the settings menu.
     * Sets up the styles and disables everything.
     */
    public void initialize() {
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

        pawnAttackButton =  setUpButton("pawnattack", "Pawn attack",
                "Just a bunch of pawns and their king", 2);
        pawnAttackButton.setOnMousePressed(e -> onPawnAttackSelected());

        chess960Button = setUpButton("chess960", "Chess960",
                "A chess variant where the back row is randomized", 3);
        chess960Button.setOnMousePressed(e -> onChess960Selected());

        chaosModeButton = setUpButton("chaos", "Chaos Mode",
                "WARNING: This mode is chaotic", 4);
        chaosModeButton.setOnMousePressed(e -> onChaosModeSelected());

        if (Constants.DEVMODE) {
            testingButton =  setUpButton("fastpaced", "Testing",
                    "Made for testing", 5);
            testingButton.setOnMousePressed(e -> onTestingSelected());
        }

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

        // Set up value factory for the spinners
        minutesSpinner.valueProperty().addListener((obs, oldVal, newVal) -> gameTimerMinutes = newVal);

        secondsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> gameTimerSeconds = newVal);

        // Set up button styles
        classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE);
        classicalButton.setOnMouseEntered(e -> classicalButton.setStyle(HOVERED_BUTTON_STYLE));
        classicalButton.setOnMouseExited(e -> classicalButton.setStyle(IDLE_CLASSICAL_BUTTON_STYLE));

        fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE);
        fastpacedButton.setOnMouseEntered(e -> fastpacedButton.setStyle(HOVERED_BUTTON_STYLE));
        fastpacedButton.setOnMouseExited(e -> fastpacedButton.setStyle(IDLE_FASTPACED_BUTTON_STYLE));

        if (Constants.DEVMODE) {
            testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE);
            testingButton.setOnMouseEntered(e -> testingButton.setStyle(HOVERED_BUTTON_STYLE));
            testingButton.setOnMouseExited(e -> testingButton.setStyle(IDLE_TESTING_BUTTON_STYLE));
        }

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
        if (Constants.DEVMODE)
            System.out.println("Classical selected");
        playButton.setText("Play Classical");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
    }

    /**
     * Set up the game mode for pawn attack
     */
    protected void onPawnAttackSelected() {
        selectedGameMode = "pawnattack";
        disableEverything();
        setSelectedGameModeButton();
        if (Constants.DEVMODE)
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
        if (Constants.DEVMODE)
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
        if (Constants.DEVMODE)
            System.out.println("Testing selected");
        playButton.setText("Play Testing");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(false);
    }

    /**
     * Set up the game mode for chess 960 gamemode
     */
    protected void onChess960Selected() {
        selectedGameMode = "chess960";
        disableEverything();
        setSelectedGameModeButton();
        if (Constants.DEVMODE)
            System.out.println("Chess960 selected");
        playButton.setText("Play Chess96");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(true);
        castlingCheckBox.setSelected(false);
        isCastlingEnabled = false;
    }

    /**
     * Set up the game mode for chaos mode
     */
    protected void onChaosModeSelected() {
        selectedGameMode = "chaos";
        disableEverything();
        setSelectedGameModeButton();
        if (Constants.DEVMODE)
            System.out.println("Chaos mode selected");
        playButton.setText("Play Chaos Mode");
        timerCheckBox.setDisable(false);
        playButton.setDisable(false);
        enpassantCheckBox.setDisable(false);
        promotionCheckBox.setDisable(false);
        castlingCheckBox.setDisable(true);
        castlingCheckBox.setSelected(false);
        isCastlingEnabled = false;
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
    protected void onPlayButtonPressed() {
        if (!selectedGameMode.equals("")) {
            RuleSet rules = RuleSet.getInstance();
            switch (selectedGameMode) {
                case "classical" -> rules.gamemode = new Classical();
                case "fastpaced" -> rules.gamemode = new Fastpaced();
                case "testing" -> rules.gamemode = new Testing();
                case "pawnattack" -> rules.gamemode = new PawnAttack();
                case "chess960" -> rules.gamemode = new Chess960();
                case "chaos" -> rules.gamemode = new ChaosMode();
            }
            rules.timer = gameTimerEnabled;
            rules.startTime = gameTimerMinutes;
            rules.timeDelta = gameTimerSeconds;
            rules.castling = isCastlingEnabled;
            rules.promotion = isPromotionEnabled;
            rules.enpassant = isEnPassantEnabled;
            Parent newRoot;
            try {
                newRoot = FXMLLoader.load(Objects.requireNonNull(ResourceGrabber.getInstance().getClass().getResource("chess.fxml")));
            } catch (IOException e) {
                System.out.println("ERROR in SettingsMenuController (onPlayButtonPressed): Chess.fxml not found");
                throw new RuntimeException(e);
            }
            Stage primarStage = (Stage) playButton.getScene().getWindow();
            primarStage.getScene().setRoot(newRoot);
            primarStage.requestFocus();
        } else {
            playButton.setText("Select a game mode");
        }
    }

    /**
     * When the user presses the back button, we take them back to the main menu
     */
    @FXML
    protected void onBackButtonPressed() {
        Parent newRoot;
        try {
            newRoot = FXMLLoader.load(Objects.requireNonNull(ResourceGrabber.getInstance().getClass().getResource("mainMenu.fxml")));
        } catch (IOException e) {
            System.out.println("ERROR in SettingsMenuController (onBackButtonPressed): Chess.fxml not found");
            throw new RuntimeException(e);
        }
        Stage primaryStage = (Stage) backButton.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }
}