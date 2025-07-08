import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.ArrayList;



public class GUI {
    String title = "Hoops Simulator 0.0.4";
    public static final int FREE_AGENTS = 1000;

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

    private JScrollPane freeAgentScrollPane;
    private JList<Player> freeAgentList;
    Player[] freeAgents;

    private JButton btnAddFreeAgent;

    private JPanel buttonPanel = new JPanel(new GridLayout(4,4,2,2));

    public GUI(){
        gameManager = new GameManager();
        backFrame = new JFrame();
        mainPanel = new JPanel();

        taMatchup = new JTextArea();
        //taMatchup.setLineWrap(true);
        taMatchup.setWrapStyleWord(true);
        taMatchup.setEditable(false);
        taMatchup.setFont(new Font("Monospaced",Font.PLAIN,12));
        gameScrollPane = new JScrollPane(taMatchup);
        gameScrollPane.setPreferredSize(new Dimension(720, 240));

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

        btnSimulate = new JButton("Simulate Game");
        btnSimulate.addActionListener(e -> simulateGame());

        matchupRecord = new JLabel();

        randomizeTeams();
        teamList = new JList<>(teams);
        teamScrollPane = new JScrollPane(teamList);
        teamScrollPane.setPreferredSize(new Dimension(240, 240));
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
                label.setText(team.getName() + " (" + team.wins + "-" + (team.gamesPlayed - team.wins) + ")");
                
                return label;
            }
        });
                
        gameList = new JList<>(gameManager.games.toArray(new Game[0]));
        gameLog = new JScrollPane(gameList);
        gameLog.setPreferredSize(new Dimension(320, 320));
        gameList.addListSelectionListener(e -> {
            if (!gameList.isSelectionEmpty()){
                taMatchup.setText(gameList.getSelectedValue().toString(true));
            }
            taMatchup.setCaretPosition(0);
        });

        playerList = new JList<>();
        teamView = new JScrollPane(playerList);
        teamView.setPreferredSize(new Dimension(240, 240));
        teamList.addListSelectionListener(e -> updateTeamView());

        attributeList = new JList<>();
        playerView = new JScrollPane(attributeList);
        playerView.setPreferredSize(new Dimension(240, 240));
        playerList.addListSelectionListener(e -> {
            updatePlayerView(0);
        });


        
        populateFreeAgents();

        freeAgentList = new JList<>(freeAgents);
        freeAgentScrollPane = new JScrollPane(freeAgentList);
        freeAgentScrollPane.setPreferredSize(new Dimension(240, 240));
        freeAgentList.addListSelectionListener(e -> {
            updatePlayerView(1);
        });

        freeAgentList.setCellRenderer(new DefaultListCellRenderer() {
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

        playerArchetype = new JLabel();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color((float) (0.5 + 0.5 * Math.random()), (float) (0.5 + 0.5 * Math.random()),(float) (0.5 + 0.5 * Math.random())));

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("ctrl/HoopsSimulator.png"));
        Image image = icon.getImage();
        backFrame.setIconImage(image);

        JLabel iconImage = new JLabel(icon);

        btnPlayAll = new JButton("Simulate round robin");
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

        btnAddFreeAgent = new JButton("Add Free Agent");
        btnAddFreeAgent.addActionListener(e -> addFreeAgent());

        
        buttonPanel.add(btnSimulate);
        buttonPanel.add(btnPlayAll);
        buttonPanel.add(btnPerf);
        buttonPanel.add(btnMVP);
        
        buttonPanel.add(btnSetTeam1);
        buttonPanel.add(btnSetTeam2);
        
        buttonPanel.add(btnAddFreeAgent);

        addElement(iconImage, 0, 0);
        addElement(teamScrollPane,2,0);
        addElement(teamView, 3, 0);
        addElement(playerView, 4, 0);
        addElement(gameScrollPane, 1, 4);
        addElement(playerArchetype, 4, 1);
        addElement(gameLog, 1, 5);
        addElement(matchupRecord, 1, 2);
        addElement(taBest, 2, 4);
        addElement(freeAgentScrollPane, 0, 4);

        addElement(buttonPanel, 0, 1);

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

    /**
     * 
     * @param mode 0 == team mode, 1 == free agent mode
     */
    private void updatePlayerView(int mode){
        boolean teamSelected = playerList.getSelectedValue() != null;
        boolean faSelected = freeAgentList.getSelectedValue() != null;
        if (!teamSelected && !faSelected){
            attributeList.setListData(new Attribute[0]);
            return;
        }
        
        JList<Player> list;
        switch (mode){
            case 0:
                list = playerList;
                break;
            case 1:
                list = freeAgentList;
                break;
            default: return;
        }
        if (list.getSelectedValue() == null){
            return;
        }
        Attribute[] attributes = list.getSelectedValue().attributes.toArray(new Attribute[0]);
        

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
        updatePlayerArchetype(mode);
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

    /**
     * 
     * @param mode 0 == team mode, 1 == free agent mode
     */
    private void updatePlayerArchetype(int mode){
        JList<Player> list;
        switch (mode){
            case 0:
                list = playerList;
                break;
            case 1:
                list = freeAgentList;
                break;
            default: return;
        }

        playerArchetype.setText(Archetype.findArchetype(list.getSelectedValue()).name());
    }

    private void populateFreeAgents(){
        freeAgents = new Player[FREE_AGENTS];
        for (int i = 0; i < FREE_AGENTS; i++){
            freeAgents[i] = Player.randomPlayer(Role.ATH, Attribute.ATTRIBUTE_MAX - (Attribute.ATTRIBUTE_MAX * i) / FREE_AGENTS);
        }
        
    }

    private void addFreeAgent(){
        Team team = teamList.getSelectedValue();
        Player player = playerList.getSelectedValue();
        Player freeAgent = freeAgentList.getSelectedValue();
        if (team == null || player == null || freeAgent == null){
            System.out.println("DEBUG");
            return;   
        }
        
        freeAgents[freeAgentList.getSelectedIndex()] = team.getRoster().replacePlayer(team.getRoster().getPosition(player), freeAgent);
        updateTeamView();
        freeAgentList.setListData(freeAgents);
        playerList.setSelectedValue(freeAgent, true);
        freeAgentList.setSelectedValue(player, true);
    }
    
}
