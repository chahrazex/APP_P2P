package Interfaces;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private VBox vbox;
    private Parent fxml;

    /*-----------------------------------------------------------------------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() +372);
        t.play();
        t.setOnFinished((e) ->{
            try
            {
                fxml = FXMLLoader.load(getClass().getResource("../FXML/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        });

    }
    /*---------------------------------------------Load SignUp fxml file ----------------------------*/
    @FXML
    private void open_signin()
    {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.6), vbox);
        t.setToX(vbox.getLayoutX() +372);
        t.play();
        t.setOnFinished((e) ->{
            try
            {
                fxml = FXMLLoader.load(getClass().getResource("../FXML/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        });
    }
    /*---------------------------------------------Load SignUp fxml file ----------------------------*/
    @FXML
    private void open_signup()
    {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.6), vbox);
        t.setToX(3);
        t.play();
        t.setOnFinished((e) ->{
            try
            {
                fxml = FXMLLoader.load(getClass().getResource("../FXML/SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        });
    }
    /*------------------------------------------------------------------------------------------------------*/
}
