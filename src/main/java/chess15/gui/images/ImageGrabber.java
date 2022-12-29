package chess15.gui.images;

/**
 * Just a class to use getResource on for standalone build
 */
public class ImageGrabber {
    private static ImageGrabber instance = null;

    public static ImageGrabber getInstance() {
        if (instance == null) {
            instance = new ImageGrabber();
        }
        return instance;
    }
}
