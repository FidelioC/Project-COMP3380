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

class GUI extends JFrame implements ActionListener {

    private JLabel label;

    private JFrame frame;
    private JPanel panel;
    private JButton button;
    private JTextArea text;

    private JTextArea textArea;
    private Connection connection;
    private Statement statement;
    String connectionUrl;

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

        button = new JButton("Show All City");
        Dimension size = button.getPreferredSize();

        button.setBounds(0, 0, 350, 50);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.addActionListener(this);

        panel.setLayout(null);
        panel.add(button);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();

            ResultSet resultSet = null;
            String result = "";
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * from city";
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
}
