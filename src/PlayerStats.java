import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlayerStats {
    public Player player;
    ArrayList<ShotAttempt> shotAttempts;
    private int assists;
    private int turnovers;
    private int oRebounds;
    private int dRebounds;
    private int steals;
    private int blocks;
    
    public int getSteals() {
        return steals;
    }

    public void addSteal() {
        steals++;
    }


    public int minutes;

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

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
        int STL = getSteals();
        int BLK = getBlocks();

        return (double) (2 * PTS - 0.5 * FTA - FGA + 1.0 * DREB + 1.5 * OREB + 1.5 * ASS - 3.0 * TO + 2.0 * STL + 2.0 * BLK);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // BPER block
        double bper = calculateBPER();
        sb.append(String.format("BPER: %5.2f", bper));
        sb.append(" | ");

        // slash line: PTS/REB/AST
        String slashLine = String.format("%-8s", getPoints() + "/" + getRebounds() + "/" + getAssists());
        sb.append(slashLine);
        sb.append(" | ");

        // FG string
        String fgString = (fieldGoalsAttempted() > 0)
                ? String.format("%-10s", fieldGoalsMade() + "/" + fieldGoalsAttempted() + " FG")
                : String.format("%-10s", "");
        sb.append(fgString);
        sb.append(" | ");

        // FT string
        String ftString = (freeThrowsAttempted() > 0)
                ? String.format("%-8s", freeThrowsMade() + "/" + freeThrowsAttempted() + " FT")
                : String.format("%-8s", "");
        sb.append(ftString);
        sb.append(" | ");

        // 3pt string
        String threePtString = (madeThrees() > 0)
                ? String.format("%-8s", madeThrees() + " 3pt")
                : String.format("%-8s", "");
        sb.append(threePtString);
        sb.append(" | ");

        // BLK string
        String blocksString = (getBlocks() > 0)
                ? String.format("%-8s", getBlocks() + " BLK")
                : String.format("%-8s", "");
        sb.append(blocksString);
        sb.append(" | ");

        // STL string
        String stealsString = (getSteals() > 0)
                ? String.format("%-8s", getSteals() + " STL")
                : String.format("%-8s", "");
        sb.append(stealsString);
        sb.append(" | ");

        // OREB string
        String orebString = (getORebounds() > 0)
                ? String.format("%-8s", getORebounds() + " OREB")
                : String.format("%-8s", "");
        sb.append(orebString);

        return sb.toString().trim();
    }


    private int getBlocks() {
        return blocks;
    }

    public void addShotAttempt(ShotAttempt shotAttempt){
        shotAttempts.add(shotAttempt);
    }

    public void addBlock() {
       blocks++;
    }
}
