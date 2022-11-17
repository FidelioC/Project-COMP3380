import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        GUI nGUI = new GUI();
    }
}

class GUI{

    private JLabel label;

    private JFrame frame;
    private JPanel panel;
    private JTextArea text;

    private JTextArea textArea;
    private Connection connection;
    private Statement statement;
    String connectionUrl;
    String selectSql;

    public GUI() {
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

        if (username == null || password == null) {
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                        + "database=cs3380;"
                        + "user=" + username + ";"
                        + "password=" + password + ";"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        frame = new JFrame("Demo Frame");
        panel = new JPanel();
        panel.setLayout(null);
        //queryButtons();
        createLogin();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void runQuery() {
        try {
            connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();

            ResultSet resultSet = null;
            String result = "";
            // Create and execute a SELECT SQL statement.

            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                result += resultSet.getString(1) +
                        " " + resultSet.getString(2) + "\n";
            }
            //System.out.println(result);
            JOptionPane.showMessageDialog(null, result, "table 1", JOptionPane.PLAIN_MESSAGE);

            connection.close();
            statement.close();

        } catch (Exception s) {
            s.printStackTrace();
        }
    }
    public void queryButtons(){
        JButton button = new JButton(new AbstractAction("Show All Cities") {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSql = "select * from city";
                runQuery();
            }
        });
        button.setBounds(0, 0, 350, 50);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(button);
    }

    public void createLogin(){
        JLabel label = new JLabel("User");
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

        JButton buttonLogin = new JButton("Login");
        buttonLogin.setBounds(10,80,80,25);
        panel.add(buttonLogin);

        JLabel success = new JLabel("");
        success.setBounds(10,110,300,25);
        panel.add(success);

    }
}
