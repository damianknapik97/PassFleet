package main;

import database.DataManager;
import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utilities.Encryptor;

import java.io.File;
import java.sql.SQLException;

//TODO Change PasswordFields to SimplePasswordFields


public class Main extends Application {

    private static File file;
    //To prevent other instances of application from removing temporary file
    private static boolean isRunning = true;

    @Override
    public void start(Stage primaryStage){

        try{
            //Due to implemented encrypting/decrypting and database access methods by me,
            //we need to limit usage of application by allowing user to open only one instance of it
            //to do this, we will be creating temporary file that will be deleted afterwads
            String path = System.getProperty("user.dir");
            path = path.concat("/temporary.txt");
            file = new File(path);
            if(!file.isFile()){
                file.createNewFile();
                isRunning = true;

                //Reads from database if application is protected by password or not
                //(Decides if there is need for showing login screen)
                if(Database.getInstance().select("ask_for_password_status","Config").getBoolean(1) == false){
                    Parent root = FXMLLoader.load( getClass().getResource("/MainPanel.fxml"));
                    primaryStage.setTitle("PassFleet");
                    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
                    primaryStage.setResizable(false);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                } else{
                    Parent root = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
                    primaryStage.setTitle("PassFleet");
                    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
                    primaryStage.setResizable(false);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
            } else{
                isRunning = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error !");
                alert.setContentText("Program is already running");
                alert.show();
            }
        } catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !");
            alert.setHeaderText("Something went wrong starting the application !");
            alert.setContentText("Login failed, please contact developer");
            alert.show();
        }
    }

    //Decides if there is need for encrypting data in database
    //Conditions for this action are:
    // 1) Boolean variable extracted from table Config in database stating that user
    //    wants to be protected by password
    // 2) User has successfully logged into application, and boolean variable
    //    in Encryption class guarding that has been changed to true;
    @Override
    public void stop(){
        //Deletin temporary txt file preventing user from launching two appplications at the same time
        if(isRunning)
            file.delete();
        //Encrypting database if user was logged successfully and checkec the "ask for password"
        //box in settings
        try{
            if(Database.getInstance().select(
                    "ask_for_password_status","Config").getBoolean(1)
                    && Encryptor.getInstance().getLoginFlag()){
                DataManager.getInstance().encryptData();
                Database.getInstance().commit();
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {launch(args);
    }
}
