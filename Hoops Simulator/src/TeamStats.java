/**
 * TeamStats represents a team's stats across a single game, only to be edited through Game instances
 */
public class TeamStats {
    private Team team;
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
    }


}
