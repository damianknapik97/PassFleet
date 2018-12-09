package database;

import javafx.beans.property.StringProperty;

//This abstract class i absolutely unnecessary here, but implementation was made
//to pass requirements for my student project
public abstract class DataSchema {

    protected  int id;
    protected  StringProperty name ;
    protected  StringProperty websiteAdress ;
    protected  StringProperty email ;
    protected  StringProperty login ;
    protected  StringProperty password ;
    protected  StringProperty description ;
    protected  StringProperty creationDate;

    public abstract void decryptData();
    public abstract void encryptData();


}
