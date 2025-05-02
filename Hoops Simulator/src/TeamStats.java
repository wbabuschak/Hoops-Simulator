import java.util.ArrayList;

/**
 * TeamStats represents a team's stats across a single game, only to be edited through Game instances
 */
public class TeamStats {
    private Team team;
    private ArrayList<PlayerStats> playerStats;
    private int score;

    public boolean changeScore(ShotAttempt shotAttempt){
        //if (shotAttempt.make() != 0){System.out.println(shotAttempt.getShooter().getName() + " scored " + shotAttempt.make() + "!");}
        score += shotAttempt.make();
        return shotAttempt.make() == 0;
    }

    public int getScore(){
        return score;
    }

    public TeamStats(Team team){
        this.team = team;
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

    public String toString(){
        String string = "";
        for (int i = 0; i < team.getRoster().noPlayers(); i++){
            string += String.format("%-24s",team.getRoster().getPlayer(i).getName());
            string += " ";
            string += playerStats.get(i).toString();
            string += "\n";
        }
        return string;
    }


}
