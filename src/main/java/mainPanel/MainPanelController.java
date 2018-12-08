package mainPanel;

import database.Data;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MainPanelController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField nameField;
    @FXML
    private TextField websiteAdressField;
    @FXML
    private TextField eMailField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField searchField;
    @FXML
    private Text creationDateText;
    @FXML
    private Button saveButton;
    @FXML
    private Button newButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button randomButton;
    @FXML
    private TableView<Data> tableView;
    @FXML
    private TableColumn<Data,String> nameColumn;
    @FXML
    private TableColumn<Data,String> websiteAdressColumn;
    @FXML
    private TableColumn<Data,String> emailColumn;
    @FXML
    private TableColumn<Data,String> loginColumn;
    @FXML
    private TableColumn<Data,String> passwordColumn;
    @FXML
    private TableColumn<Data,String> descriptionColumn;

    private MainPanelModel model;

    @FXML
    private void initialize(){
        //Below code is run after initialize has been completed and all fxml components are ready
        Platform.runLater(() -> {
            //Initializing columns in the table and adding variables for them to observe
            //(data from database)
            model = new MainPanelModel(tableView, nameColumn,
                    websiteAdressColumn, emailColumn, loginColumn,
                    passwordColumn, descriptionColumn,saveButton,
                    newButton,deleteButton);
            //Assigning TextFields which property should they keep track of
            model.onRowSelected(tableView, nameField,websiteAdressField,
                    eMailField,loginField,passwordField,descriptionField,creationDateText);
            tableView.getSelectionModel().select(0);
            //Initializing keyboard shortcuts
            keyboardEvents();
        });
    }

    @FXML
    private void saveChanges(){
        //Updating row in table with text inserted in corresponding fields
        model.saveChanges(tableView, nameField,websiteAdressField,
                eMailField,loginField,passwordField,descriptionField);
    }

    @FXML
    private void addNewRow(){
        //Simply adding new, empty row in database only with incremented id
        model.addNewRow(tableView,nameField);
    }

    @FXML
    private void deleteRow(){
        model.deleteRow(tableView);
    }

    @FXML
    private void openSettings(){
        model.openSettings();
    }

    @FXML
    private void search(){
        model.search(searchField);
    }

    @FXML
    private void generatePassword(){
        model.generatePassword(passwordField);
    }

    private void keyboardEvents(){

        //Coding key shortcuts and key events to make application more enjoyable for user
        //(and for me )

        // ENTER = SAVE
        // SEARCH FIELD FOCUSED + ENTER = SEARCH
        // CTRL + N = new row
        // CTRL + S = save row
        // CTRL + S + selected search bar = save row
        // CTRL + D = delete row
        // DELETE = delete row
        // CTRL + ARROW UP = highlight the one row up in table
        // CTRL + ARROW DOWN = highlight the one down row in table
        // CTRL + C = exit
        anchorPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER) && searchField.isFocused())
                this.search();
            if(event.getCode().equals(KeyCode.ENTER) && !searchField.isFocused())
                this.saveChanges();
            if(event.isControlDown() && event.getCode().equals(KeyCode.N))
                this.addNewRow();
            if(event.isControlDown() && event.getCode().equals(KeyCode.S))
                this.saveChanges();
            if(event.isControlDown() && event.getCode().equals(KeyCode.D))
                this.deleteRow();
            if(event.getCode().equals(KeyCode.DELETE))
                this.deleteRow();
            if(event.isControlDown() && event.getCode().equals(KeyCode.UP))
                this.tableView.requestFocus();
            if(event.isControlDown() && event.getCode().equals(KeyCode.DOWN))
                this.tableView.requestFocus();
            if(event.isControlDown() && event.getCode().equals(KeyCode.C)){
                Platform.exit();
            }
        });
    }
}
