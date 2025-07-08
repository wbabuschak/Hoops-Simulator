import java.util.ArrayList;

public class GameManager {
    public ArrayList<Game> games = new ArrayList<Game>();
    public ArrayList<Player> players = new ArrayList<Player>();
    
    public void addGame(Game game){
        games.add(game);
    }

    public void playAll(Team[] teams){
        for (int i = 0; i < teams.length; i++){
            for (int j = i + 1; j < teams.length; j++){
                Game game = new Game(teams[i], teams[j]);
                game.playGame();
                addGame(game);
            }
        }
    }

    public void findPlayers(){
        players = new ArrayList<Player>();
        for (int i = 0; i < games.size(); i++){
            for (int j = 0; j < games.get(i).team1.roster.noPlayers(); j++){
                if (!players.contains(games.get(i).team1.roster.getPlayer(j))){
                    players.add(games.get(i).team1.roster.getPlayer(j));
                }
            }
            for (int j = 0; j < games.get(i).team2.roster.noPlayers(); j++){
                if (!players.contains(games.get(i).team2.roster.getPlayer(j))){
                    players.add(games.get(i).team2.roster.getPlayer(j));
                }
            }
        }
    }

    /**
     * NYI
     * @return
     */
    public Player findMVP(){
        if (games.isEmpty()) return null;
        // find and order every player
        findPlayers();
        // attribute every player's stats and games played
        int[] gamesPlayed = new int[players.size()];
        double[] totalBPER = new double[players.size()];
        for (int i = 0; i < games.size(); i++){
            for (int j = 0; j < games.get(i).teamstats1.getPlayerStats().size(); j++){
                Player playerWithStats = games.get(i).teamstats1.getPlayerStats().get(j).player;
                if (players.indexOf(playerWithStats) == -1){
                    continue;
                }
                gamesPlayed[players.indexOf(playerWithStats)]++;
                totalBPER[players.indexOf(playerWithStats)] += games.get(i).teamstats1.getPlayerStats().get(j).calculateBPER();
                playerWithStats.points += games.get(i).teamstats1.getPlayerStats().get(j).getPoints();
                playerWithStats.rebounds += games.get(i).teamstats1.getPlayerStats().get(j).getRebounds();
                playerWithStats.assists += games.get(i).teamstats1.getPlayerStats().get(j).getAssists();
                playerWithStats.gamesPlayed++;
            }
            for (int j = 0; j < games.get(i).teamstats2.getPlayerStats().size(); j++){
                Player playerWithStats = games.get(i).teamstats2.getPlayerStats().get(j).player;
                if (players.indexOf(playerWithStats) == -1){
                    continue;
                }
                gamesPlayed[players.indexOf(playerWithStats)]++;
                totalBPER[players.indexOf(playerWithStats)] += games.get(i).teamstats2.getPlayerStats().get(j).calculateBPER();
                playerWithStats.points += games.get(i).teamstats2.getPlayerStats().get(j).getPoints();
                playerWithStats.rebounds += games.get(i).teamstats2.getPlayerStats().get(j).getRebounds();
                playerWithStats.assists += games.get(i).teamstats2.getPlayerStats().get(j).getAssists();
                playerWithStats.gamesPlayed++;
            }
        }
        int maxIndex = 0;
        double maxAveBPER = totalBPER[0] / gamesPlayed[0];
        for (int i = 1; i < players.size(); i++){
            double currentAveBPER = totalBPER[i] / gamesPlayed[i];
            if (currentAveBPER > maxAveBPER){
                maxAveBPER = currentAveBPER;
                maxIndex = i;
            }
        }
        return players.get(maxIndex);
    }

    public PlayerStats findBestPerformance(){
        if (games.isEmpty()) return null;
        findPlayers();
        PlayerStats bestPerformance = games.get(0).teamstats1.getPlayerStats().get(0);
        for (int i = 1; i < games.size(); i++){
            for (int j = 0; j < games.get(i).team1.getRoster().noPlayers(); j++){
                if (games.get(i).teamstats1.getPlayerStats().get(j).calculateBPER() > bestPerformance.calculateBPER()){
                    bestPerformance = games.get(i).teamstats1.getPlayerStats().get(j);
                }
            }
            for (int j = 0; j < games.get(i).team2.getRoster().noPlayers(); j++){
                if (games.get(i).teamstats2.getPlayerStats().get(j).calculateBPER() > bestPerformance.calculateBPER()){
                    bestPerformance = games.get(i).teamstats2.getPlayerStats().get(j);
                }
            }
        }
        return bestPerformance;
    }
}
