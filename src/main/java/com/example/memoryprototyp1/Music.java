package com.example.memoryprototyp1;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class Music {


    public static class MusicPlayer {

        private static MediaPlayer backgroundMusic;

        public static void playBackgroundMusic(Media media) {
            if (backgroundMusic != null) {
                backgroundMusic.setMute(true);
            }
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setOnEndOfMedia(() -> playBackgroundMusic(backgroundMusic.getMedia()));
            backgroundMusic.setVolume(0.5);
            backgroundMusic.play();
        }


    }

}
