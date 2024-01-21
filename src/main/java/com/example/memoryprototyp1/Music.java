package com.example.memoryprototyp1;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;


public class Music {


    public static class MusicPlayer {

        private static double volumeBackground = 0.05;
        private static double mute = 0.0;

        private static MediaPlayer backgroundMusic;
        static boolean isMuted;
        //Hintergrundmusik ab und setzt eine Endlos-Wiedergabe-Schleife.
        public static void playBackgroundMusic(Media media) {
            if (backgroundMusic != null) {
                backgroundMusic.stop();

            }
            backgroundMusic = new MediaPlayer(media);
            // Endlos-Wiedergabe-Schleife
            backgroundMusic.setOnEndOfMedia(() -> playBackgroundMusic(backgroundMusic.getMedia()));
            backgroundMusic.setVolume(volumeBackground);
            backgroundMusic.play();
        }
        //Lautstärke der Hintergrundmusik
        public static void setBackgroundMusicVolume(double volume) {
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(volume);
                volumeBackground = volume;
            }
        }
        //Schaltet die Hintergrundmusik stumm oder an
        public static void toggleMute() {
            isMuted = !isMuted;

            if (isMuted) {
                setBackgroundMusicVolume(mute);
            } else {
                setBackgroundMusicVolume(0.4);
                
            }
        }
        //Spielt den Ton für den Tastendruck ab, wenn nicht stummgeschaltet.
        public static void playButtonSound() {
            String soundPath = "/com/example/memoryprototyp1/sounds/buttonSound.mp3";
            Media sound = new Media(Objects.requireNonNull(Music.class.getResource(soundPath)).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            if(!isMuted) {
                mediaPlayer.setVolume(0.5);
                mediaPlayer.play();
            }else {
                mediaPlayer.setVolume(mute);
            }



        }
    }
}