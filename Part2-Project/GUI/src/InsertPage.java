import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class InsertPage implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JLabel success;

    private JLabel gettingData;

    private JButton buttonCreate;

    private String connectionUrl;

    private String currFile;

    public InsertPage(){
        frame = new JFrame("Populate DB Page");
        panel = new JPanel();
        frame.setLayout(new BorderLayout());

        connectDatabase();
        setTitle();
        createButtons();

        buttonCreate.addActionListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        frame.setSize(450, 200);
        frame.setVisible(true);
    }

    public void createButtons(){
        buttonCreate = new JButton();
        buttonCreate.setText("Create Table & Insert Data");
        buttonCreate.setFocusable(false);
        panel.add(buttonCreate);

        success = new JLabel("");
        success.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(success);
    }

    private void setTitle(){
        JPanel topPanel = new JPanel();
        //panel = new JPanel();

        JLabel title = new JLabel("Populate Database");
        title.setSize(100,100);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 25);
        title.setFont(font);

        topPanel.add(title);
        frame.add(topPanel,BorderLayout.NORTH);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == buttonCreate){
            success.setText("Populating the Database. Please wait..... (around 30 minutes)");
            allInsert();
            QueryPage nQueryPage = new QueryPage();
            frame.dispose();
        }

    }

    private void connectDatabase(){
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
    }

    public void allInsert(){
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            File city = new File("csvfiles/city.csv");
            File conference = new File("csvfiles/conference.csv");
            File team = new File("csvfiles/team.csv");
            File gameData = new File("csvfiles/gameData.csv");
            File player = new File("csvfiles/player.csv");
            File season = new File("csvfiles/season.csv");
            File generate = new File("csvfiles/generate.csv");
            File compete = new File("csvfiles/compete.csv");
            File signed = new File("csvfiles/signed.csv");
            File leaderboard = new File ("csvfiles/leaderboard.csv");

            insertInto(connection, city, "city");
            insertInto(connection, conference, "conference");
            insertInto(connection, team, "team");
            insertInto(connection, gameData, "gameData"); //7 minutes to finish
            insertInto(connection, player, "player"); //3 minutes to finish
            insertInto(connection, season, "season");
            insertInto(connection, generate, "generate"); //14 minutes to finish
            insertInto(connection, compete, "compete");
            insertInto(connection,signed, "signed");
            insertInto(connection,leaderboard, "leaderboard");//32 minutes
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void insertInto(Connection connection, File file, String fileName) throws Exception{
        PreparedStatement stmt = null;
        BufferedReader readFile = new BufferedReader(new FileReader(file));
        String line = "";
        line = readFile.readLine();
        String[] tuple;
        tuple = line.split(",");
        String questionMark = "";
        String query = "";
        Boolean isCityConference = fileName.equals("city") || fileName.equals("conference");
        currFile = fileName;

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

    }
    public boolean isInteger(String str){
        try {
            Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
