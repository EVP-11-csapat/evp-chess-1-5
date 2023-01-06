package chess15.gui.controllers;

import chess15.board.Move;
import chess15.board.Piece;
import chess15.board.Vector2;
import chess15.engine.RuleSet;
import chess15.gamemode.Fastpaced;
import chess15.gui.images.ImageGrabber;
import chess15.gui.interfaces.UIInteface;
import chess15.gui.newui.General;
import chess15.gui.newui.Initializer;
import chess15.gui.newui.MoveRepr;
import chess15.gui.util.AudioPlayer;
import chess15.util.WinReason;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static chess15.gui.newui.Variables.*;

public class NewChessController implements UIInteface {
    @FXML
    public VBox rightPanel;
    @FXML
    public Pane chessBoardPane;

    public void initialize() {
        Initializer initializer = new Initializer();
        initializer.initGameElements(this);
    }

    private void displayTaken() {
        int whiteSpacing = whiteTaken.getChildren().size();
        int blackSpacing = blackTaken.getChildren().size();
        int whiteTakenWidth = 500;
        int blackTakenWidth = 500;
        if (70 * whiteSpacing > whiteTakenWidth) whiteTakenWidth = 70 * whiteSpacing;
        if (70 * blackSpacing > blackTakenWidth) blackTakenWidth = 70 * blackSpacing;
        whiteTaken.setPrefWidth(whiteTakenWidth);
        blackTaken.setPrefWidth(blackTakenWidth);
        whiteTakenScroll.setContent(whiteTaken);
        blackTakenScroll.setContent(blackTaken);
    }

    private void addToTaken(Piece piece) {
        String imagePath = "pieces/" +
                General.getPieceColorString(piece) +
                "-" +
                General.getPieceTypeString(piece) +
                ".png";
        Image image;
        try {
            image = new Image(Objects.requireNonNull(
                    ImageGrabber.getInstance().getClass().getResource("" + imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("ERROR in ChessController (addToTaken): Image not found");
            throw new RuntimeException(e);
        }
        ImageView pieceImage = new ImageView();
        pieceImage.setImage(image);
        pieceImage.setFitHeight(70);
        pieceImage.setFitWidth(70);
        int spacign;

        if (piece.color == Piece.Color.WHITE) spacign = whiteTaken.getChildren().size();
        else spacign = blackTaken.getChildren().size();

        pieceImage.setX(70 * spacign);
        takenList.put(piece, pieceImage);

        if (piece.color == Piece.Color.WHITE) whiteTaken.getChildren().add(pieceImage);
        else blackTaken.getChildren().add(pieceImage);
    }

    private void clearTaken() {
        for (Piece piece : takenList.keySet()) {
            ImageView imageView = takenList.get(piece);
            if (piece.color == Piece.Color.WHITE) whiteTaken.getChildren().remove(imageView);
            else blackTaken.getChildren().remove(imageView);
        }
        Background bg = new Background(new BackgroundFill(Paint.valueOf("#4a4a4a"),
                CornerRadii.EMPTY, Insets.EMPTY));
        takenList.clear();
        whiteTaken = new Pane();
        blackTaken = new Pane();
        whiteTaken.setPrefHeight(90);
        whiteTaken.setBackground(bg);
        blackTaken.setPrefHeight(90);
        blackTaken.setBackground(bg);

    }

    private void handleTakenList() {
        clearTaken();
        for (Piece p : takenPieces) {
            addToTaken(p);
        }
        displayTaken();
    }

    private String generateMoveString(Move move) {
        String color = move.color == Piece.Color.WHITE ? "White" : "Black";
        String fromCoord = General.convertMoveToChessCord(move.from);
        String toCoord = General.convertMoveToChessCord(move.to);

        return fromCoord.toUpperCase() + "-" + toCoord.toUpperCase();
    }

    private void updateMoveList(Move moveToAdd) {
        if (moveToAdd.color == Piece.Color.WHITE) {
            moveTable.getItems().add(new MoveRepr(generateMoveString(moveToAdd), null));
        } else {
            String prevMove = moveTable.getItems().get(moveTable.getItems().size() - 1).getWhiteMove();
            moveTable.getItems().remove(moveTable.getItems().size() - 1);
            moveTable.getItems().add(new MoveRepr(prevMove, generateMoveString(moveToAdd)));
        }

        moveTable.scrollTo(moveTable.getItems().size());

        if (DEVMODE)
            logger.info("Move String added: " + generateMoveString(moveToAdd));
    }

    private void displayFromTo(Move move) {
        Image image;
        try {
            image = new Image(Objects.requireNonNull(
                    ImageGrabber.getInstance().getClass().getResource("move.png")).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageView fromMove = new ImageView();
        fromMove.setImage(image);
        fromMove.setFitHeight(90);
        fromMove.setFitWidth(90);
        fromMove.setX(90 * move.from.x);
        fromMove.setY(90 * move.from.y);
        fromMove.setOpacity(0.2);
        fromToMoves.put(move.from, fromMove);
        chessBoardPane.getChildren().add(fromMove);
        ImageView toMove = new ImageView();
        toMove.setImage(image);
        toMove.setFitHeight(90);
        toMove.setFitWidth(90);
        toMove.setX(90 * move.to.x);
        toMove.setY(90 * move.to.y);
        toMove.setOpacity(0.2);
        fromToMoves.put(move.to, toMove);
        chessBoardPane.getChildren().add(toMove);
    }

    private Piece.Color playerColor() {
        if (algColor == Piece.Color.WHITE) return Piece.Color.BLACK;
        else return Piece.Color.WHITE;
    }

    private void handleTimerUpdate(Piece.Color color) {
        timerWhiteSide = color != Piece.Color.WHITE;
        if (RuleSet.getInstance().timeDelta != 0) {
            if (color == Piece.Color.WHITE) whiteTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
            if (color == Piece.Color.BLACK) blackTimeInMillis += RuleSet.getInstance().timeDelta * 1000L;
        }

        whiteTimerLabel.setText(Initializer.formatTime(whiteTimeInMillis));
        blackTimerLabel.setText(Initializer.formatTime(blackTimeInMillis));
    }

    public void movePiece(Move pMove) {
        removeFromTo();
        whiteToMove = !whiteToMove;
        AtomicBoolean moveFinished = new AtomicBoolean(false);

        if (RuleSet.getInstance().gamemode instanceof Fastpaced) {
            fastPacedCounter = 0;
            removePosibleMoves();
        }

        ImageView pieceView = pieces.get(pMove.from);
        Piece piece = (Piece) engine.getBoard().getElement(pMove.from);

        boolean playedTake = false;

        if (engine.getBoard().getElement(pMove.to) instanceof Piece) {
            takenPieces.add((Piece) engine.getBoard().getElement(pMove.to));
            handleTakenList();
            remove(pMove.to, null);
            AudioPlayer.playCaptureSound();
            playedTake = true;
        }

        if (!playedTake) AudioPlayer.playMoveSound();

        Move move = new Move(pMove.from, pMove.to, piece.color);
        updateMoveList(move);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        KeyValue kvx = new KeyValue(pieceView.xProperty(), 90 * pMove.to.x);
        KeyValue kvy = new KeyValue(pieceView.yProperty(), 90 * pMove.to.y);

        KeyFrame kfx = new KeyFrame(Duration.millis(100 * Math.abs(pMove.to.x - pMove.from.x)), kvx);
        KeyFrame kfy = new KeyFrame(Duration.millis(100 * Math.abs(pMove.to.y - pMove.from.y)), kvy);
        timeline.getKeyFrames().add(kfx);
        timeline.getKeyFrames().add(kfy);
        timeline.setOnFinished(event -> moveFinished.set(true));
        timeline.play();

        displayFromTo(move);

        pieceView.setX(90 * pMove.to.x);
        pieceView.setY(90 * pMove.to.y);

        pieces.remove(pMove.from);
        pieces.put(pMove.to, pieceView);

        engine.move(pMove.from, pMove.to);
        if (!RuleSet.getInstance().isAiGame || piece.color != playerColor()) {
            updateClickEventToPiece(pMove.to);
        }

        if (RuleSet.getInstance().timer && !(RuleSet.getInstance().gamemode instanceof Fastpaced)) {
            handleTimerUpdate(piece.color);
        }

        if (RuleSet.getInstance().isAiGame && piece.color == playerColor()) {
            AtomicBoolean testing = new AtomicBoolean(true);
            algMoveThreads = new Thread(() -> {
                while (testing.get()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (moveFinished.get() && isRunning && !pausedForPromotion) {
                        testing.set(false);
                        Move computerMove = alg.move(engine.getBoard(), new Move(pMove.from, pMove.to));
                        Platform.runLater(() -> movePiece(computerMove));
                    }
                }
            });
            algMoveThreads.start();
        }

        if (DEVMODE)
            logger.info("Piece: " + piece + "\n\t\t Moved from: " + pMove.from +
                    " to: " + pMove.to);
    }

    @Override
    public void endGame(Piece.Color won, WinReason reason) {
        endGameBase = new StackPane();
        isRunning = false;
        endGameBase.setPrefHeight(90 * 8);
        endGameBase.setPrefWidth(90 * 8);
        endGameBase.setLayoutX(0);
        endGameBase.setLayoutY(0);
        endGameBase.setStyle("-fx-background-color: #2a2a2a");

        Label winLabel = new Label();
        Label winDescriptionLabel = new Label();

        if (won != null) {
            winLabel.setText("The winner is " + (won == Piece.Color.WHITE ? "WHITE" : "BLACK"));
        } else {
            winLabel.setText("DRAW");
        }

        switch (reason) {
            case TIMEOUT -> winDescriptionLabel.setText("Won by timeout");
            case CHECKMATE -> winDescriptionLabel.setText("Won by checkmate");
            case STALEMATE -> winDescriptionLabel.setText("Drawn by stalemate");
            case NOMATERIAL -> winDescriptionLabel.setText("Draw by lack of material");
            case RESIGNITION -> winDescriptionLabel.setText("Won by resignation");
            case DRAW -> winDescriptionLabel.setText("Draw by agreement");
        }

        winLabel.setFont(new Font("Ariel", 70));
        winDescriptionLabel.setFont(new Font("Ariel", 40));
        winLabel.setTextFill(Color.WHITE);
        winDescriptionLabel.setTextFill(Color.WHITE);

        Button backToMenu = new Button();
        backToMenu.setText("Back to menu");
        backToMenu.setFont(new Font("Ariel", 30));
        backToMenu.setTextFill(Color.WHITE);
        backToMenu.setStyle("-fx-background-color: #2a2a2a");

        Button rematch = new Button();
        rematch.setText("Rematch");
        rematch.setFont(new Font("Ariel", 30));
        rematch.setTextFill(Color.WHITE);
        rematch.setStyle("-fx-background-color: #2a2a2a");

        rematch.setOnMousePressed(e -> General.reset(this, engine));

        backToMenu.setOnMousePressed(e -> General.changeScene((Stage) chessBoardPane.getScene().getWindow()));

        VBox labelHolder = new VBox();
        labelHolder.getChildren().addAll(winLabel, winDescriptionLabel, backToMenu, rematch);
        labelHolder.setSpacing(10);
        labelHolder.setAlignment(Pos.CENTER);

        endGameBase.getChildren().add(labelHolder);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> chessBoardPane.getChildren().add(endGameBase));
        });

        thread.start();
        if (DEVMODE)
            logger.info("Game ended with reason: " + winDescriptionLabel + " the winner is: " + winLabel);
    }

    @Override
    public void setCheck(Vector2 checkCoord) {

    }

    @Override
    public void remove(Vector2 pieceToRemove, Piece taken) {
        ImageView piece = pieces.get(pieceToRemove);
        chessBoardPane.getChildren().remove(piece);
        pieces.remove(pieceToRemove);
        if (taken != null) {
            takenPieces.add(taken);
            handleTakenList();
            if (DEVMODE)
                logger.info("Piece added to TAKEN LIST: " + piece.toString());
        }
        if (DEVMODE)
            logger.info("Piece removed at: " + pieceToRemove);
    }

    public void aiPromote(Vector2 to) {
        Piece p = PROMOTIONPIECES.get(0);
        p.color = algColor;
        remove(to, null);
        addPiece(p, to);
        engine.setPiece(to, p);
        pausedForPromotion = false;
    }

    @Override
    public void promote(Piece.Color color, Vector2 to) {
        if (RuleSet.getInstance().isAiGame && color == Piece.Color.BLACK) {
            aiPromote(to);
            return;
        }
        // Prepare the promotion UI base
        promotionUIBase.setPrefWidth(90);
        promotionUIBase.setPrefHeight(90 * 4);
        promotionUIBase.setLayoutX(to.x * 90);
        promotionUIBase.setStyle("-fx-background-color: #2a2a2a");
        DropShadow ds = new DropShadow();
        ds.setColor(Color.color(0,0,0,0.25));
        ds.setOffsetY(4);
        ds.setOffsetX(0);
        ds.setRadius(4);
        promotionUIBase.setEffect(ds);

        // Flip the position of the UI for when the black pawn promotes
        if (color == Piece.Color.WHITE) {
            promotionUIBase.setLayoutY(to.y * 90);
        } else {
            promotionUIBase.setLayoutY((to.y - 3) * 90);
        }

        for (int i = 0; i < 4; i++) {
            Piece p = PROMOTIONPIECES.get(i);
            p.color = color;
            String imagePath = "pieces/" +
                    General.getPieceColorString(p) +
                    "-" +
                    General.getPieceTypeString(p) +
                    ".png";

            Image image = null;
            try {
                image = new Image(Objects.requireNonNull(
                        ImageGrabber.getInstance().getClass().getResource("" + imagePath)).openStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ImageView pieceImage = new ImageView();
            pieceImage.setImage(image);
            pieceImage.setFitHeight(90);
            pieceImage.setFitWidth(90);
            pieceImage.setX(0);
            pieceImage.setY(90 * i);

            // On click, we spawn the correct piece from the list and remove the UI
            pieceImage.setOnMouseClicked(event -> {
                remove(to, null);
                addPiece(promotionList.get(pieceImage), to);
                chessBoardPane.getChildren().remove(promotionUIBase);
                promotionList.clear();
                engine.setPiece(to, p);
                pausedForPromotion = false;
                fastPacedCounter = 0;
                if (DEVMODE)
                    logger.info("Promotion finished at: " + to + " with piece: " + p);
            });

            promotionList.put(pieceImage, p);
            promotionUIBase.getChildren().add(pieceImage);
        }
        chessBoardPane.getChildren().add(promotionUIBase);
        if (DEVMODE)
            logger.info("Promotion dialog prompted at: " + to);
    }

    @Override
    public void addPiece(Piece piece, Vector2 pos) {
        String imagePath = "pieces/" +
                General.getPieceColorString(piece) +
                "-" +
                General.getPieceTypeString(piece) +
                ".png";

        Image image;
        try {
            image = new Image(Objects.requireNonNull(ImageGrabber.getInstance().getClass().getResource(imagePath)).openStream());
        } catch (IOException e) {
            System.out.println("ERROR in ChessController (addPiece): Image not found");
            throw new RuntimeException(e);
        }
        ImageView pieceImage = new ImageView();

        pieceImage.setImage(image);
        pieceImage.setFitHeight(90);
        pieceImage.setFitWidth(90);
        pieceImage.setX(90 * pos.x);
        pieceImage.setY(90 * pos.y);

        pieces.put(pos, pieceImage);

        chessBoardPane.getChildren().add(pieceImage);

        if (!RuleSet.getInstance().isAiGame || piece.color != algColor)
            addClickEventToPiece(pieceImage);
    }

    private static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    EventHandler<MouseEvent> pressHandler = mouseEvent -> {
        Vector2 handlePos = getKeysByValue(pieces, (ImageView) mouseEvent.getSource()).get(0);
        removePosibleMoves();
        displayPosibleMoves(engine.getMoves(handlePos), handlePos);
    };

    private void addClickEventToPiece(ImageView piece) {
        piece.setOnMousePressed(pressHandler);
    }

    private void updateClickEventToPiece(Vector2 pos) {
        ImageView piece = pieces.get(pos);
        addClickEventToPiece(piece);
    }

    public void removePosibleMoves() {
        for (Vector2 move : possibleMoves.keySet()) {
            ImageView imageView = possibleMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        possibleMoves.clear();
    }

    private void displayPosibleMoves(ArrayList<Vector2> moves, Vector2 piece) {
        for (Vector2 move : moves) {
            Image image;
            try {
                image = new Image(Objects.requireNonNull(
                        ImageGrabber.getInstance().getClass().getResource("possibleMove.jpg")).openStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ImageView possibleMoveImage = new ImageView();
            possibleMoveImage.setImage(image);
            possibleMoveImage.setFitHeight(90);
            possibleMoveImage.setFitWidth(90);
            possibleMoveImage.setX(90 * move.x);
            possibleMoveImage.setY(90 * move.y);
            possibleMoveImage.setOpacity(0.5);
            possibleMoveImage.setOnMouseClicked(event -> {
                movePiece(new Move(piece, move));
                removePosibleMoves();
            });
            possibleMoves.put(move, possibleMoveImage);
            chessBoardPane.getChildren().add(possibleMoveImage);
        }
    }

    public void removeFromTo() {
        for (Vector2 move : fromToMoves.keySet()) {
            ImageView imageView = fromToMoves.get(move);
            chessBoardPane.getChildren().remove(imageView);
        }
        fromToMoves.clear();
    }
}
