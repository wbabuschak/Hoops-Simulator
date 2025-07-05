import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.ArrayList;



public class GUI {
    String title = "Hoops Simulator 0.0.3";

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

    private JScrollPane gameLog;

    private JScrollPane teamScrollPane;
    private Team[] teams;
    private JList<Team> teamList;

    private JScrollPane teamView;
    private JList<Player> playerList;

    private JScrollPane playerView;
    private JList<Attribute> attributeList;

    private JLabel playerArchetype;
    
    private GameManager gameManager;
    private JList<Game> gameList;

    private JLabel matchupRecord;

    private JButton btnPlayAll;
    private JTextArea taBest;

    public GUI(){
        gameManager = new GameManager();
        backFrame = new JFrame();
        mainPanel = new JPanel();

        taMatchup = new JTextArea();
        taMatchup.setLineWrap(true);
        taMatchup.setWrapStyleWord(true);
        taMatchup.setEditable(false);
        taMatchup.setFont(new Font("Monospaced",Font.PLAIN,12));
        gameScrollPane = new JScrollPane(taMatchup);
        gameScrollPane.setPreferredSize(new Dimension(960, 240));

        btnSetTeam1 = new JButton("Set Home Team");
        btnSetTeam1.addActionListener(e -> {
            if (teamList.getSelectedValue() != null){
                team1 = teamList.getSelectedValue();
                if (team2 == null){
                    taMatchup.setText("Away Team vs " + team1);
                    return;
                }
                taMatchup.setText(team2 + " vs " + team1);
                updateMatchupLabel();
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
                updateMatchupLabel();
            }
        });

        btnSimulate = new JButton("Simulate!");
        btnSimulate.addActionListener(e -> simulateGame());

        matchupRecord = new JLabel();

        randomizeTeams();
        teamList = new JList<>(teams);
        teamScrollPane = new JScrollPane(teamList);
        teamScrollPane.setPreferredSize(new Dimension(200, 240));
        teamList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
                
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                
                Team team = (Team) value;
                label.setText(team.getName() + " (" + team.wins + ")");
                
                return label;
            }
        });
                
        gameList = new JList<>(gameManager.games.toArray(new Game[0]));
        gameLog = new JScrollPane(gameList);
        gameLog.setPreferredSize(new Dimension(320, 240));
        gameList.addListSelectionListener(e -> {
            taMatchup.setText(gameList.getSelectedValue().toString(true));
            taMatchup.setCaretPosition(0);
        });

        playerList = new JList<>();
        teamView = new JScrollPane(playerList);
        teamView.setPreferredSize(new Dimension(200, 240));
        teamList.addListSelectionListener(e -> updateTeamView());

        attributeList = new JList<>();
        playerView = new JScrollPane(attributeList);
        playerView.setPreferredSize(new Dimension(200, 240));
        playerList.addListSelectionListener(e -> updatePlayerView());
        playerList.addListSelectionListener(e -> updatePlayerArchetype());

        playerArchetype = new JLabel();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color((float) (0.5 + 0.5 * Math.random()), (float) (0.5 + 0.5 * Math.random()),(float) (0.5 + 0.5 * Math.random())));

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ctrl/HoopsSimulator.png"));
        Image image = icon.getImage();
        backFrame.setIconImage(image);

        JLabel iconImage = new JLabel(icon);

        btnPlayAll = new JButton("Play round robin");
        btnPlayAll.addActionListener(e -> {
            gameManager.playAll(teams);
            updateGameLog();
            teamList.setListData(teams);
            updateMatchupLabel();
        });
        
        taBest = new JTextArea();
        taBest.setEditable(false);
        taBest.setLineWrap(true);
        taBest.setWrapStyleWord(true);
        taBest.setPreferredSize(new Dimension(200, 100));
        JButton btnMVP = new JButton("Find MVP");
        
        btnMVP.addActionListener(
            e -> {
                Player MVP = gameManager.findMVP();
                if (MVP == null){
                    taBest.setText("");
                    return;
                }
                int gamesPlayed = MVP.gamesPlayed;
                taBest.setText(MVP.toString() + " " + (int) (MVP.points / gamesPlayed) + "/" + (int) (MVP.rebounds / gamesPlayed) + "/" + (int) (MVP.assists / gamesPlayed));
            });

        JButton btnPerf = new JButton("Find Best Performance");
        btnPerf.addActionListener(
            e -> {
                PlayerStats best = gameManager.findBestPerformance();
                if (best == null){
                    taBest.setText("");
                    return;
                }
                Player bestPlayer = best.player;
                taBest.setText(bestPlayer.toString() + " " + (best));
            });

        addElement(iconImage, 0, 0);
        addElement(btnSimulate,0,1);
        addElement(teamScrollPane,1,0);
        addElement(teamView, 2, 0);
        addElement(playerView, 3, 0);
        addElement(btnSetTeam1, 1, 1);
        addElement(btnSetTeam2, 1, 2);
        addElement(gameScrollPane, 0, 4);
        addElement(playerArchetype, 3, 1);
        addElement(gameLog, 0, 5);
        addElement(matchupRecord, 0, 2);
        addElement(btnPlayAll, 1, 3);
        addElement(btnMVP, 2, 2);
        addElement(taBest, 2, 3);
        addElement(btnPerf, 2, 1);

        backFrame.add(mainPanel, BorderLayout.CENTER);
        backFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backFrame.setTitle(title);
        
        backFrame.pack();
        backFrame.setLocationRelativeTo(null);

        

        
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

        playerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Player player = (Player) value;
                if (!isSelected) {
                    label.setBackground(new Color((int) (255 - (128 * player.overall()/Attribute.ATTRIBUTE_MAX)), (int) (128 + (127 * player.overall()/Attribute.ATTRIBUTE_MAX)),0));
                    //System.out.println((int) (255 - (128 * player.overall()/Attribute.ATTRIBUTE_MAX)) + ", " + (int) (128 + (127 * player.overall()/Attribute.ATTRIBUTE_MAX)));
                    label.setOpaque(true);
                }

                return label;
            }
        });
    }

    private void updatePlayerView(){
        if (playerList.getSelectedValue() == null){
            attributeList.setListData(new Attribute[0]);
            return;
        }
        Attribute[] attributes = playerList.getSelectedValue().attributes.toArray(new Attribute[0]);
        attributeList.setListData(attributes);

        attributeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Attribute att = (Attribute) value;
                if (!isSelected) {
                    label.setBackground(new Color((int) (255 - (128 * att.getValue()/Attribute.ATTRIBUTE_MAX)), (int) (128 + (127 * att.getValue()/Attribute.ATTRIBUTE_MAX)),0));
                    //System.out.println((int) (255 - (128 * att.getValue()/Attribute.ATTRIBUTE_MAX)) + ", " + (int) (128 + (127 * att.getValue()/Attribute.ATTRIBUTE_MAX)));
                    label.setOpaque(true);
                }

                return label;
            }
        });
    }

    private void simulateGame(){
        if (team1 == team2 || team1 == null || team2 == null){
            return;
        }
        Game game = new Game(team1, team2);
        game.playGame();
        gameManager.addGame(game);
        updateGameLog();
        taMatchup.setText(game.toString(true));
        taMatchup.setCaretPosition(0);
        updateMatchupLabel();
    }

    private void updateMatchupLabel(){
        int i1 = 0;
        int i2 = 0;
        if (team1 == team2 || team1 == null || team2 == null){
            matchupRecord.setText(" - ");
            return;
        }
        for (int i = 0; i < gameManager.games.size(); i++){
            if (gameManager.games.get(i).getWinner() == team1 && (gameManager.games.get(i).team1 == team2 || gameManager.games.get(i).team2 == team2)){
                i1++;
            } else if (gameManager.games.get(i).getWinner() == team2 && (gameManager.games.get(i).team1 == team1 || gameManager.games.get(i).team2 == team1)){
                i2++;
            }
        }
        matchupRecord.setText(i1 + "-" + i2);
    }

    private void updateGameLog(){
        gameList.setListData(gameManager.games.toArray(new Game[0]));
        gameList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Game game = (Game) value;
                if (!isSelected) {
                    if (game.getWinner() == game.team1){
                        label.setBackground(Color.LIGHT_GRAY);
                    } else {
                        label.setBackground(Color.WHITE);
                    }
                    
                    label.setOpaque(true);
                }

                return label;
            }
        });
        
        SwingUtilities.invokeLater(() -> {
            gameLog.getVerticalScrollBar().setValue(gameLog.getVerticalScrollBar().getMaximum());
        }); 
    }

    private void updatePlayerArchetype(){
        if (playerList.getSelectedValue() == null){
            playerArchetype.setText("");
            return;
        }
        playerArchetype.setText(Archetype.findArchetype(playerList.getSelectedValue()).name());
    }
    
}
