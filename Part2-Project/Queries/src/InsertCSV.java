import java.sql.Statement;
import java.io.*;
import java.util.Scanner;

public class InsertCSV {
    private File fileName;
    private Scanner readFile;

    InsertCSV () {

    }
    public void insertManyOne(Statement s, String name, File theFile) throws Exception{
        setFile(theFile);
        String insertSQL = "";
        String line = "";
        line = readFile.nextLine();
        String[] tuple;
        while(readFile.hasNextLine()){
            line = readFile.nextLine();
            tuple = line.split(",");
            insertSQL = "insert into " + name + " values ('" + tuple[1] + "')";
            s.executeUpdate(insertSQL);
        }
    }
    private void setFile(File theFile) throws Exception{
        fileName = theFile;
        readFile = new Scanner(fileName);
    }

}
