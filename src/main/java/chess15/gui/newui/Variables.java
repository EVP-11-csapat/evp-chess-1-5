package chess15.gui.newui;

import chess15.algorithm.AlgorithmInterface;
import chess15.board.*;
import chess15.engine.EngineInterface;
import chess15.gui.controllers.NewChessController;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Variables {
    public static final int FASTPACEDTIMEOUT = 2000;
    public static final boolean DEVMODE = true;
    public static final Logger logger = Logger.getLogger(NewChessController.class.getName());
    public static FileHandler logFileHandler;

    public static HashMap<Vector2, ImageView> pieces = new HashMap<>();
    public static HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();
    public static HashMap<Vector2, ImageView> fromToMoves = new HashMap<>();
    public static ArrayList<Piece> takenPieces = new ArrayList<>();
    public static HashMap<Piece, ImageView> takenList = new HashMap<>();
    public static HashMap<ImageView, Piece> promotionList = new HashMap<>();
    public static final ArrayList<Piece> PROMOTIONPIECES = new ArrayList<>(List.of(
            new Piece(Piece.Color.WHITE, Piece.Type.QUEEN, Queen.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.ROOK, Rook.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.BISHOP, Bishop.getInstance(), false),
            new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT, Knight.getInstance(), false)
    ));

    public static StackPane endGameBase = new StackPane();
    public static Pane promotionUIBase = new Pane();

    public static boolean whiteToMove = true;

    public static boolean isRunning = true;
    public static boolean pausedForPromotion = false;
    public static boolean pausedForSetup = true;


    public static EngineInterface engine;
    public static AlgorithmInterface alg;

    public static Piece.Color algColor = Piece.Color.BLACK;

    public static Thread timerThread;
    public static Thread algMoveThreads;

    public static Label whiteTimerLabel;
    public static Label blackTimerLabel;
    public static Font timerFont  = Font.font(Font.getDefault().getName(), FontWeight.BOLD, 64);
    public static long whiteTimeInMillis;
    public static long blackTimeInMillis;
    public static int fastPacedCounter;
    public static boolean timerWhiteSide = true;

    public static ScrollPane blackTakenScroll;
    public static ScrollPane whiteTakenScroll;
    public static Pane blackTaken;
    public static Pane whiteTaken;

    public static TableView<MoveRepr> moveTable = new TableView<MoveRepr>();
    public static ListView<String> chatPrevList;
    public static TextField userInput;

}
