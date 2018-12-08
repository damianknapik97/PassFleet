package loginScreen;

import utilities.LimitedPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public class LoginScreenController {

    @FXML
    private Pane mainPane;
    @FXML
    private Button loginButton;
    @FXML
    private Button quitButton;
    @FXML
    private LimitedPasswordField passwordField;
    @FXML
    private Text textField;

    private LoginScreenModel model;

    @FXML
    public void initialize(){
        //Below code is run after initialize has completed and all fxml components are ready
        Platform.runLater(() ->{
            model = new LoginScreenModel(passwordField);
            keyboardEvents();
            passwordField.requestFocus();
        });
    }
    // used on login button
    public void login(){
        model.verifyLogIn(passwordField,textField);
    }
    public void quit(){
        model.quit(mainPane);
    }

    public void keyboardEvents(){
        textField.requestFocus();
        mainPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER))
                login();
            if(event.getCode().equals(KeyCode.ESCAPE))
                quit();
        });
    }
}

