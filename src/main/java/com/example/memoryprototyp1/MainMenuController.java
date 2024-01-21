package com.example.memoryprototyp1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javafx.stage.Stage;
import javafx.scene.Parent;

import static com.example.memoryprototyp1.Music.MusicPlayer.playButtonSound;



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
    private Slider sliderVolume;



    private static String player1name;
    private static String player2name;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static boolean singleplayer = false;
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

    public static String getPlayer1name() {
        return player1name;
    }

    public static String getPlayer2name() {
        return player2name;
    }

    public void singleplayer(){
        playButtonSound();
        singleplayer = true;

        switchToSubmenu();
    }

    public void multiplayer(){
        playButtonSound();
        switchToSubmenu();
    }

    public void switchToSubmenu(){
        mainMenuAP.setVisible(false);
        SubMenuAP.setVisible(true);
    }

    public void backButton(){
        mainMenuAP.setVisible(true);
        SubMenuAP.setVisible(false);
    }

    public Stage switchToGame(ActionEvent event, String mode){
        try{
            root = FXMLLoader.load(getClass().getResource(mode));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.setCursor(new ImageCursor(CURSOR));
            stage.setScene(scene);
            stage.show();
            return stage;
        }catch (Exception e){
            writeInLog(e, gamemode);
        }
        return null;
    }
    public void switchToSetting(ActionEvent event){
        try{
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Setting.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            writeInLog(e, "Setting");
        }

    }


    public void switchToMenu(ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            writeInLog(e, "MainMenu");
        }
    }

    public void twoCards(ActionEvent event){
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

        }else{
            gamemode = "Multiplayer2Cards";
            playButtonSound();
            switchMainToNames();
        }
    }

    public void threeCards(ActionEvent event){
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

        }else{
            gamemode = "Multiplayer3Cards";
            playButtonSound();
            switchMainToNames();
        }
    }


    public void switchMainToNames(){
        SubMenuAP.setVisible(!SubMenuAP.isVisible());
        nameInputAP.setVisible(!nameInputAP.isVisible());
        label_errormessage.setText(" ");
    }

    public void closeGame(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Quit game");
        alert.setHeaderText("Do you really want to quit?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            Platform.exit();
        }
    }

    public void submitNames(ActionEvent event){
        player1name = tf_player1.getText();
        player2name = tf_player2.getText();

        //TODO:  schauen ob die Namenslängen passen
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

        //Testzwecke
        System.out.println(player1name + " " + player2name);

        //TODO: ev überlegen das mit dem szenen laden und den buttons noch anders zu machen..
        //vorallem weil das jz hier nicht so schön ausschaut
        if(gamemode.equals("Multiplayer2Cards")){

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

    public void returnFromNames(){
        playButtonSound();
        tf_player1.setText("Player 1");
        tf_player2.setText("Player 2");
        label_errormessage.setText(" ");

        switchMainToNames();
    }


    //The following method has been copied from ChatGPT: https://chat.openai.com/share/62ddc123-fe7c-4f0d-9e13-9c5c91bab0a5 (20.01.2024
    private void writeInLog(Exception e, String Fehlerseite) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.log", true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

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

}
