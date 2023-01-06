package chess15.gui.controllers;

import chess15.engine.RuleSet;
import chess15.gamemode.Classical;
import chess15.gui.images.ImageGrabber;
import chess15.gui.scenes.ResourceGrabber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NewMainMenuController {
    @FXML
    private Pane main;

    private Button mpButton;
    private Button spButton;
    private Label anotherLabel;
    private Label aiLabel;

    private DropShadow dropShadow = new DropShadow();
    private InnerShadow innerShadow = new InnerShadow();

    public void initialize() {
        dropShadow.setRadius(8);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(4);
        dropShadow.setColor(Color.color(0,0,0,0.25));

        innerShadow.setOffsetX(0);
        innerShadow.setOffsetY(4);
        innerShadow.setRadius(4);
        innerShadow.setColor(Color.color(0,0,0,0.25));

        Pane fullPage = setUpPage();

        main.getChildren().add(fullPage);
    }

    private Pane setUpPage() {
        Pane basePane = new Pane();

        basePane.setPrefHeight(720);
        basePane.setPrefWidth(1280);
        Background bg = new Background(new BackgroundFill(Paint.valueOf("#2a2a2a"), CornerRadii.EMPTY, Insets.EMPTY));
        basePane.setBackground(bg);

        // Set up main title
        Label title = new Label();
        title.setText("Chess 1.5");
        title.setFont(Font.font(Font.getDefault().getName(), 96));
        title.setTextFill(Paint.valueOf("#cdcdcd"));
        title.layoutXProperty().bind(basePane.widthProperty().subtract(title.widthProperty()).divide(2));
        title.setLayoutY(70);
        title.setEffect(innerShadow);

        // Set up play against
        Label playAgainst = new Label();
        playAgainst.setText("play against...");
        playAgainst.setFont(Font.font(Font.getDefault().getName(), 43));
        playAgainst.setTextFill(Paint.valueOf("#bbbbbb"));
        playAgainst.layoutXProperty().bind(basePane.widthProperty().subtract(playAgainst.widthProperty()).divide(2));
        playAgainst.setLayoutY(220);

        playAgainst.setEffect(innerShadow);

        // Set up 2 pictures
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(4);
        dropShadow.setColor(Color.color(0,0,0,0.25));

        Image mPlayerImage;
        Image sPlayerImage;

        try {
            mPlayerImage = new Image(Objects.requireNonNull(ImageGrabber.getInstance().getClass().getResource("multiplayer.png")).openStream());
            sPlayerImage = new Image(Objects.requireNonNull(ImageGrabber.getInstance().getClass().getResource("ai.png")).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageView mPlayerView = new ImageView();
        ImageView sPlayerView = new ImageView();

        mPlayerView.setImage(mPlayerImage);
        mPlayerView.setFitWidth(200);
        mPlayerView.setFitHeight(150);

        sPlayerView.setImage(sPlayerImage);
        sPlayerView.setFitWidth(230);
        sPlayerView.setFitHeight(180);

        mPlayerView.setY(360);
        mPlayerView.setX(223);

        sPlayerView.setY(350);
        sPlayerView.setX(837);

        mPlayerView.setEffect(dropShadow);
        sPlayerView.setEffect(dropShadow);

        // Set up buttons
        mpButton = new Button();
        spButton = new Button();

        Background buttonBG = new Background(new BackgroundFill(Paint.valueOf("#424242"), new CornerRadii(5), Insets.EMPTY));

        mpButton.setPrefWidth(300);
        mpButton.setPrefHeight(80);
        mpButton.setLayoutY(520);
        mpButton.setLayoutX(179);
        mpButton.setEffect(dropShadow);
        mpButton.setBackground(buttonBG);

        spButton.setPrefWidth(300);
        spButton.setPrefHeight(80);
        spButton.setLayoutY(520);
        spButton.setLayoutX(807);
        spButton.setEffect(dropShadow);
        spButton.setBackground(buttonBG);

        anotherLabel = new Label();
        anotherLabel.setText("Another human");
        anotherLabel.setFont(Font.font(Font.getDefault().getName(), 34));
        anotherLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        anotherLabel.setEffect(innerShadow);

        mpButton.setGraphic(anotherLabel);

        aiLabel = new Label();
        aiLabel.setText("The computer");
        aiLabel.setFont(Font.font(Font.getDefault().getName(), 34));
        aiLabel.setTextFill(Paint.valueOf("#cdcdcd"));
        aiLabel.setEffect(innerShadow);

        spButton.setGraphic(aiLabel);

        spButton.setOnMouseEntered(e -> onSpButtonMouseEnter());
        spButton.setOnMouseExited(e -> onSpButtonMouseLeave());
        spButton.setOnMousePressed(e -> onSpButtonPressed());

        mpButton.setOnMouseEntered(e -> onMpButtonMouseEnter());
        mpButton.setOnMouseExited(e -> onMpButtonMouseLeave());
        mpButton.setOnMousePressed(e -> onMpButtonPressed());

        basePane.getChildren().addAll(title, playAgainst, sPlayerView, mPlayerView,
                mpButton, spButton);
        return basePane;
    }

    private void onSpButtonMouseEnter() {
        aiLabel.setEffect(dropShadow);
        spButton.setEffect(innerShadow);
    }

    private void onSpButtonMouseLeave() {
        aiLabel.setEffect(innerShadow);
        spButton.setEffect(dropShadow);
    }

    private void onMpButtonMouseEnter() {
        anotherLabel.setEffect(dropShadow);
        mpButton.setEffect(innerShadow);
    }

    private void onMpButtonMouseLeave() {
        anotherLabel.setEffect(innerShadow);
        mpButton.setEffect(dropShadow);
    }

    private void onMpButtonPressed() {
        Parent newRoot;
        try {
            newRoot = FXMLLoader.load(Objects.requireNonNull(
                    ResourceGrabber.getInstance().getClass().getResource("settingsMenu.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage primaryStage = (Stage) main.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }

    private void onSpButtonPressed() {
        RuleSet rules = RuleSet.getInstance();
        rules.castling = true;
        rules.enpassant = true;
        rules.promotion = true;
        rules.gamemode = new Classical();
        rules.startTime = 1;
        rules.timeDelta = 5;
        rules.timer = false;
        rules.isAiGame = true;

        Parent newRoot;
        try {
            newRoot = FXMLLoader.load(Objects.requireNonNull(
                    ResourceGrabber.getInstance().getClass().getResource("newChess.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage primaryStage = (Stage) main.getScene().getWindow();
        primaryStage.getScene().setRoot(newRoot);
        primaryStage.requestFocus();
    }
}
