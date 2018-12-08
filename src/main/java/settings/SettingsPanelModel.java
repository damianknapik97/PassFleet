package settings;

import utilities.LimitedPasswordField;
import database.Database;
import utilities.Encryptor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class SettingsPanelModel {

    //Inserting values into fx elements to match those in database
    public SettingsPanelModel(CheckBox requestPasswordCheckBox,
                              CheckBox includeUpperCaseLettersCheckoBox,
                              CheckBox includeSpecialCharacterCheckBox,
                              ComboBox numberOfCharactersComboBox,
                              ComboBox languageComboBox){

        //Populating combo boxes
        for(int i = 6; i<26;i++)
            numberOfCharactersComboBox.getItems().add(new Integer(i));
        languageComboBox.getItems().add("English");

        try{
            requestPasswordCheckBox.setSelected(
                    Database.getInstance().select(
                            "ask_for_password_status","Config").getBoolean(1)
            );
            includeUpperCaseLettersCheckoBox.setSelected(
                    Database.getInstance().select(
                            "generator_include_upper_case", "Config").getBoolean(1)
            );
            includeSpecialCharacterCheckBox.setSelected(
                    Database.getInstance().select(
                            "generator_include_special_char", "Config").getBoolean(1)
            );
            numberOfCharactersComboBox.setValue(
                    Database.getInstance().select(
                            "generator_number_of_char","Config").getString(1)
            );
           languageComboBox.setValue(
                    Database.getInstance().select(
                            "language","Config").getString(1)
            );
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //TODO PASSWORDS SETTINGS SHOULD BE DISPLAYED IN ONE LINE, REST OF THE SETTINGS IN OTHER LINES

    public void saveSettings(LimitedPasswordField password, LimitedPasswordField passwordRepeat,
                             TextField passwordHint,
                             CheckBox requestPasswordCheckBox, CheckBox includeUpperCaseLettersCheckBox,
                             CheckBox includeSpecialCharacterCheckBox, ComboBox numberOfCharactersComboBox,
                             ComboBox languageComboBox, Button saveSettingsButton){
        //Inserting values to database -> config
        String update;
        try {
            if (password.getText().equals(passwordRepeat.getText())
                    && password.getText().isEmpty() == false
                    && passwordRepeat.getText().isEmpty() == false) {
                String postfix = "password_postfix = ";
                postfix = postfix.concat("\"");
                postfix = postfix.concat(Encryptor.getInstance().setKey(password.getText()));
                postfix = postfix.concat("\"");
                Database.getInstance().update("Config", postfix, "ID = '1'");
                String passwordSample = "password_sample = ";
                passwordSample = passwordSample.concat("\"");
                passwordSample = passwordSample.concat(Encryptor.getInstance().encrypt("decodeSample"));
                passwordSample = passwordSample.concat("\"");
                Database.getInstance().update("Config", passwordSample,"ID = '1'" );

                //Removing data from variables for safety, just in case.
                postfix = null;
                passwordSample = null;
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        if (passwordHint.getText().isEmpty() == false){
            String hint ="password_hint = ";
            hint = hint.concat("\"");
            hint = hint.concat( passwordHint.getText());
            hint = hint.concat("\"");
            Database.getInstance().update("Config",hint,"id = '1'");
        }


        update = "ask_for_password_status = ";
        update = update.concat(String.valueOf( requestPasswordCheckBox.isSelected()));
        Database.getInstance().update("Config",update  ,"id = '1'");
        //above statement suggest that user wants to protect himself, so nowe we have to encrypt data in database

        update = "generator_include_upper_case = ";
        update = update.concat(String.valueOf(includeUpperCaseLettersCheckBox.isSelected()));
        Database.getInstance().update("Config",update,"id = 1");

        update = "generator_include_special_char = ";
        update = update.concat(String.valueOf(includeSpecialCharacterCheckBox.isSelected()));
        Database.getInstance().update("Config",update,"id = 1");

        update = "generator_number_of_char = ";
        update = update.concat(numberOfCharactersComboBox.getSelectionModel().getSelectedItem().toString());
        Database.getInstance().update("Config",update,"id = 1");

        update = "generator_number_of_char = ";
        update = update.concat(numberOfCharactersComboBox.getSelectionModel().getSelectedItem().toString());
        Database.getInstance().update("Config",update,"id = 1");

        update = "language = ";
        update = update.concat("\"");
        update = update.concat(languageComboBox.getSelectionModel().getSelectedItem().toString());
        update = update.concat("\"");
        Database.getInstance().update("Config",update,"id = 1");

        Database.getInstance().commit();

        Stage stage = (Stage) saveSettingsButton.getScene().getWindow();
        stage.close();
    }

    public void cancelSettings(Button button){
        Stage stage =(Stage) button.getScene().getWindow();
        stage.close();
    }
}
