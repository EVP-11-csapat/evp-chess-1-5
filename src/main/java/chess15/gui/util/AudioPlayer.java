package chess15.gui.util;

import chess15.gui.sounds.SoundGrabber;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * Audio Player class that creates and plays audio
 * Sounds are from <a href="https://lichess.org/">Lichess</a>
 */
public class AudioPlayer {
    /**
     * Play the step sound when a piece moves
     */
    public static void playMoveSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("step.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }

    /**
     * Play the take sound when a piece takes another piece
     */
    public static void playCaptureSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("take.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }

    /**
     * Play the start sound when the game starts
     */
    public static void playStartSound() {
        Media audio = new Media(Objects.requireNonNull(SoundGrabber.getInstance().getClass().getResource("start.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(audio);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.play();
    }
}
