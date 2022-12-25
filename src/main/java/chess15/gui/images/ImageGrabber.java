package chess15.gui.images;

public class ImageGrabber {
    private static ImageGrabber instance = null;

    private ImageGrabber() {
    }

    public static ImageGrabber getInstance() {
        if (instance == null) {
            instance = new ImageGrabber();
        }
        return instance;
    }
}
