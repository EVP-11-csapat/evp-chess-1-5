package chess15.gui.scenes;

public class ResourceGrabber {
    private static ResourceGrabber instance;

    public static ResourceGrabber getInstance() {
        if (instance == null) instance = new ResourceGrabber();
        return instance;
    }
}
