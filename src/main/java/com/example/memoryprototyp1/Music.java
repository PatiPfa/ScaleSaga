package com.example.memoryprototyp1;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;


public class Music {


    public static class MusicPlayer {

        private static MediaPlayer backgroundMusic;
        private static boolean isMuted;

        public static void playBackgroundMusic(Media media) {
            if (backgroundMusic != null) {
                backgroundMusic.stop();

            }
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setOnEndOfMedia(() -> playBackgroundMusic(backgroundMusic.getMedia()));
            backgroundMusic.setVolume(0.4);
            backgroundMusic.play();
        }

        public static void setBackgroundMusicVolume(double volume) {
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(volume);
            }
        }

        public static void toggleMute() {
            isMuted = !isMuted;

            if (isMuted) {
                setBackgroundMusicVolume(0.0);
            } else {
                setBackgroundMusicVolume(0.4);
                
            }
        }

        public static void playButtonSound() {
            String soundPath = "/com/example/memoryprototyp1/sounds/buttonSound.mp3";
            Media sound = new Media(Objects.requireNonNull(Music.class.getResource(soundPath)).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            if(!isMuted) {
                mediaPlayer.setVolume(0.5);
                mediaPlayer.play();

            }



        }
    }
}