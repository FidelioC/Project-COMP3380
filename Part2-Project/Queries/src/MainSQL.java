import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
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

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            File city = new File("city.csv");
            File conference = new File("conference.csv");
            File team = new File("team.csv");
            File gameData = new File("gameData.csv");
            File player = new File("player.csv");
            File season = new File("season.csv");
            File generate = new File("generate.csv");

            //insertInto(connection, city, "city");
            //insertInto(connection, conference, "conference");
            //insertInto(connection, team, "team");


            //insertInto(connection, gameData, "gameData"); //7 minutes to finish

            insertInto(connection, player, "player"); //3 minutes to finish
            //insertInto(connection, season, "season");
            //insertInto(connection, generate, "generate"); //14 minutes to finish
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertInto(Connection connection, File file, String fileName) throws Exception{
        PreparedStatement stmt = null;
        BufferedReader readFile = new BufferedReader(new FileReader(file));
        String line = "";
        line = readFile.readLine();
        String[] tuple;
        tuple = line.split(",");
        String questionMark = "";
        String query = "";
        Boolean isCityConference = fileName.equals("city") || fileName.equals("conference") ||
                                    fileName.equals("season");
        System.out.println("Getting Data from " + fileName + ".......");
        while((line = readFile.readLine()) != null) {
            tuple = line.split(",");
            if(isCityConference){
                query = "insert into " + fileName + " values (?)";
                stmt = connection.prepareStatement(query);
                stmt.setString(1,tuple[1]);
            }
            else{
                for(int i=0; i<tuple.length; i++){
                    questionMark += "?";
                    if(i < tuple.length-1)
                        questionMark += ",";
                }
                query = "insert into " + fileName + " values (" + questionMark + ")";
                stmt = connection.prepareStatement(query);
                for(int i=0; i<tuple.length; i++){
                    if(tuple[i].matches("-?\\\\d+")){//is Numeric
                        if(isInteger(tuple[i])){
                            stmt.setInt(i+1,Integer.parseInt(tuple[i]));
                        }else{
                            stmt.setDouble(i+1,Integer.parseInt(tuple[i]));
                        }
                    }
                    else{
                        stmt.setString(i+1,tuple[i]);
                    }
                }
                questionMark = "";
            }
            //System.out.println(query);
            stmt.executeUpdate();

        }

        System.out.println("Getting Data from " + fileName + " Finished!");
    }
    public static boolean isInteger(String str){
        try {
            Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
