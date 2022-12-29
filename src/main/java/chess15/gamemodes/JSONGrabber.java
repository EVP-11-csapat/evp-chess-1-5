package chess15.gamemodes;

/**
 * Just a class to use getResource on for standalone build
 */
public class JSONGrabber {
    private static JSONGrabber instance;

    public static JSONGrabber getInstance() {
        if (instance == null) instance = new JSONGrabber();
        return instance;
    }
}
