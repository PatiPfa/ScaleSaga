package com.example.memoryprototyp1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;

import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;
import static com.example.memoryprototyp1.Score.deserializeScore;



public class MainMenuController {


    @FXML
    private TextField tf_player1;
    @FXML
    private TextField tf_player2;

    @FXML
    private Label label_errormessage;
    @FXML
    private AnchorPane mainMenuAP;
    @FXML
    private AnchorPane SubMenuAP;
    @FXML
    private AnchorPane nameInputAP;
    @FXML
    private VBox scoreBoardthreeCards;
    @FXML
    private VBox scoreBoardtwoCards;
    @FXML
    private TextField tf_player1;
    @FXML
    private TextField tf_player2;
    @FXML
    private Slider sliderVolume;
    @FXML
    private Label one1;

    @FXML
    private Label one2;

    @FXML
    private Label one3;

    @FXML
    private Label one4;

    @FXML
    private Label one5;
    @FXML
    private Label two1;

    @FXML
    private Label two2;

    @FXML
    private Label two3;

    @FXML
    private Label two4;

    @FXML
    private Label two5;
    @FXML
    private VBox scoreBoardthreeCards;

    @FXML
    private VBox scoreBoardtwoCards;
    @FXML
    private Button buttonSoundOnOff;
    @FXML
    private void toggleMute(ActionEvent event) {
        Music.MusicPlayer.toggleMute();
        if (Music.MusicPlayer.isMuted) {
            buttonSoundOnOff.setText("OFF");
        } else {
            buttonSoundOnOff.setText("ON");
        }

    }

    @FXML
    private void handleVolumeChange(MouseEvent event) {
        double volume = sliderVolume.getValue();
        Music.MusicPlayer.setBackgroundMusicVolume(volume/100);
        System.out.println("Volume: " + volume);
    }

    private static String player1name;
    private static String player2name;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static boolean singleplayer = false;
    private static String gamemode;

    private static String player1name;
    private static String player2name;
    private static String txtFileTwoCards = "src/main/resources/com/example/memoryprototyp1/score/scoreTwoCards.txt";
    private static String txtFileThreeCards = "src/main/resources/com/example/memoryprototyp1/score/scoreThreeCards.txt";
    private final Image CURSOR = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));

    /*
     * Effekt wenn man über button streicht
     * return: <a target="_blank" href="https://icons8.com/icon/7806/left-2">Left 2</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
     * einstellungen button: <a target="_blank" href="https://icons8.com/icon/2969/einstellungen">Einstellungen</a> Icon von <a target="_blank" href="https://icons8.com">Icons8</a>
     * */

    private static String gamemode;

    public static String getGamemode() {
        return gamemode;
    }

    public static void setSingleplayer(boolean newState) {
        singleplayer = newState;
    }


    public static String getGamemode() {
        return gamemode;
    }
    public static String getPlayer1name() {
        return player1name;
    }

    public static String getPlayer2name() {
        return player2name;
    }

    /**
     * Singleplayer-Button setzt singleplayer auf true und wechselt zur Kartenauswahl.
     **/
    public void singleplayer(){
        playButtonSound();
        singleplayer = true;
        switchToSubmenu();
    }

    /**
     * Multiplayer-Button zur Kartenauswahl. Singleplayer bleibt unverändert auf false
     **/
    public void multiplayer(){
        playButtonSound();
        switchToSubmenu();
        scoreBoardtwoCards.setVisible(false);
        scoreBoardthreeCards.setVisible(false);
    }

    /**
     * wechselt vom Hauptmenü/Modiauswahl (mainMenuAP) zum Submenu/Kartenauswahl (SubMenuAP)
     **/
    public void switchToSubmenu(){
        mainMenuAP.setVisible(false);
        SubMenuAP.setVisible(true);
        scoreDisplay();
    }

    /**
     * wechselt vom Submenu/Kartenauswahl (SubMenuAP) zum Hauptmenü/Modiauswahl (mainMenuAP)
     **/
    public void backButton(){
        mainMenuAP.setVisible(true);
        SubMenuAP.setVisible(false);
    }

    /**
     * wechselt vom Hauptmenü (genauer Submenu) zum jeweiligen Spielmodi welcher über einen String übergeben wird
     * wird von anderen methoden aufgerufen
     **/
    public Stage switchToGame(ActionEvent event, String mode){
        try{                                    //probieren, ob er wechseln kann
            root = FXMLLoader.load(getClass().getResource(mode));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.setCursor(new ImageCursor(CURSOR));
            stage.setScene(scene);
            stage.show();
            return stage;
        }catch (Exception e){                   //Falls der Spielmodi nicht geöffnet werden kann, wird der Fehler in die Logdatei gespeichert
            writeInLog(e, gamemode);
        }
        return null;
    }

    /**
     * Button um zu den Einstellungen zu wechseln
     **/
    public void switchToSetting(ActionEvent event){
        try{                                    //probieren, ob er wechseln kann
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Setting.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){                   //Falls die Einstellungen nicht geöffnet werden kann, wird der Fehler in die Logdatei gespeichert
            writeInLog(e, "Setting");
        }

    }

    /**
     * wechselt (von den Einstellungen) zurück zum Hauptmenü
     **/
    public void switchToMenu(ActionEvent event){
        try {                                   //probieren, ob er wechseln kann
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {                 //Falls die Einstellungen nicht geöffnet werden kann, wird der Fehler in die Logdatei gespeichert
            writeInLog(e, "MainMenu");
        }
    }

    /**
     *Button für 2 Karten Modi
     **/
    public void twoCards(ActionEvent event){
         //*Wenn singleplayer = true (wird im Hauptmenü beim klicken auf den Singleplayer Button gesetzt) ist, wird der Spielmodi
         //auf Singleplayer2Cards gesetzt und die switchToGame Methode aufgerufen um zu wechseln

        if(singleplayer){
            gamemode = "Singleplayer2Cards";
            playButtonSound();
            try{
                Stage stage = switchToGame(event, "Singleplayer_2Cards.fxml");
                stage.setTitle("Singleplayer 2 Cards");
                stage.show();
            }catch (Exception e){
                writeInLog(e, "Singleplayer 2 Cards");
            }

            //Wenn singleplayer false ist (standardmäßig) wird der gamemode auf Multiplayer2Cards gesetzt und die switchMainToNames Methode aufgrufen
            //womit die Namenseingabe aufgerufen wird
        }else{
            gamemode = "Multiplayer2Cards";
            playButtonSound();
            switchMainToNames();
        }
    }

    /**
     * Button für 3 Karten Modi
     **/
    public void threeCards(ActionEvent event){
        //*Wenn singleplayer = true (wird im Hauptmenü beim klicken auf den Singleplayer Button gesetzt) ist, wird der Spielmodi
        //auf Singleplayer3Cards gesetzt und die switchToGame Methode aufgerufen um zu wechseln
        if(singleplayer){
            gamemode = "Singleplayer3Cards";
            playButtonSound();
            try{
                Stage stage = switchToGame(event, "Singleplayer_3Cards.fxml");
                stage.setTitle("Singleplayer 3 Cards");
                stage.show();
            }catch (Exception e){
                writeInLog(e, "Singleplayer 3 Cards");
            }

            //Wenn singleplayer false ist (standardmäßig) wird der gamemode auf Multiplayer3cards gesetzt und die switchMainToNames Methode aufgrufen
            //womit die Namenseingabe aufgerufen wird
        }else{
            gamemode = "Multiplayer3Cards";
            playButtonSound();
            switchMainToNames();
        }
    }

    /**
     * wechselt vom Submenü/Kartenauswahl zur Namenseingabe und umgekehrt
     **/
    public void switchMainToNames(){
        //Das AP wird auf den umgekehrten Visible Status gesetzt.
        //zB. !SubMenuAP.isVisible() gibt false zurück, dann wird das SubmenuAp aufgrund der Negation mit setVisible auf true gesetzt
        SubMenuAP.setVisible(!SubMenuAP.isVisible());
        nameInputAP.setVisible(!nameInputAP.isVisible());
        label_errormessage.setText(" ");
    }

    /**
     * Game schließen Button
     **/
    public void closeGame(){
        //neues Alert des Type Warning erstellen und entsprechend beschreiben
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Quit game");
        alert.setHeaderText("Do you really want to quit?");

        //erstelle 2 Button mit Yes und No und hänge sie an das Alert an
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);

        //wartet auf das Ergebnis des Alerts
        java.util.Optional<ButtonType> result = alert.showAndWait();

        ///Wenn es ein Ergebnis gibt und das Ergebnis Yes ist, wird das Spiel geschlossen
        //ansonsten wird das Alert geschlossen.
        if (result.isPresent() && result.get() == yes) {
            Platform.exit();
        }
    }

    /**
     * Button um die Namenseingabe zu bestätigen
     **/
    public void submitNames(ActionEvent event){

        //speichert die Eingabe der Textfelder in eine Variable
        player1name = tf_player1.getText();
        player2name = tf_player2.getText();

        //Überprüft die Namenslänge sowie ob die Namen gleich sind. Falls es nicht passt, wird ein Fehler ausgegeben und zurückgekehrt
        if(player1name.length() < 3 || player2name.length() < 3){
            label_errormessage.setText("All names must contain at least 3 characters!");
            return;
        }else if(player1name.length() > 10 || player2name.length() > 10) {
            label_errormessage.setText("The name may consist of a maximum of 10 characters!");
            return;
        } else if(player1name.equals(player2name)){
            label_errormessage.setText("The names should not be the same!");
            return;
        }

        //Je nach gewähltem Spielmodi wird dieser aufgerufen
        if(gamemode.equals("Multiplayer2Cards")){
            playButtonSound();
            try{
                Stage stage = switchToGame(event, "MultiplayerForTwo_2Cards.fxml");
                stage.setTitle("Multiplayer 2 Cards");
                stage.show();
            }catch (Exception e){
                writeInLog(e, "Multiplayer 2 Cards");
            }
        }else{
            playButtonSound();
            try{
                Stage stage = switchToGame(event, "MultiplayerForTwo_3Cards.fxml");
                stage.setTitle("Multiplayer 3 Cards");
                stage.show();
            }catch (Exception e){
                writeInLog(e, "Multiplayer 3 Cards");
            }

        }
    }

    //Speichert die Highscore namen in die Labels
    public void scoreDisplay() {

        setScoreLabel(one1, 0, txtFileTwoCards);
        setScoreLabel(one2, 1, txtFileTwoCards);
        setScoreLabel(one3, 2, txtFileTwoCards);
        setScoreLabel(one4, 3, txtFileTwoCards);
        setScoreLabel(one5, 4, txtFileTwoCards);

        setScoreLabel(two1, 0, txtFileThreeCards);
        setScoreLabel(two2, 1, txtFileThreeCards);
        setScoreLabel(two3, 2, txtFileThreeCards);
        setScoreLabel(two4, 3, txtFileThreeCards);
        setScoreLabel(two5, 4, txtFileThreeCards);
    }

    /**
     * Button um von der Namenseingabe zurück zu wechseln
     **/
    public void returnFromNames(){
        //setzt Textfelder und labels zurück und wechselt mit der switchMainToName Methode zum Hauptmenü zurück
        playButtonSound();
        tf_player1.setText("Player 1");
        tf_player2.setText("Player 2");
        label_errormessage.setText(" ");

        switchMainToNames();
    }

    /**
     * schreibt Fehlermeldungen in eine log Datei
     **/
    //The following method has been copied from ChatGPT: https://chat.openai.com/share/62ddc123-fe7c-4f0d-9e13-9c5c91bab0a5 (20.01.2024)
    public static void writeInLog(Exception e, String Fehlerseite) {
        //hier wird probiert ein neuer PrintWriter zu erstellen
        //dieser schreibt in das file test.log. Der Parameter true sagt, dass der Inhalt angehängt wird und nicht den alten überschreibt
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.log", true))) {
            //mit dem simple Date Formater wird eine Datumsvorlage erstellt und mit dem timestamp ein Zeitstemmpel in dem Format in eine Variable gespeichert
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            //der aktuelle Zeitstempel sowie die Info um welche Fehlerseite es sich handelt werden zusammen mit der Fehlermeldung und einer Leerzeile in die log datei geschrieben
            writer.println("["+ timestamp + "] Fehler beim Wechseln zur " + Fehlerseite + "-Seite:");
            e.printStackTrace(writer);
            writer.println();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    @FXML
    private void toggleMute(ActionEvent event) {
        Music.MusicPlayer.toggleMute();
    }
    @FXML
    private void handleVolumeChange(MouseEvent event) {
        double volume = sliderVolume.getValue();
        Music.MusicPlayer.setBackgroundMusicVolume(volume);
    }

    private void setScoreLabel(Label l, int pos, String path) {
        Score[] scores = readHighscore(path);

        scores[pos] = readHighscore(path)[pos];
        if (scores[pos] != null && scores[pos].getScoreSec() < 10) {
            l.setText(scores[pos].getScoreMin() + ":0" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        } else if (scores[pos] != null) {
            l.setText(scores[pos].getScoreMin() + ":" + scores[pos].getScoreSec() + " | " + scores[pos].getPlayerName());
        }

    }

    public static Score[] readHighscore(String path) {
        Score[] out = new Score[5];


        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            out = (Score[]) in.readObject();
        } catch (Exception e){
            System.out.println("Array is empty why");
        }

        return out;
    }
}


