package database;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.*;

//Using sqlite database to allow application usage without internet connection
//Also applied singleton to simplyfiy access
public class Database {


    private static Database instance = null;
    private Connection connection = null;

    // initializing database connection
    private Database(){
        try{
            //Checking if database file exists, if not, initializing new database with
            //tables, constraints, etc.
            String url = "jdbc:sqlite:database.db";
            File tmpDir = new File(url);
            System.out.println(tmpDir.getPath());
            if(!tmpDir.exists()){
                connection = DriverManager.getConnection(url);
                connection.setAutoCommit(false);
                initializeDatabase();
            } else{
                connection = DriverManager.getConnection(url);
            }
            //Setting auto commit to false, trying to protect user from accidently deleting data
            connection.setAutoCommit(false);
            System.out.println("Connection to database estabilished successfully");
        } catch (SQLException e){
            System.out.println("Connection to database failed");
            System.out.println(e.getMessage());
        }
    }

    private void initializeDatabase(){
        try{
            //Creating config table with all its columns and constraints, also initializing default values
            //Adding Check(id = 1) constraint because we need only one row in config
            StringBuilder sqlStatement = new StringBuilder();
            sqlStatement.append("CREATE TABLE Config (");
            sqlStatement.append("ID  INTEGER     PRIMARY KEY AUTOINCREMENT   NOT NULL    CHECK(ID = 1) ,");
            sqlStatement.append("password_sample     TEXT    DEFAULT decodeSample ,");
            sqlStatement.append("password_postfix    VARCHAR ,");
            sqlStatement.append("ask_for_password_status BOOLEAN NOT NULL DEFAULT (false) ,");
            sqlStatement.append("generator_number_of_char   INTEGER     NOT NULL    DEFAULT 8 ,");
            sqlStatement.append("generator_include_upper_case   BOOLEAN     NOT NULL    DEFAULT (true) ,");
            sqlStatement.append("generator_include_special_char     BOOLEAN     NOT NULL    DEFAULT (true) ,");
            sqlStatement.append("language   VARCHAR     NOT NULL     DEFAULT English  );");
            connection.prepareStatement(sqlStatement.toString()).execute();

            sqlStatement = new StringBuilder();
            sqlStatement.append("CREATE TABLE Passwords (");
            sqlStatement.append("ID     INTEGER     PRIMARY KEY AUTOINCREMENT   NOT NULL,");
            sqlStatement.append("name   TEXT,");
            sqlStatement.append("website_adress     TEXT,");
            sqlStatement.append("email  TEXT,");
            sqlStatement.append("login  TEXT,");
            sqlStatement.append("password   TEXT,");
            sqlStatement.append("description    TEXT,");
            sqlStatement.append("creation_date  DATE    DEFAULT (datetime('now','localtime')) );");
            connection.prepareStatement(sqlStatement.toString()).execute();

            this.insert("ID","Config","'1'");
            this.commit();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

    //SQL Select statement
    public ResultSet select(@NotNull String columnNames,@NotNull String table,String conditions){
        //SELECT columnNames FROM table WHERE conditions
        String sqlStatement = "SELECT ";
        ResultSet resultSet;
        sqlStatement = sqlStatement.concat(columnNames);
        sqlStatement = sqlStatement.concat(" FROM ");
        sqlStatement = sqlStatement.concat(table);
        sqlStatement = sqlStatement.concat(" WHERE ");
        sqlStatement = sqlStatement.concat(conditions);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Select statement successful");
            return resultSet;
        } catch (SQLException e){
            System.out.println("Retrieving statement failed");
            System.out.println(e.getMessage());
        }
        return null;
    }
    //SQL Select statement without condition
    public ResultSet select(@NotNull String columnNames,@NotNull String table){
        //SELECT columnNames FROM table
        String sqlStatement = "SELECT ";
        ResultSet resultSet;
        sqlStatement = sqlStatement.concat(columnNames);
        sqlStatement = sqlStatement.concat(" FROM ");
        sqlStatement = sqlStatement.concat(table);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Select statement successful");
            return resultSet;
        } catch (SQLException e){
            System.out.println("Select statement failed");
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Parsing which row was inserted last
    public int selectLastInsertedRowID(){
        String sqlStatement = "SELECT last_insert_rowid()";
        int id = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            id = preparedStatement.executeQuery().getInt(1);
        } catch(SQLException e){
            System.out.println("Select last inserted row id failed");
            System.out.println(e.getMessage());
        }
        return id;
    }

    //Standard SQL delete statement
    public void delete(@NotNull String table, String conditions){
        //DELETE FROM table WHERE conditions;
        String sqlStatement = "DELETE FROM ";
        sqlStatement = sqlStatement.concat(table);
        sqlStatement = sqlStatement.concat(" WHERE ");
        sqlStatement = sqlStatement.concat(conditions);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
            System.out.println("Successfully deleted");
        } catch(SQLException e){
            System.out.println("Delete failed");
            System.out.println(e.getMessage());
        }
    }

    //SQL DELETE statement without condition
    public void deleteAll(@NotNull String table){
        //DELETE FROM table;
        String sqlStatement = "DELETE FROM ";
        sqlStatement = sqlStatement.concat(table);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
            System.out.println("Successfully deleted");
        } catch(SQLException e){
            System.out.println("Delete failed");
            System.out.println(e.getMessage());
        }
    }

    //SQL INSERT statement
    public void insert(@NotNull String columns, @NotNull String table, @NotNull String values ){
        //INSERT INTO table(columns) VALUES values;
        String sqlStatement = "INSERT INTO ";
        sqlStatement = sqlStatement.concat(table);
        sqlStatement = sqlStatement.concat("(");
        sqlStatement = sqlStatement.concat(columns);
        sqlStatement = sqlStatement.concat(") ");
        sqlStatement = sqlStatement.concat(" VALUES (");
        sqlStatement = sqlStatement.concat(values);
        sqlStatement = sqlStatement.concat(")");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
            System.out.println("Successfully inserted");
        } catch(SQLException e){
            System.out.println("Insert failed");
            System.out.println(e.getMessage());
        }
    }

    //SQL update statement
    public void update(@NotNull String table, @NotNull String update, @NotNull String conditions ){
        //UPDATE table SET update WHERE conditions
        String sqlStatement = "UPDATE ";
        sqlStatement = sqlStatement.concat(table);
        sqlStatement = sqlStatement.concat(" SET ");
        sqlStatement = sqlStatement.concat(update);
        sqlStatement = sqlStatement.concat(" WHERE ");
        sqlStatement = sqlStatement.concat(conditions);
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
            System.out.println("Successfully inserted");
        } catch(SQLException e){
            System.out.println("Insert failed");
            System.out.println(e.getMessage());
        }
    }

    public void commit(){
        try{
            connection.commit();
            System.out.println("Commit successful");
        } catch(SQLException e) {
            System.out.println("Commit failed");
            System.out.println(e.getMessage());
        }
    }

    public void rollback(){
        try{
            connection.rollback();
            System.out.println("Rollback successful");
        } catch(SQLException e){
            System.out.println("Rollback Failed");
        }
    }
}
