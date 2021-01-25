package Interfaces;

import Model.User;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;

public class signUpController
{

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXTextField passTextField;

    @FXML
    private JFXCheckBox showPassCheckBox;
    @FXML
    private StackPane rootStackPane;

    private TextField portTextField;
    JFXSnackbar snackbar ;

    public InetAddress getIpAdresse() throws SocketException
    {
        Enumeration en = NetworkInterface.getNetworkInterfaces() ;
        while (en.hasMoreElements())
        {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses();en2.hasMoreElements();)
            {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress())
                {
                    if (addr instanceof Inet4Address)
                    {
                        return addr ;
                    }
                }
            }
        }
        return null ;
    }
    @FXML
    void SignUp()
    {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(usernameTextField, Validator.createEmptyValidator("Username is Required")) ;
        validationSupport.registerValidator(emailTextField, Validator.createEmptyValidator("Email is Required")) ;
        validationSupport.registerValidator(passwordTextField, Validator.createEmptyValidator("password is Required")) ;


        if (!usernameTextField.getText().isEmpty()&
                !passwordTextField.getText().isEmpty()&
                   !emailTextField.getText().isEmpty() )
        {
            try
            {

                Socket s =new Socket("192.168.43.119" ,9191) ;

                InputStream is =s.getInputStream();
                InputStreamReader isr =new InputStreamReader(is) ;
                BufferedReader bf =new BufferedReader(isr) ;
                ObjectInputStream ois = new ObjectInputStream(is);

                OutputStream os =s.getOutputStream() ;
                PrintWriter pw =new PrintWriter(os,true) ;
                ObjectOutputStream oos = new ObjectOutputStream(os);


                /*------------------Creat New User ---------------*/
                String username =usernameTextField.getText() ;
                String email =emailTextField.getText() ;
                String password =passTextField.getText();
                String ip = getIpAdresse().getHostAddress();
                int port = Integer.parseInt(portTextField.getText());
                /*------------------------------------------------------*/

                User u = new User(username,email,password,ip,port,"Offline") ;

                //Read id
                bf.readLine();

                // <<1>> means that this is a registration request ==> For the server
                pw.println(1);

                //then we send the user  information to the server to add it in the data base
                oos.writeObject(u);

                //Get the reponse from server
                String reponse = bf.readLine();
                if (reponse.equals("false"))
                {
                    showToast();
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Username  exists, try another one !",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /*---------------------------------------------------------------------------------*/
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

    /*---------------------------------------------------------------------------------------------------*/
    @FXML
    void Setting()
    {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Setting");

        Pane pane = new Pane() ;
        pane.setPrefSize(350,100);

        portTextField = new TextField() ;
        portTextField.setPromptText("Port");
        portTextField.setLayoutX(65);
        portTextField.setLayoutY(50);
        Label l1 = new Label("Port :") ;
        l1.setLayoutX(20);
        l1.setLayoutY(54);
        portTextField.setPrefSize(250,20);

        pane.getChildren().addAll(portTextField,l1) ;
        textInputDialog.setHeaderText(null);
        textInputDialog.getDialogPane().setContent(pane);
        textInputDialog.showAndWait() ;
    }
    /*----------------------------------------------------------------------------------------------------------*/
    public void showToast()
    {
        snackbar = new JFXSnackbar();
        Text download = GlyphsDude.createIcon(FontAwesomeIconName.CHECK_CIRCLE,"2em") ;
        download.setFill(Color.valueOf("#ffffff"));
        snackbar = new JFXSnackbar(rootStackPane) ;
        snackbar.fireEvent( new JFXSnackbar.SnackbarEvent(download));
    }
}
