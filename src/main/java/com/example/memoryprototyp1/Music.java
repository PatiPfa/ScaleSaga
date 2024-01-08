package com.example.memoryprototyp1;
import javafx.scene.media.AudioClip;
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
            backgroundMusic.setVolume(0.4);
            backgroundMusic.play();
        }
    }
    public static void playButtonSound(){
        String soundPath = "/com/example/memoryprototyp1/sounds/buttonSound.mp3";
        Media sound = new Media(Music.class.getResource(soundPath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.play();
    }
}