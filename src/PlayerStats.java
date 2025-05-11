import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlayerStats {
    private Player player;
    ArrayList<ShotAttempt> shotAttempts;
    private int assists;
    private int turnovers;
    private int oRebounds;
    private int dRebounds;

    public int madeThrees(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).make() == 3){
                cnt++;
            }
        }
        return cnt;
    }

    public int fourPointPlays(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).make() == 3 && shotAttempts.get(i).getFouled() == true){
                cnt++;
            }
        }
        return cnt;
    }

    public int attemptedThrees(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).getCourtLocation() == CourtLocations.THREE){
                cnt++;
            }
        }
        return cnt;
    }

    public int fieldGoalsAttempted(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).getCourtLocation() != CourtLocations.FT) cnt++;
        }
        return cnt;
    }

    public int fieldGoalsMade(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).make() > 1){
                cnt++;
            }
        }
        return cnt;
    }

    public int freeThrowsAttempted(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).getCourtLocation() == CourtLocations.FT) cnt++;
        }
        return cnt;
    }

    public int freeThrowsMade(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).make() == 1){
                cnt++;
            }
        }
        return cnt;
    }
    

    public PlayerStats(Player player){ 
        this.player = player;
        shotAttempts = new ArrayList<ShotAttempt>();
        assists = 0;
        turnovers = 0;
    }

    public void logShot(ShotAttempt shotAttempt){
        shotAttempts.add(shotAttempt);
    }

    public int getPoints(){
        int sum = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            sum += shotAttempts.get(i).make();
        }
        return sum;
    }

    public void addAssist(){
        assists++;
    }

    public int getAssists(){
        return assists;
    }

    public int getTurnovers(){
        return turnovers;
    }

    public int getORebounds(){
        return oRebounds;
    }

    public int getDRebounds(){
        return dRebounds;
    }

    public int getRebounds(){
        return oRebounds + dRebounds;
    }

    public void incrementORebound(){
        oRebounds++;
    }

    public void incrementDRebound(){
        dRebounds++;
    }

    public double calculateBPER(){
        int FGA = fieldGoalsAttempted();
        int FTA = shotAttempts.size() - FGA;
        int PTS = getPoints();
        int OREB = getORebounds();
        int DREB = getDRebounds();
        int ASS = getAssists();
        int TO = getTurnovers();

        return (double) (2 * PTS - 0.5 * FTA - FGA + 0.5 * DREB + 1.5 * OREB + ASS - 2.0 * TO);
    }
    
    public String toString(){
        String string = "BPER: ";
        string += String.format("%-6s", new DecimalFormat("0.00").format(calculateBPER()));
        string += " | ";
        String slashline = String.valueOf(getPoints());
        slashline += "/";
        slashline += getRebounds();
        slashline += "/";
        slashline += getAssists();
        int threes = madeThrees();
        string += String.format("%-9s", slashline);
        if (fieldGoalsAttempted() > 0 || freeThrowsAttempted() > 0){
            String fg = " | ";
            fg += fieldGoalsMade();
            fg += "/";
            fg += fieldGoalsAttempted();
            fg += " FG";
            string += fg;
        }

        if (freeThrowsAttempted() > 0){
            String ft = ", ";
            ft += freeThrowsMade();
            ft += "/";
            ft += freeThrowsAttempted();
            ft += " FT";
            string += ft;
        }
        
        if (madeThrees() > 0){
            string += ", ";
            string += threes;
            string += " 3pt";
        }

        if (getORebounds() > 0){
            string += ", ";
            string += getORebounds();
            string += " OREB";
        }
        

        return string;
    }

    public void addShotAttempt(ShotAttempt shotAttempt){
        shotAttempts.add(shotAttempt);
    }
}
