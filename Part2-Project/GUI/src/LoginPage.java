import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;
public class LoginPage implements ActionListener {
    private JFrame frame;
    private JPanel panel;

    private Connection connection;
    private String connectionUrl;

    private JTextField userText;

    private JPasswordField passwordText;

    private JLabel success;

    private JButton buttonLogin;
    public LoginPage(){
        frame = new JFrame("Login Page");
        panel = new JPanel();
        panel.setLayout(null);

        createLogin();

        buttonLogin.addActionListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
    public void createLogin(){
        JLabel label = new JLabel("Username");
        label.setBounds(10,20,80,25);
        panel.add(label);

        userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        success = new JLabel("");
        success.setBounds(10,110,300,25);
        panel.add(success);

        buttonLogin = new JButton();
        buttonLogin.setText("Login");
        buttonLogin.setFocusable(false);
        buttonLogin.setBounds(10,80,80,25);
        panel.add(buttonLogin);
    }

    public void actionPerformed(ActionEvent e){
        boolean isTrue = false;
        if(e.getSource() == buttonLogin){
            connectionUrl =
                    "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                            + "database=cs3380;"
                            + "user=" + userText.getText() + ";"
                            + "password=" + passwordText.getText() + ";"
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
                QueryPage nQueryPage = new QueryPage();
                nQueryPage.setUsername(userText.getText());
                nQueryPage.setPassword(passwordText.getText());
                frame.dispose();
            }else{
                success.setText("Please input a valid username or password");
            }
        }
    }
}
