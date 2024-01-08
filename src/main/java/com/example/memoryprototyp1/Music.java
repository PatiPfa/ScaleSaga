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
            backgroundMusic.setVolume(0.2);
            backgroundMusic.play();
        }
    }
    public static void playButtonSound(){
        String soundPath ="/com/example/memoryprototyp1/sounds/buttonSound.mp3";
        AudioClip buttonClick = new AudioClip(soundPath);
        buttonClick.setVolume(0.5);
        buttonClick.play();
    }
}