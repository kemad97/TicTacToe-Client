/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.soundManager;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author ayaah
 */
public class SoundManager {
    
    private static MediaPlayer backgroundMusicPlayer;

    public static void playBackgroundMusic() {
        if (backgroundMusicPlayer == null) {
            URL resource = SoundManager.class.getResource("/media/sound/backgroundMusic.mp3");
            if (resource == null) {
                System.err.println("Sound file not found!");
                return;
            }
            Media media = new Media(resource.toString());
            backgroundMusicPlayer = new MediaPlayer(media);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
        }
        backgroundMusicPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    public static void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    public static void resumeBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.seek(Duration.ZERO); 
            backgroundMusicPlayer.play();
        }
    }
}
