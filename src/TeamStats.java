import java.util.ArrayList;

/**
 * TeamStats represents a team's stats across a single game, only to be edited through Game instances
 */
public class TeamStats {
    private Team team;
    private ArrayList<PlayerStats> playerStats;
    private int score;
    private int turnovers;

    public boolean changeScore(ShotAttempt shotAttempt){
        //if (shotAttempt.make() != 0){System.out.println(shotAttempt.getShooter().getName() + " scored " + shotAttempt.make() + "!");}
        score += shotAttempt.make();
        return shotAttempt.make() != 0;
    }

    public int getScore(){
        return score;
    }

    public TeamStats(Team team){
        this.team = team;
        turnovers = 0;
        playerStats = new ArrayList<PlayerStats>();
        for (int i = 0; i < team.getRoster().noPlayers(); i++){
            playerStats.add(new PlayerStats(team.getRoster().getPlayer(i)));
        }
    }

    public ArrayList<PlayerStats> getPlayerStats(){
        return playerStats;
    }

    public PlayerStats getStatsFromPlayer(Player player){
        int pos = team.getRoster().getPosition(player);
        return playerStats.get(pos);
    }

    public int teamRebouds(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).getRebounds();
        }
        return cnt;
    }

    public int teamFGA(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).fieldGoalsAttempted();
        }
        return cnt;
    }

    public int teamFGM(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).fieldGoalsMade();
        }
        return cnt;
    }

    public int team3PM(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).madeThrees();
        }
        return cnt;
    }

    public int team3PA(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).attemptedThrees();
        }
        return cnt;
    }

    public int team4PP(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).fourPointPlays();
        }
        return cnt;
    }

    public int teamAssists(){
        int cnt = 0;
        for (int i = 0; i < playerStats.size(); i++){
            cnt += playerStats.get(i).getAssists();
        }
        return cnt;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        // --- HEADER ---
        sb.append(String.format("%-30s %-12s %-3s %s%n",
                "Player (OVR)",
                "Minutes",
                "|",
                "Player Stats"));

        sb.append(String.format("%s%n", "-".repeat(70)));

        // --- PLAYER LINES ---
        for (int i = 0; i < team.getRoster().noPlayers(); i++) {
            Player player = team.getRoster().getPlayer(i);
            String nameOverall = player.getName() + " (" + (int) player.overall() + ")";

            sb.append(String.format("%-32s", nameOverall));
            sb.append(String.format("%-12s", "Minutes: " + playerStats.get(i).getMinutes()));

            if (team.getRosterMinutes()[i] == 0) {
                sb.append("\n");
                continue;
            }

            sb.append(String.format("%-3s", "|"));
            sb.append(playerStats.get(i).toString());
            sb.append("\n");
        }

        // --- TEAM TOTALS LINE ---
        sb.append("-".repeat(70)).append("\n");
        sb.append(String.format(
                "%2s team TO | %2s team Assists | %2s team Rebounds | %7s FGA | %2s/%2s 3pts | %1s 4-point plays%n",
                getTurnovers(),
                teamAssists(),
                teamRebouds(),
                teamFGM() + "/" + teamFGA(),
                team3PM(), team3PA(),
                team4PP()
        ));

        return sb.toString();
    }


    public void turnover(){
        turnovers++;
    }

    public int getTurnovers(){
        return turnovers;
    }


}
