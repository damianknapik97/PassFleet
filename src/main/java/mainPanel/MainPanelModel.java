package mainPanel;

import database.Data;
import database.DataManager;
import database.Database;
import utilities.PasswordGenerator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainPanelModel {

    private PasswordGenerator passwordGenerator;

    //Initializing columns in the table and adding variables for them to observe
    //(data from database)
    public MainPanelModel(TableView<Data> tableView,
                          TableColumn<Data,String> nameColumn,
                          TableColumn<Data,String> websiteAdressColumn,
                          TableColumn<Data,String> emailColumn,
                          TableColumn<Data,String> loginColumn,
                          TableColumn<Data,String> passwordColumn,
                          TableColumn<Data,String> descriptionColumn,
                          Button saveButton, Button newButton,
                          Button deleteButton){
        passwordGenerator = new PasswordGenerator();

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        websiteAdressColumn.setCellValueFactory(cellData -> cellData.getValue().getWebsiteAdress());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmail());
        loginColumn.setCellValueFactory(cellData -> cellData.getValue().getLogin());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().getPassword());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());

        tableView.setItems(DataManager.getInstance().getFilteredDataList());

        saveButton.setTooltip(new Tooltip("CTRL + S"));
        newButton.setTooltip(new Tooltip("CTRL + N"));
        deleteButton.setTooltip(new Tooltip("CTRL + D"));
    }

    public void openSettings(){
        try{
            Pane newPane = FXMLLoader.load(getClass().getClassLoader().getResource("SettingsPanel.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(newPane));
            newStage.setTitle("Settings");
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error !");
            alert.setHeaderText("Something went wrong logging in !");
            alert.setContentText("Login failed, please contact developer");
            alert.show();
        }
    }
    //Updating row in table with text inserted in corresponding fields and saving it into database
    public void saveChanges(TableView<Data> tableView,
                            TextField nameField,
                            TextField websiteAdressField,
                            TextField emailField,
                            TextField loginField,
                            TextField passwordField,
                            TextField descriptionField){

        Data data = tableView.getSelectionModel().getSelectedItem();
        if(data != null){
            data.update(
                    nameField.getText(),
                    websiteAdressField.getText(),
                    emailField.getText(),
                    loginField.getText(),
                    passwordField.getText(),
                    descriptionField.getText()
            );
        }
        Database.getInstance().commit();
    }

    //Simply adding new, empty row in database only with incremented id
    public void addNewRow(TableView<Data> tableView,TextField nameField){
        DataManager.getInstance().createNewRow();
        tableView.requestFocus();
        tableView.getSelectionModel().selectLast();
        tableView.getFocusModel().focus(0);
        nameField.requestFocus();
    }

    public void deleteRow(TableView<Data> tableView){
        Data data = tableView.getSelectionModel().getSelectedItem();
        if(data != null){
            DataManager.getInstance().deleteRow(data.getId());
        }
    }

    public void search(TextField searchField){
        DataManager.getInstance().search(searchField.getText());
    }

    //Assigning TextFields which property should they keep track of
    public void onRowSelected(TableView<Data> tableView,
                              TextField nameField,
                              TextField websiteAdressField,
                              TextField emailField,
                              TextField loginField,
                              TextField passwordField,
                              TextField descriptionField,
                              Text creationDateText){
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue != null ){
                nameField.setText(newValue.getName().getValueSafe());
                websiteAdressField.setText(newValue.getWebsiteAdress().getValueSafe());
                emailField.setText(newValue.getEmail().getValueSafe());
                loginField.setText(newValue.getLogin().getValueSafe());
                passwordField.setText(newValue.getPassword().getValueSafe());
                descriptionField.setText(newValue.getDescription().getValueSafe());
                creationDateText.setText(newValue.getCreationDate().getValueSafe());
            }
        } );
    }

    //Generating password and
    public void generatePassword(TextField passwordField){
        try{
            passwordField.setText(passwordGenerator.generateNewPassword(
                    Database.getInstance().select("generator_number_of_char","Config").getInt(1),
                    Database.getInstance().select("generator_include_upper_case","Config").getBoolean(1),
                    Database.getInstance().select("generator_include_special_char", "Config").getBoolean(1)));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }
}
