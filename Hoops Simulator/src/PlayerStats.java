import java.util.ArrayList;

public class PlayerStats {
    private Player player;
    ArrayList<ShotAttempt> shotAttempts;

    public int madeThrees(){
        int cnt = 0;
        for (int i = 0; i < shotAttempts.size(); i++){
            if (shotAttempts.get(i).make() == 3){
                cnt++;
            }
        }
        return cnt;
    }

    public int fieldGoalsAttempted(){
        return shotAttempts.size();
    }

    public int fieldGoalsMade(){
        int cnt = 0;
        for (int i = 0; i < fieldGoalsAttempted(); i++){
            if (shotAttempts.get(i).make() > 0){
                cnt++;
            }
        }
        return cnt;
    }

    public PlayerStats(Player player){ 
        this.player = player;
        shotAttempts = new ArrayList<ShotAttempt>();
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
    
    public String toString(){
        String slashline = String.valueOf(getPoints());
        slashline += "/0/0";
        int threes = madeThrees();
        String string = String.format("%-9s", slashline);
        if (fieldGoalsAttempted() > 0){
            String fg = " ";
            fg += fieldGoalsMade();
            fg += "/";
            fg += fieldGoalsAttempted();
            fg += " FG";
            if (threes > 0) fg += ", ";
            string += String.format("%-11s",fg);
        }
        
        if (madeThrees() > 0){
            string += threes;
            string += " 3pt";
        }
        return string;
    }

    public void addShotAttempt(ShotAttempt shotAttempt){
        shotAttempts.add(shotAttempt);
    }
}
