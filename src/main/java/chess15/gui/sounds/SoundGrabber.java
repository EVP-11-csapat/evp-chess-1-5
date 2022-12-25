package chess15.gui.sounds;

public class SoundGrabber {
    private static SoundGrabber instance = null;

    private SoundGrabber() {
    }

    public static SoundGrabber getInstance() {
        if (instance == null) {
            instance = new SoundGrabber();
        }
        return instance;
    }
}
