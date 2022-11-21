import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Main {
    static String connectionUrl;
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
        String username = (prop.getProperty("username"));;
        String password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                        + "database=cs3380;"
                        + "user=" + username + ";"
                        + "password="+ password +";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        try {
            File createTable = new File("create-table.sql")   ;
            createTable(createTable);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void createTable(File file) throws Exception{
        Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        BufferedReader readFile = new BufferedReader(new FileReader(file));
        String line = "";
        String query = "";
        boolean startQuery = false;
        boolean endQuery = false;
        while((line = readFile.readLine()) != null){
            if(line.equals("*")){
                startQuery = true;
                line = readFile.readLine();
            } else if (line.equals("#")) {
                startQuery = false;

            }
            if(startQuery){
                query += line;
            } else {
                System.out.println(query);
                statement.execute(query);
                query = "";
            }

        }
        connection.close();
    }
}