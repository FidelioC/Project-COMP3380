import java.io.IOException;
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

    public void insertManyMany(Statement s, String name, File theFile) throws Exception{
        setFile(theFile);
        String insertSQL = "";
        //String[] attributes;
        String line = "";
        line = readFile.nextLine();
        //attributes = line.split(",");

        String[] tuple;
        String tupleData= "";
        String replace = "";
        while(readFile.hasNextLine()){
            line = readFile.nextLine();
            tuple = line.split(",");
            for(int i=0; i<tuple.length; i++){
                if(tuple[i].matches("\\d+")) {//if int
                    tupleData += tuple[i];
                }
                else{
                    if(tuple[i].contains("'")){
                        replace = tuple[i].replace("'","");
                        tupleData += "'" + replace  + "'";
                    }else{
                        tupleData += "'" + tuple[i]  + "'";
                    }

                }
                if(i < tuple.length-1){
                    tupleData += ",";
                }
            }

            insertSQL = "insert into " + name + " values (" + tupleData + ")";
            System.out.println(insertSQL);
            s.executeUpdate(insertSQL);

            tupleData = "";
        }
    }


    private void setFile(File theFile) throws Exception{
        fileName = theFile;
        readFile = new Scanner(fileName);
    }

}
