import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class QueryPage implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private Connection connection;
    private String connectionUrl;
    private String selectSql;
    private String username;
    private String password;
    private String resultTableName;
    private JButton[] allButtons;

    private int buttonHeight = 50;
    public QueryPage() {
        Properties prop = new Properties();
        String fileName = "auth.cfg";
        allButtons = new JButton[10];

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

        frame = new JFrame("Queries");
        panel = new JPanel();
        panel.setLayout(null);
        insertAllButtons();
        //queryButtons();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, buttonHeight * allButtons.length + buttonHeight);
        frame.setVisible(true);
    }

    private void insertAllButtons(){
        int xPos = 0;
        int yPos = 0;
        int buttonWidth = 350;

        for(int i=0; i<allButtons.length; i++){
            allButtons[i] = new JButton();
            allButtons[i].setBounds(xPos, yPos, buttonWidth, buttonHeight);
            allButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            panel.add(allButtons[i]);
            yPos += buttonHeight;
        }
    }
    public void runQuery() {
        try {
            connectionUrl =
                    "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                            + "database=cs3380;"
                            + "user=" + username + ";"
                            + "password=" + password + ";"
                            + "encrypt=false;"
                            + "trustServerCertificate=false;"
                            + "loginTimeout=30;";

            connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();

            ResultSet resultSet = null;

            // Create and execute a SELECT SQL statement.
            resultSet = statement.executeQuery(selectSql);

            makeTable(resultSet);

            connection.close();
            statement.close();

        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    public void makeTable(ResultSet resultSet) throws Exception {
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

    public void queryButtons() {
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

    public void actionPerformed(ActionEvent e){

    }
    public void setUsername(String theUsername){
        username = theUsername;
    }

    public void setPassword(String thePassword){
        password = thePassword;
    }
}