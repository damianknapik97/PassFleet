package database;
import utilities.Encryptor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.SQLException;

public class Data extends DataSchema {

    //Using Property because it alerts application instantly when data has ben changed

    private String constraint;
/*    private int id;
    private StringProperty name ;
    private StringProperty websiteAdress ;
    private StringProperty email ;
    private StringProperty login ;
    private StringProperty password ;
    private StringProperty description ;
    private StringProperty creationDate;*/

    //First constructor is for extracting data from database
    public Data(int id){
        this.setId(id);
        //Constraint is used to point from which row draw data
        constraint = "ID = '";
        constraint = constraint.concat(String.valueOf(id));
        constraint = constraint.concat("'");

        try{
            //Read values from database
            this.name = new SimpleStringProperty(Database.getInstance().select(
                    "name","Passwords",constraint).getString(1));
            this.websiteAdress = new SimpleStringProperty(Database.getInstance().select(
                    "website_adress","Passwords",constraint).getString(1));
            this.email= new SimpleStringProperty(Database.getInstance().select(
                    "email","Passwords",constraint).getString(1));
            this.login = new SimpleStringProperty(Database.getInstance().select(
                    "login","Passwords",constraint).getString(1));
            this.password = new SimpleStringProperty(Database.getInstance().select(
                    "password","Passwords",constraint).getString(1));
            this.description = new SimpleStringProperty(Database.getInstance().select(
                    "description","Passwords",constraint).getString(1));
            this.creationDate = new SimpleStringProperty(Database.getInstance().select(
                    "creation_date","Passwords",constraint).getString(1));

            //If ask_for_password_status is false, it means that application didnt ask user for
            //passwords, so it means that data in database is decrypted and no further action is needed;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Second constructor is for creating new row
    public Data(){
        //INSERT INTO Passwords(name) VALUES (NULL);
        constraint = "ID = '";
        try{
            Database.getInstance().insert("ID","Passwords","NULL");
            this.setId(Database.getInstance().selectLastInsertedRowID());

            constraint = constraint.concat(String.valueOf(id));
            constraint = constraint.concat("'");

            this.name = new SimpleStringProperty(Database.getInstance().select(
                    "name","Passwords",constraint).getString(1));
            this.websiteAdress = new SimpleStringProperty(Database.getInstance().select(
                    "website_adress","Passwords",constraint).getString(1));
            this.email= new SimpleStringProperty(Database.getInstance().select(
                    "email","Passwords",constraint).getString(1));
            this.login = new SimpleStringProperty(Database.getInstance().select(
                    "login","Passwords",constraint).getString(1));
            this.password = new SimpleStringProperty(Database.getInstance().select(
                    "password","Passwords",constraint).getString(1));
            this.description = new SimpleStringProperty(Database.getInstance().select(
                    "description","Passwords",constraint).getString(1));
            this.creationDate = new SimpleStringProperty(Database.getInstance().select(
                    "creation_date","Passwords",constraint).getString(1));
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void update(
            String name, String website, String email,
            String login, String password,String description){
        String updateStatement;

        updateStatement = "name = \"";
        updateStatement = updateStatement.concat(name);
        updateStatement = updateStatement.concat("\"");
        this.setName(name);
        Database.getInstance().update("Passwords",updateStatement,constraint);

        updateStatement = "website_adress = \"";
        updateStatement = updateStatement.concat(website);
        updateStatement = updateStatement.concat("\"");
        this.setWebsiteAdress(website);
        Database.getInstance().update("Passwords",updateStatement,constraint);

        updateStatement = "email= \"";
        updateStatement = updateStatement.concat(email);
        updateStatement = updateStatement.concat("\"");
        this.setEmail(email);
        Database.getInstance().update("Passwords",updateStatement,constraint);

        updateStatement = "login = \"";
        updateStatement = updateStatement.concat(login);
        updateStatement = updateStatement.concat("\"");
        this.setLogin(login);
        Database.getInstance().update("Passwords",updateStatement,constraint);

        updateStatement = "password = \"";
        updateStatement = updateStatement.concat(password);
        updateStatement = updateStatement.concat("\"");
        this.setPassword(password);
        Database.getInstance().update("Passwords",updateStatement,constraint);

        updateStatement = "description = \"";
        updateStatement = updateStatement.concat(description);
        updateStatement = updateStatement.concat("\"");
        this.setDescription(description);
        Database.getInstance().update("Passwords",updateStatement,constraint);
    }

    public void dropThisRow(){
        Database.getInstance().delete("Passwords",constraint);
    }

    @Override
    public void decryptData(){
        //Update is used for constructing update statement
        String update;
        try{
            //Decrypting valuable data simultaneously in variables and in database to prevent
            //accidently encrypting data two times or making some other mistake
            //it takes more code but it is more safe for me
            this.setName(Encryptor.getInstance().decrypt(this.getName().getValueSafe()));
            update = "name = \"";
            update = update.concat(this.getName().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setWebsiteAdress(Encryptor.getInstance().decrypt(this.getWebsiteAdress().getValueSafe()));
            update = "website_adress = \"";
            update = update.concat(this.getWebsiteAdress().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setEmail(Encryptor.getInstance().decrypt(this.getEmail().getValueSafe()));
            update = "email = \"";
            update = update.concat(this.getEmail().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setLogin(Encryptor.getInstance().decrypt(this.getLogin().getValueSafe()));
            update = "login = \"";
            update = update.concat(this.getLogin().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setPassword(Encryptor.getInstance().decrypt(this.getPassword().getValueSafe()));
            update = "password = \"";
            update = update.concat(this.getPassword().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setDescription(Encryptor.getInstance().decrypt(this.getDescription().getValueSafe()));
            update = "description = \"";
            update = update.concat(this.getDescription().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            Database.getInstance().commit();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void encryptData(){
        String update;
        //Constraint is used to point from which row draw data
        try{
            //Encrypting data and inserting it into database
            this.setName(Encryptor.getInstance().encrypt(this.getName().getValueSafe()));
            update = "name = \"";
            update = update.concat(this.getName().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setWebsiteAdress(Encryptor.getInstance().encrypt(this.getWebsiteAdress().getValueSafe()));
            update = "website_adress = \"";
            update = update.concat(this.getWebsiteAdress().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setEmail(Encryptor.getInstance().encrypt(this.getEmail().getValueSafe()));
            update = "email = \"";
            update = update.concat(this.getEmail().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setLogin(Encryptor.getInstance().encrypt(this.getLogin().getValueSafe()));
            update = "login = \"";
            update = update.concat(this.getLogin().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setPassword(Encryptor.getInstance().encrypt(this.getPassword().getValueSafe()));
            update = "password = \"";
            update = update.concat(this.getPassword().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);

            this.setDescription(Encryptor.getInstance().encrypt(this.getDescription().getValueSafe()));
            update = "description = \"";
            update = update.concat(this.getDescription().getValueSafe());
            update = update.concat("\"");
            Database.getInstance().update("Passwords",update,constraint);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public StringProperty getWebsiteAdress() {
        return websiteAdress;
    }

    public void setWebsiteAdress(String websiteAdress) {
        this.websiteAdress.setValue(websiteAdress);
    }

    public StringProperty getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public StringProperty getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login.setValue(login);
    }

    public StringProperty getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public StringProperty getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public StringProperty getCreationDate() {
        return creationDate;
    }

}
