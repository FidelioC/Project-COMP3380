import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private int totalQueries = 7;
    private JButton[] allButtons;
    private String[] allQueries;
    private String[] buttonTitle;
    private int frameWidth = 700;
    private int frameHeight = 500;
    private boolean askInput;
    private String userInput;
    private JFrame tableWindow;
    private boolean tableOpened;
    public QueryPage() {
        connectDatabase();
        pageSetup();
        setAllQueries();

        for (int i=0; i<totalQueries; i++){
            allButtons[i].addActionListener(this);
        }
    }

    private void pageSetup(){
        frame = new JFrame("Queries");
        frame.setLayout(new BorderLayout());

        setTitle();
        panel.setBorder(new EmptyBorder(10,30,2,30));
        panel.setLayout(new GridLayout(totalQueries,1,10,5));
        insertAllButtons();

        frame.add(panel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
    private void setTitle(){
        JPanel topPanel = new JPanel();
        panel = new JPanel();

        JLabel title = new JLabel("NBA Dataset Queries");
        title.setSize(100,100);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 25);
        title.setFont(font);

        topPanel.add(title);
        frame.add(topPanel,BorderLayout.NORTH);
    }
    private void setAllButtonsTitle(){
        String[] theButtonTitle = {
            "City That Has Multiple Teams",
            "Highest Score a Team Has Achieved At Home",
            "Players That Never Change Any Team",
            "Which Team Does the Player 'X' Played For Each Season",
            "List of Teams on each conference",
            "Top 5 Team In East Conference That Has Highest Assists When Playing At Home",
            "Total Wins of Each Team from 2004-2020"
        };
        buttonTitle = theButtonTitle;
    }
    private void setAllQueries(){
        String[] theQueries = {
            "SELECT cityName\n" +
                    "from team\n" +
                    "join city on city.cityID = team.cityID\n" +
                    "group by cityName\n" +
                    "having count(cityName) > 1",

            "with allTeam as(\n" +
                    "  SELECT team.teamID, teamName, gameData.ptsHome, gameData.gameID\n" +
                    "  from team\n" +
                    "  join generate on team.teamID = generate.teamID\n" +
                    "  join gameData on generate.gameID = gameData.gameID\n" +
                    "  where team.teamID = gameData.homeTeamID\n" +
                    ")\n" +
                    "select teamID, teamName, max(ptsHome) as HighestPoint\n" +
                    "from allTeam\n" +
                    "group by teamID, teamName\n" +
                    "order by teamID desc;",

            "select player.playerID, playerName\n" +
                    "from player\n" +
                    "join signed on player.playerID = signed.playerID\n" +
                    "join team on team.teamID = signed.teamID\n" +
                    "group by player.playerID, playerName\n" +
                    "having count(team.teamID) = 1",

            "SELECT player.playerName, teamName, compete.season_year from player\n" +
                    "join season on season.playerID = player.playerID\n" +
                    "join compete on player.playerID = compete.playerID \n" +
                    "and season.season_year = compete.season_year\n" +
                    "join team on team.teamID = compete.teamID\n" +
                    "where playerName = ?;",

            "SELECT teamName, conference\n" +
                    "from team\n" +
                    "join conference on conference.conferenceID = team.conferenceID\n" +
                    "ORDER by conference",

            "select top 5 teamName,astHome\n" +
                    "from gameData\n" +
                    "join team on team.teamID = gameData.homeTeamID\n" +
                    "join conference on conference.conferenceID = team.conferenceID\n" +
                    "where conference.conference like '%east%'\n"+
                    "group by teamName,astHome\n" +
                    "order by astHome desc",

            "select teamName,sum(homeTeamWins) as totalWins\n" +
                    "from gameData\n" +
                    "join team on team.teamID = gameData.homeTeamID\n" +
                    "group by teamName\n" +
                    "order by totalWins DESC"
        };

        allQueries = theQueries;
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

        username = (prop.getProperty("username"));
        password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }
    }
    private void insertAllButtons(){
        setAllButtonsTitle();
        allButtons = new JButton[totalQueries];
        int xPos = 0;
        int yPos = 0;
        int buttonHeight = frameHeight/allButtons.length;
        int buttonWidth = frameWidth;

        for(int i=0; i<allButtons.length; i++){
            allButtons[i] = new JButton(buttonTitle[i]);
            allButtons[i].setBounds(xPos, yPos, buttonWidth, buttonHeight);
            allButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            allButtons[i].setFocusable(false);
            panel.add(allButtons[i]);
            yPos += buttonHeight;
        }
    }
    public void runQuery(int index) {
        try {
            selectSql = allQueries[index];
            connectionUrl =
                    "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                            + "database=cs3380;"
                            + "user=" + username + ";"
                            + "password=" + password + ";"
                            + "encrypt=false;"
                            + "trustServerCertificate=false;"
                            + "loginTimeout=30;";

            connection = DriverManager.getConnection(connectionUrl);
            PreparedStatement statement = connection.prepareStatement(selectSql);

            ResultSet resultSet = null;

            if(askInput){
                statement.setString(1,userInput);
            }
            // Create and execute a SELECT SQL statement.
            resultSet = statement.executeQuery();

            makeTable(resultSet, index);

            askInput = false;

            connection.close();
            statement.close();

        } catch (Exception s) {
            s.printStackTrace();
        }
    }
    private String askInputUser(){
        return JOptionPane.showInputDialog("Please Enter A Name");
    }
    public void actionPerformed(ActionEvent e){
        int index = -1;

        for(int i=0; i<allButtons.length; i++){
            if(e.getSource().equals(allButtons[i])){
                index = i;
            }
        }
        if(index == 3){
            askInput = true;
            userInput = askInputUser();
        }
        if(tableOpened){
            tableWindow.dispose();
            tableOpened = false;
        }
        runQuery(index);
    }
    public void makeTable(ResultSet resultSet, int index) throws Exception {
        tableOpened = true;
        JTable table;

        tableWindow = new JFrame(buttonTitle[index]);

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

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        tableWindow.setLocation(dim.width/2-tableWindow.getSize().width/2, dim.height/2-tableWindow.getSize().height/2);
        // Frame Visible = true
        tableWindow.setVisible(true);
    }
    public void setUsername(String theUsername){
        username = theUsername;
    }
    public void setPassword(String thePassword){
        password = thePassword;
    }
}