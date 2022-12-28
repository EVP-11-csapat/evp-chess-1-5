package chess15.gui.util;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.gui.controllers.ChessController;
import chess15.util.Move;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Stores all the constants that we use
 */
public class Constants {
    // Timer Variables
    public static final int FASTPACEDTIMEOUT = 2000;
    public static HBox timerHBox;
    public static Label whiteTimerLabel;
    public static Label blackTimerLabel;
    public static Pane whiteTimerBox;
    public static Pane blackTimerBox;
    public static boolean whiteTimeRanOut = false;
    public static boolean blackTimeRanOut = false;
    public static Thread timerThread;
    public static boolean whiteSide;
    public static boolean isRunning;
    public static long whiteTimeInMillis;
    public static long blackTimeInMillis;
    public static int fastPacedCounter;

    // Chess Variables
    public static Thread algMoveThreads;
    public static HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();
    public static HashMap<Vector2, ImageView> fromToMoves = new HashMap<>();
    public static HashMap<Vector2, ImageView> pieces = new HashMap<>();
    public static ArrayList<Piece> takenPieces = new ArrayList<>();
    public static HashMap<Piece, ImageView> takenList = new HashMap<>();
    public static HashMap<ImageView, Piece> promotionList = new HashMap<>();
    public static Board board = null;
    public static Pane whiteTaken;
    public static Pane blackTaken;
    public static Pane promotionUIBase = new Pane();
    public static StackPane endGameBase = new StackPane();
    public static ArrayList<Move> playedMoves = new ArrayList<>();
    public static boolean pauseForPromotion = false;

    // DevMode
    public static final boolean DEVMODE = true;
    public static final Logger logger = Logger.getLogger(ChessController.class.getName());
    public static FileHandler logFileHandler;
}
