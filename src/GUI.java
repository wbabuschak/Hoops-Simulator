import java.awt.*;
import javax.swing.*;
import java.text.*;



public class GUI {
    String title = "Hoops Simulator 0.0.2";

    private int noTeams = 16;

    private Team team1;
    private Team team2;

    private JFrame backFrame;
    private JPanel mainPanel;

    private JButton btnSetTeam1;
    private JButton btnSetTeam2;
    private JTextArea taMatchup;

    private JButton btnSimulate;
    private JScrollPane gameScrollPane;

    private JScrollPane teamScrollPane;
    private Team[] teams;
    private JList<Team> teamList;

    private JButton btnTeamView;
    private JScrollPane teamView;
    private Player[] players;
    private JList<Player> playerList;


    public GUI(){
        backFrame = new JFrame();
        mainPanel = new JPanel();

        taMatchup = new JTextArea();
        taMatchup.setLineWrap(true);
        taMatchup.setWrapStyleWord(true);
        taMatchup.setEditable(false);
        gameScrollPane = new JScrollPane(taMatchup);
        gameScrollPane.setPreferredSize(new Dimension(640, 480));

        btnSetTeam1 = new JButton("Set Home Team");
        btnSetTeam1.addActionListener(e -> {
            if (teamList.getSelectedValue() != null){
                team1 = teamList.getSelectedValue();
                if (team2 == null){
                    taMatchup.setText("Away Team vs " + team1);
                    return;
                }
                taMatchup.setText(team2 + " vs " + team1);
            }
        });

        btnSetTeam2 = new JButton("Set Away Team");
        btnSetTeam2.addActionListener(e -> {
            if (teamList.getSelectedValue() != null){
                team2 = teamList.getSelectedValue();
                if (team1 == null){
                    taMatchup.setText(team2 + " vs Home Team");
                    return;
                }
                taMatchup.setText(team2 + " vs " + team1);
            }
        });

        btnSimulate = new JButton("Simulate!");
        btnSimulate.addActionListener(e -> simulateGame());

        
        randomizeTeams();
        teamList = new JList<>(teams);
        teamScrollPane = new JScrollPane(teamList);
        teamScrollPane.setPreferredSize(new Dimension(300, 240));
        

        playerList = new JList<>();
        teamView = new JScrollPane(playerList);
        teamView.setPreferredSize(new Dimension(300, 240));

        teamList.addListSelectionListener(e -> updateTeamView());

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        mainPanel.setLayout(new GridBagLayout());

        addElement(btnSimulate,0,0);
        addElement(teamScrollPane,1,0);
        addElement(teamView, 2, 0);
        addElement(btnSetTeam1, 1, 1);
        addElement(btnSetTeam2, 1, 2);
        addElement(gameScrollPane, 0, 1);

        backFrame.add(mainPanel, BorderLayout.CENTER);
        backFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backFrame.setTitle(title);
        backFrame.pack();
        backFrame.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ctrl/HoopsSimulator.png"));
        Image image = icon.getImage();
        backFrame.setIconImage(image);

        
        backFrame.setVisible(true);

    }

    private void randomizeTeams(){
        teams = new Team[noTeams];
        for (int i = 0; i < noTeams; i++){
            teams[i] = Team.randomTeam();
        }
    }

    private void addElement(JComponent component, int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = x;
        gbc.gridy = y;
        
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(component,gbc);
    }

     private void setSize(JComponent component, int size){
        switch (size){
            case 0:
                component.setPreferredSize(new Dimension(350, 75));
                component.setMinimumSize(new Dimension(250, 50));
                break;
            case 1:
                component.setPreferredSize(new Dimension(350, 50));
                component.setMinimumSize(new Dimension(250, 40));
                break;
            default: 
                component.setPreferredSize(new Dimension(350, 60));
                component.setMinimumSize(new Dimension(250, 50));
                break;
        }
    }

    private void updateTeamView(){
        if (teamList.getSelectedValue() == null){
            return;
        }
        Player[] players = teamList.getSelectedValue().getRoster().players;
        playerList.setListData(players);
        
    }

    private void simulateGame(){
        Game game = new Game(team1, team2);
        game.playGame();
        taMatchup.setText(game.toString(true));
        taMatchup.setCaretPosition(0);
    }
}
