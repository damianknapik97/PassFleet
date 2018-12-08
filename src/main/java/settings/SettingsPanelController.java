package settings;

import utilities.LimitedPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class SettingsPanelController {

    @FXML
    private LimitedPasswordField passwordField;
    @FXML
    private LimitedPasswordField passwordRepeatField;
    @FXML
    private TextField passwordHintField;

    @FXML
    private CheckBox requestPasswordCheckBox;
    @FXML
    private CheckBox includeUpperCaseLettersCheckBox;
    @FXML
    private CheckBox includeSpecialCharacterCheckBox;

    @FXML
    private ComboBox numberOfCharactersComboBox;
    @FXML
    private ComboBox languageComboBox;

    @FXML
    private Button saveSettingsButton;
    @FXML
    private Button cancelSettingsButton;

    private SettingsPanelModel model;


    @FXML
    private void initialize(){
        //Below code is run after initialize has completed and all fxml components are ready
        Platform.runLater(() -> {
            model = new SettingsPanelModel(
                    requestPasswordCheckBox,
                    includeUpperCaseLettersCheckBox,
                    includeSpecialCharacterCheckBox,
                    numberOfCharactersComboBox,
                    languageComboBox);
        });
    }

    @FXML
    private void saveSettings(){
        model.saveSettings(passwordField,passwordRepeatField,passwordHintField,
                requestPasswordCheckBox,includeUpperCaseLettersCheckBox, includeSpecialCharacterCheckBox,
                numberOfCharactersComboBox,languageComboBox,saveSettingsButton);
    }

    @FXML
    private void cancelSettings(){
        model.cancelSettings(cancelSettingsButton);
    }

}
