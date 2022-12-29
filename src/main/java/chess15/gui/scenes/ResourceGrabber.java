package chess15.gui.scenes;

/**
 * Just a class to use getResource on for standalone build
 */
public class ResourceGrabber {
    private static ResourceGrabber instance;

    public static ResourceGrabber getInstance() {
        if (instance == null) instance = new ResourceGrabber();
        return instance;
    }
}
