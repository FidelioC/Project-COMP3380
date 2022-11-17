import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        GUI nGUI = new GUI();
    }
}

class GUI{
    private JFrame frame;
    private JPanel panel;
    private Connection connection;
    private String connectionUrl;
    private String selectSql;
    private String username;
    private String password;
    private boolean isTrue;
    private String resultTableName;
    public GUI() {
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        username = "ciandyf";
        password = "7934456";

        connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                        + "database=cs3380;"
                        + "user=" + username + ";"
                        + "password=" + password + ";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        try {
            connection = DriverManager.getConnection(connectionUrl);
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }catch (Exception e){

        }

        frame = new JFrame("Demo Frame");
        panel = new JPanel();
        panel.setLayout(null);
        queryButtons();
        //createLogin();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void runQuery() {
        try {
            //connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();

            ResultSet resultSet = null;
            String result = "";

            // Create and execute a SELECT SQL statement.
            resultSet = statement.executeQuery(selectSql);

            makeTable(resultSet);

            //connection.close();
            statement.close();

        } catch (Exception s) {
            s.printStackTrace();
        }
    }
    public void makeTable(ResultSet resultSet) throws Exception{
        JFrame tableWindow;
        JTable table;

        tableWindow = new JFrame();

        // Frame Title
        tableWindow.setTitle(resultTableName);//change to preferred display name

        //get the data from result set
        List<Object[]> data = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        //get column info and name
        int nCol = resultSet.getMetaData().getColumnCount();
        String[] columnNames = null;
        columnNames = new String[nCol];
        for (int column = 0; column < nCol; column++) {
            columnNames[column] = metaData.getColumnName(column + 1).toUpperCase();
        }

        //get data
        while (resultSet.next()) {
            final Object[] row = new Object[nCol];
            for (int columnIndex = 0; columnIndex < nCol; columnIndex++) {
                row[columnIndex] = resultSet.getObject(columnIndex + 1);
            }
            data.add(row);
        }

        // Print results from select statement
        table = new JTable(data.toArray(new Object[data.size()][nCol]), columnNames);
        table.setBounds(30, 40, 200, 300);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(table);
        tableWindow.add(sp);
        // Frame Size
        tableWindow.setSize(500, 200);
        // Frame Visible = true
        tableWindow.setVisible(true);

    }
    public void queryButtons(){
        JButton buttonCity = new JButton(new AbstractAction("Show All Cities") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSql = "select * from city";
                resultTableName = "All Cities";
                runQuery();
            }
        });
        buttonCity.setBounds(0, 0, 350, 50);
        buttonCity.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(buttonCity);
    }

    public void createLogin(){
        JLabel label = new JLabel("Username");
        label.setBounds(10,20,80,25);
        panel.add(label);

        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField();
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        JLabel success = new JLabel("");
        success.setBounds(10,110,300,25);
        panel.add(success);
        JButton buttonLogin = new JButton(new AbstractAction("Login") {
            @Override
            public void actionPerformed(ActionEvent e) {
                isTrue = false;
                username = userText.getText();
                password = passwordText.getText();
                connectionUrl =
                        "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                                + "database=cs3380;"
                                + "user=" + username + ";"
                                + "password=" + password + ";"
                                + "encrypt=false;"
                                + "trustServerCertificate=false;"
                                + "loginTimeout=30;";
                try{
                    DriverManager.getConnection(connectionUrl);
                    isTrue = true;
                }catch(Exception s){
                    isTrue = false;
                }

                if(isTrue){
                    success.setText("Login Successful!!");
                }else{
                    success.setText("Please input a valid username or password");
                }
            }
        });
        buttonLogin.setBounds(10,80,80,25);
        panel.add(buttonLogin);

    }
}
