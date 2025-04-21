/**
 * TeamStats represents a team's stats across a single game, only to be edited through Game instances
 */
public class TeamStats {
    private Team team;
    private int score;

    public boolean changeScore(ShotAttempt shotAttempt){
        return shotAttempt.make() == 0;
    }

    public int getScore(){
        return score;
    }

    public TeamStats(Team team){
        this.team = team;
    }
}
