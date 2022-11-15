import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.*;
import java.util.Scanner;

public class MainSQL {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = "ciandyf";
        String password = "7934456";

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password="+ password +";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {


            insertAll(statement);

            // Create and execute a SELECT SQL statement.
            String selectSql = "select * from conference;";
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + 
                " " + resultSet.getString(2));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertAll(Statement s) throws Exception{
        File city = new File("city.csv");
        File conference = new File("conference.csv");

        InsertCSV insert = new InsertCSV();

        insert.insertManyOne(s,"city", city);
        insert.insertManyOne(s,"conference", conference);


    }

}
