package Interfaces;


import Model.User;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class loginController
{

    Socket connectionSocket ;
    String username ;
    ObjectOutputStream oos;
    ObjectInputStream ois ;


    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXCheckBox showPassCheckBox;

    @FXML
    private JFXTextField passTextField;
    @FXML
    private StackPane rootStackPane ;



    @FXML
    void SignIn(ActionEvent event)
    {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(usernameTextField, Validator.createEmptyValidator("Username is Required")) ;
        validationSupport.registerValidator(passwordTextField, Validator.createEmptyValidator("password is Required")) ;


        //Tester les champs
        if (!usernameTextField.getText().isEmpty()&
                !passwordTextField.getText().isEmpty() )
        {
            try
            {
                connectionSocket =new Socket("10.42.0.1" ,9191) ;


                OutputStream os = connectionSocket.getOutputStream() ;
                InputStream is = connectionSocket.getInputStream() ;
                BufferedReader bf =new BufferedReader(new InputStreamReader(is)) ;

                PrintWriter pw =new PrintWriter(os,true) ;
                oos = new ObjectOutputStream(os);
                ois = new ObjectInputStream(is);


                String monId = bf.readLine() ;//Read num

                /*------------------Creat New User ---------------*/
                username =usernameTextField.getText() ;
                String password =passTextField.getText();
                /*------------------------------------------------------*/

                User u = new User(username,null,password,null,0,null) ;


                pw.println(2);//Send choix ==> 2 :Login | ==> 1 : SignUp
                oos.writeObject(u);//Send User


                String reponse = bf.readLine() ;

                //Si le serveur répond par true ==> les données entrer correct
                if (reponse.equals("true"))
                {
                    loadMainInterface();//Affichier le prochain interface
                    System.out.println("load main");
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Wrong username or password !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    connectionSocket.close();
                }

            }
            catch (IOException | InterruptedException | ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    /*---------------------------------------------------------------------------------------------------*/
    public void  loadMainInterface () throws IOException, InterruptedException, ClassNotFoundException
    {
        String username = usernameTextField.getText() ;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../FXML/Main.fxml"));
        MainController mainController = new MainController() ;

        //En passe les info d'utlisateur vers le main interface
        mainController.getInfoConnexion(connectionSocket ,username,oos,ois);

        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage() ;
        stage.setScene(new Scene(root1));
        stage.show();

    }
    /*---------------------------------------------------------------------------------------------------*/
    public void showPassword()
    {
        if (showPassCheckBox.isSelected())
        {
            if (!passwordTextField.getText().isEmpty())
            {
                passTextField.setText(passwordTextField.getText());
            }
            passTextField.setVisible(true);
            passwordTextField.setVisible(false);
        }
        else
        {
            if (!passTextField.getText().isEmpty())
            {
                passwordTextField.setText(passTextField.getText());
            }

            passwordTextField.setVisible(true);
            passTextField.setVisible(false);
        }

    }

}
