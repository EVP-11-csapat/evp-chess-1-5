package chess15.gui.sounds;

/**
 * Just a class to use getResource on for standalone build
 */
public class SoundGrabber {
    private static SoundGrabber instance = null;

    public static SoundGrabber getInstance() {
        if (instance == null) {
            instance = new SoundGrabber();
        }
        return instance;
    }
}
