package com.example.memoryprototyp1;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;

import static com.example.memoryprototyp1.Music.playButtonSound;


public class MainMenuController {

    @FXML
    private Button btn_singleplayer;
    @FXML
    private Button btn_multiplayer;
    @FXML
    private Button btn_2cards;
    @FXML
    private Button btn_3cards;
    @FXML
    private Button btn_return;
    @FXML
    private Button btn_submitNames;

    @FXML
    private TextField tf_player1;
    @FXML
    private TextField tf_player2;

    @FXML
    private Label label_errormessage = new Label(" ");
    @FXML
    private AnchorPane mainMenuAP;
    @FXML
    private AnchorPane nameInputAP;



    private static String player1name;
    private static String player2name;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static boolean singleplayer = false;
    private Image curser = new Image(Objects.requireNonNull(Card.class.getResourceAsStream("images/sword.png")));

    private static int gamemode = 0;
    //1: Singleplayer 2 Cards, 2: Singleplayer 3 Cards
    //3: Multiplayer 2 Cards, 4: Multiplayer 3 Cards
    public static int getGamemode() {
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
        btnVisible();
    }

    public void multiplayer(){
        playButtonSound();
        btnVisible();
    }

    public void btnVisible(){
        btn_singleplayer.setVisible(!btn_singleplayer.isVisible());
        btn_multiplayer.setVisible(!btn_multiplayer.isVisible());
        btn_2cards.setVisible(!btn_2cards.isVisible());
        btn_3cards.setVisible(!btn_3cards.isVisible());
        btn_return.setVisible(!btn_return.isVisible());
    }

    public Stage switchToGame(ActionEvent event, String mode) throws IOException{
        root = FXMLLoader.load(getClass().getResource(mode));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setCursor(new ImageCursor(curser));
        stage.setScene(scene);
        stage.show();
        return stage;
    }

    public void twoCards(ActionEvent event) throws IOException{
        if(singleplayer){
            gamemode= 1;
            playButtonSound();
            Stage stage = switchToGame(event, "Singleplayer_2Cards.fxml");
            stage.setTitle("Singleplayer 2 Cards");
            stage.show();
        }else{
            gamemode = 3;
            playButtonSound();
            switchMainToNames();
        }
    }

    public void threeCards(ActionEvent event) throws IOException{
        if(singleplayer){
            gamemode = 2;
            playButtonSound();
            Stage stage = switchToGame(event, "Singleplayer_3Cards.fxml");
            stage.setTitle("Singleplayer 3 Cards");
            stage.show();
        }else{
            gamemode = 4;
            playButtonSound();
            switchMainToNames();
        }
    }

    public void returnbtn(){
        singleplayer = false;
        btnVisible();
    }

    public void switchMainToNames(){
        mainMenuAP.setVisible(!mainMenuAP.isVisible());
        nameInputAP.setVisible(!nameInputAP.isVisible());
        label_errormessage.setText(" ");
    }

    public void submitNames(ActionEvent event) throws IOException{
        //TODO: ev auch Farbe einlesen, falls wir das mit der Tastatur umsetzen
        //TODO: standartmäßig Player 1 und Player 2 drinnen lassen oder weglassen?
        boolean correctInput = false;
        player1name = tf_player1.getText();
        player2name = tf_player2.getText();
        //TODO:  schauen ob die Namenslängen passen
        if(player1name.length() < 3 || player2name.length() < 3){
            label_errormessage.setText("All names must contain at least 3 character!");
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
        if(gamemode == 3){
            playButtonSound();
            Stage stage = switchToGame(event, "MultiplayerForTwo_2Cards.fxml");
            stage.setTitle("Multiplayer 2 Cards");
            stage.show();
        }else{
            playButtonSound();
            Stage stage = switchToGame(event, "MultiplayerForTwo_3Cards.fxml");
            stage.setTitle("Multiplayer 3 Cards");
            stage.show();
        }

    }

    public void returnFromNames(){
        tf_player1.setText("Player 1");
        tf_player2.setText("Player 2");
        label_errormessage.setText(" ");

        switchMainToNames();
        playButtonSound();
    }
}
