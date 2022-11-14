import java.io.IOException;
import java.sql.Statement;
import java.io.*;
import java.util.Scanner;

public class InsertCSV {
    private File fileName;
    private Scanner readFile;

    InsertCSV (File theFile) {
        try {
            fileName  = theFile;
            readFile = new Scanner(fileName);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    public void insertCity(Statement s) throws Exception{
        String insertSQL = "";
        String line = "";
        line = readFile.nextLine();
        String[] tuple;
        while(readFile.hasNextLine()){
            line = readFile.nextLine();
            tuple = line.split(",");
            insertSQL = "insert into city values ('" + tuple[1] + "')";
            s.executeUpdate(insertSQL);
        }
    }
}
