import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Season {
    public ArrayList<Game> games = new ArrayList<Game>();
    public ArrayList<Player> players = new ArrayList<Player>();
    
    public void addGame(Game game){
        if  (games.contains(game)) return;
        games.add(game);
        for (PlayerStats ps : game.teamstats1.getPlayerStats()) {
            Player player = ps.player;
            player.points += ps.getPoints();
            player.rebounds += ps.getRebounds();
            player.assists += ps.getAssists();
            player.cumBPER += ps.calculateBPER();
            player.gamesPlayed++;
        }
        for (PlayerStats ps : game.teamstats2.getPlayerStats()) {
            Player player = ps.player;
            player.points += ps.getPoints();
            player.rebounds += ps.getRebounds();
            player.assists += ps.getAssists();
            player.cumBPER += ps.calculateBPER();
            player.gamesPlayed++;
        }
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

    public Player findMVP() {
        if (players.isEmpty()) return null;

        Player mvp = null;
        double maxAveBPER = Double.NEGATIVE_INFINITY;

        for (Player p : players) {
            if (p.gamesPlayed == 0) continue;
            double aveBPER = p.cumBPER / p.gamesPlayed;
            if (aveBPER > maxAveBPER) {
                maxAveBPER = aveBPER;
                mvp = p;
            }
        }

        return mvp;
    }

    public void updateStats() {
        if (games.isEmpty()) return;

        findPlayers();

        for (Player p : players) {
            p.points = 0;
            p.rebounds = 0;
            p.assists = 0;
            p.cumBPER = 0;
            p.gamesPlayed = 0;
        }

        for (Game game : games) {
            for (PlayerStats ps : game.teamstats1.getPlayerStats()) {
                Player player = ps.player;
                if (!players.contains(player)) continue;

                player.points += ps.getPoints();
                player.rebounds += ps.getRebounds();
                player.assists += ps.getAssists();
                player.cumBPER += ps.calculateBPER();
                player.gamesPlayed++;
            }

            for (PlayerStats ps : game.teamstats2.getPlayerStats()) {
                Player player = ps.player;
                if (!players.contains(player)) continue;

                player.points += ps.getPoints();
                player.rebounds += ps.getRebounds();
                player.assists += ps.getAssists();
                player.cumBPER += ps.calculateBPER();
                player.gamesPlayed++;
            }
        }
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

    public void exportSeasonReportCSV(String filename, Team[] teams) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write CSV header
            writer.write("Team,Record,Player,BPER,PTS,REB,AST,STL\n");

            for (Team team : teams) {
                String teamName = team.getName();
                String record = String.format("%d-%d", team.wins, team.gamesPlayed - team.wins);

                Player[] teamPlayers = team.getRoster().players;
                if (teamPlayers == null || teamPlayers.length == 0) {
                    continue;
                }

                for (Player p : teamPlayers) {
                    if (p == null || p.gamesPlayed == 0) continue;

                    double ppg = (double) p.points / p.gamesPlayed;
                    double rpg = (double) p.rebounds / p.gamesPlayed;
                    double apg = (double) p.assists / p.gamesPlayed;
                    double spg = (double) p.steals / p.gamesPlayed;
                    double bper = p.cumBPER / p.gamesPlayed;

                    String playerName = escapeCSV(p.getName());

                    writer.write(String.format("%s,%s,%s,%.2f,%.1f,%.1f,%.1f,%.1f\n",
                            escapeCSV(teamName),
                            record,
                            playerName,
                            bper,
                            ppg,
                            rpg,
                            apg,
                            spg));
                }
            }

            System.out.println("CSV season report written to: " + filename);

        } catch (IOException e) {
            System.err.println("Failed to write CSV season report: " + e.getMessage());
        }
    }
    
    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
