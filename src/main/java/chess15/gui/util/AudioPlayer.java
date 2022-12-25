package chess15.gui.util;

import chess15.gui.sounds.SoundGrabber;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class AudioPlayer {
    public static void playMoveSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("step.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }

    public static void playCaptureSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("take.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }

    public static void playStartSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("start.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }
}
