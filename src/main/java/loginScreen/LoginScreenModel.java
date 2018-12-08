package loginScreen;

import utilities.LimitedPasswordField;
import database.DataManager;
import database.Database;
import utilities.Encryptor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginScreenModel {

    public LoginScreenModel(LimitedPasswordField passwordField){
        //Seting hint
        String hint = "Hint: ";
        try{
            String databaseString =(Database.getInstance().select(
                    "password_hint","Config").getString(1));
            if(databaseString != null){
                hint = hint.concat(databaseString);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        passwordField.setTooltip(new Tooltip(hint));
    }

    //Checking if password match the one hidden in files
    //If password matches, decode database and proceed
    //If not, only display information about wrong password
    public void verifyLogIn(LimitedPasswordField passwordField, Text textField){
        if(Encryptor.getInstance().checkIfPasswordIsCorrect(passwordField.getText())){
            loginSuccesfull((Stage) passwordField.getScene().getWindow(),passwordField, textField);
        } else{
            loginFailed(textField,passwordField);
        }
    }

    private void loginSuccesfull(Stage stage,LimitedPasswordField paswordField, Text textField){
        try {
            textField.setText("Login Succesfull");
            //Decrypting Data
            DataManager.getInstance().decryptData();
            //Launching next panel -> MainPanel.fxml
            Pane newPane = FXMLLoader.load(getClass().getResource("/MainPanel.fxml"));
            Stage oldStage = stage;
            Stage newStage = new Stage();
            newStage.getIcons().add(oldStage.getIcons().get(0));
            newStage.setScene(new Scene(newPane));
            newStage.setTitle(oldStage.getTitle());
            newStage.setResizable(false);
            oldStage.close();
            newStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !");
            alert.setHeaderText("Something went wrong logging in !");
            alert.setContentText("Login failed, please contact developer");
            alert.show();
        } finally{
            paswordField.requestFocus();
        }
    }

    private void loginFailed(Text textField,LimitedPasswordField passwordField){
        textField.setText("Login failed, hover over the password for hint");
        passwordField.requestFocus();
    }

    //Used if exit button is clicked
    public void quit(Pane mainPane){
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.close();
    }

}
