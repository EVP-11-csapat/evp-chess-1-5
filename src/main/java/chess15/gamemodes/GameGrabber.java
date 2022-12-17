package chess15.gamemodes;

public class GameGrabber {
    private static GameGrabber instance;

    public static GameGrabber getInstance() {
        if (instance == null) instance = new GameGrabber();
        return instance;
    }
}
