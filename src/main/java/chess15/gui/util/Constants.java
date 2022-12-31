package chess15.gui.util;

import chess15.Board;
import chess15.Piece;
import chess15.Vector2;
import chess15.algorithm.AlgorithmInterface;
import chess15.gui.controllers.ChessController;
import chess15.util.Move;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Stores all the constants that we use
 */
public class Constants {
    // ###################
    // # Timer Variables #
    // ###################
    /**
     * The time in milliseconds for the random move clock
     */
    public static final int FASTPACEDTIMEOUT = 2000;
    /**
     * Timer Hbox container
     */
    public static HBox timerHBox;
    /**
     * The white side timer
     */
    public static Label whiteTimerLabel;
    /**
     * The black side timer
     */
    public static Label blackTimerLabel;
    /**
     * The white side timer background
     */
    public static Pane whiteTimerBox;
    /**
     * The black side timer background
     */
    public static Pane blackTimerBox;
    /**
     * True if the white side timer ran down to 0
     */
    public static boolean whiteTimeRanOut = false;
    /**
     * True if the black side timer ran down to 0
     */
    public static boolean blackTimeRanOut = false;
    /**
     * Timer theread for thread maintanence
     */
    public static Thread timerThread;
    /**
     * Used to determine if the white player is playing or the black
     */
    public static boolean whiteSide;
    /**
     * If true the timer is running
     */
    public static boolean isRunning;
    /**
     * White side remaining time in milliseconds
     */
    public static long whiteTimeInMillis;
    /**
     * Black side remaining time in milliseconds
     */
    public static long blackTimeInMillis;
    /**
     * Fast-paced timer counter
     */
    public static int fastPacedCounter;

    // ###################
    // # Chess Variables #
    // ###################

    /**
     * Chess Algorithm thread for thread maintanence
     */
    public static Thread algMoveThreads;
    /**
     * A hashmap holding all the possible move indicators
     */
    public static HashMap<Vector2, ImageView> possibleMoves = new HashMap<>();
    /**
     * A hashmap holding the from and to move indicators
     */
    public static HashMap<Vector2, ImageView> fromToMoves = new HashMap<>();
    /**
     * A hashmap containing all the pieces
     */
    public static HashMap<Vector2, ImageView> pieces = new HashMap<>();
    /**
     * A list of all the captured pieces
     */
    public static ArrayList<Piece> takenPieces = new ArrayList<>();
    /**
     * A hashmap containing the taken pieces
     */
    public static HashMap<Piece, ImageView> takenList = new HashMap<>();
    /**
     * A hashmap containing all the promotion candidate pieces
     */
    public static HashMap<ImageView, Piece> promotionList = new HashMap<>();
    /**
     * The board reference used for setup
     */
    public static Board board = null;
    /**
     * The white taken pieces background
     */
    public static Pane whiteTaken;
    /**
     * The black taken pieces background
     */
    public static Pane blackTaken;
    /**
     * The promotion background
     */
    public static Pane promotionUIBase = new Pane();
    /**
     * The end game screen background
     */
    public static StackPane endGameBase = new StackPane();
    /**
     * A list of the played moves
     */
    public static ArrayList<Move> playedMoves = new ArrayList<>();
    /**
     * True if the algorithm thread is waiting for the user to pick a promotion piece
     */
    public static boolean pauseForPromotion = false;
    /**
     * A reference to the Chess Algorithm
     */
    public static AlgorithmInterface alg;
    /**
     * True if the white side is next to move
     */
    public static boolean whiteToMove = true;
    /**
     * Reference to what color the chess algorithm is playing
     */
    public static Piece.Color AlgColor = Piece.Color.BLACK;

    // ###########
    // # DevMode #
    // ###########
    /**
     * If true, all the developer specific console prints, and log file are activated
     */
    public static final boolean DEVMODE = true;
    /**
     * A logger to create log files
     */
    public static final Logger logger = Logger.getLogger(ChessController.class.getName());
    /**
     * A File handler for the log file
     */
    public static FileHandler logFileHandler;
}
