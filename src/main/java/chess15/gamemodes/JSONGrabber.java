package chess15.gamemodes;

public class JSONGrabber {
    private static JSONGrabber instance;

    public static JSONGrabber getInstance() {
        if (instance == null) instance = new JSONGrabber();
        return instance;
    }
}
