package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataManager extends DataSchema {

    //Two lists to allow user to filter that full one
    private final ObservableList<Data> fullDataList;
    //Filtered fullDataList is the one that is going to be used in controller
    private final ObservableList<Data> filteredDataList;
    //Using singleton to save RAM and simplify code
    //(mainly because i need to use this whole class in MainPanel, encrypt method in Main.Main class,
    // and decrypt method in LoginScreen)
    private static DataManager instance;

    //Initializing both lists and fill them with data from database
    private DataManager(){
        fullDataList = FXCollections.observableArrayList();
        filteredDataList = FXCollections.observableArrayList();
        readDatabase();
    }

    //Standard singleton getInstance() method
    public static DataManager getInstance(){
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }

    //Reading entire database to fullDataList and copying fullDataList into filteredDataList
    private void readDatabase(){
        try{
            ResultSet resultSet = Database.getInstance().select("id","Passwords");
            while(resultSet.next()){
                fullDataList.add(new Data(resultSet.getInt(1)));
            }
            replaceFilteredDataListWithFullDataList();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Getter
    public ObservableList<Data> getFilteredDataList(){
        return filteredDataList;
    }

    //Creating new row by calling constructor with no arguments that
    //only job is to create new row in database with incremnted primary key
    // and actual local date time as creation time
    public void createNewRow(){
        fullDataList.add(new Data());
        replaceFilteredDataListWithFullDataList();
    }

    //Deleting row by searching whole list for desired id and then removing it from database and from both lists
    public void deleteRow(int id){
        for(Data d : fullDataList){
            if(d.getId() == id){
                d.dropThisRow();
                fullDataList.remove(d);
                if(filteredDataList.contains(d))
                    filteredDataList.remove(d);
                break;
            }
        }
    }

    //Searching for desired substring entered by user in query field by filtering whole Data list of
    //Data objects that doesnt contain substring in name field
    public void search(String query){
        ObservableList<Data> elementsToRemove =FXCollections.observableArrayList();

        replaceFilteredDataListWithFullDataList();
        for(Data d :filteredDataList){
            if(d.getName().getValueSafe().toLowerCase().contains(query.toLowerCase()) == false){
                elementsToRemove.add(d);
            }
        }
        for(Data d :elementsToRemove){
            filteredDataList.remove(d);
        }
    }

    //Used to return filteredDataList to its original state (with all Data objects extracted from fullDataList)
    private void replaceFilteredDataListWithFullDataList(){
        filteredDataList.remove(0,filteredDataList.size());
        for(Data d : fullDataList){
            filteredDataList.add(d);
        }
    }

    //Calling decrypt method for all data objects
    @Override
    public  void decryptData(){
        for(Data d : fullDataList){
            d.decryptData();
        }
        replaceFilteredDataListWithFullDataList();
    }
    //Caling encrypt method for all data objects
    @Override
    public  void encryptData(){
        for(Data d : fullDataList){
            d.encryptData();
        }
        replaceFilteredDataListWithFullDataList();
    }

}
