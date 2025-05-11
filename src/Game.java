public class Game {
    public static final int GAME_LENGTH = 48;
    public static final int SHOT_CLOCK_LENGTH = 24;
    public static final double MIN_PACE = 0.35;
    public static final double MAX_PACE = 0.90;
    public static final double PACE_SCALAR = 1.0;
    public static final double TURNOVER_SCALAR = 0.15;
    public static final double TURNOVER_RATE = 0.01;
    /**
     * A higher foul difficulty means a defender is less likely to foul
     */
    public static final double FOUL_DIFFICULTY = 20;

    /**
     * A higher three foul difficulty means a defender is less likely to foul on a three pointer
     */
    public static final double THREE_FOUL_DIFFICULTY = 0.6;


    Team team1;
    Team team2;
    TeamStats teamstats1;
    TeamStats teamstats2;
    int possessions;

    public Game(Team team1, Team team2){
        this.team1 = team1;
        this.team2 = team2;
    }

    public int getPossessions(){
        return possessions;
    }

    public int calculatePossessions(){
        return (int)(GAME_LENGTH * 2 * (60/(SHOT_CLOCK_LENGTH)) * Math.max(MIN_PACE,
        Math.min(
        (PACE_SCALAR * (team1.getRosterAttributeMean("Pace") + (team2.getRosterAttributeMean("Pace"))) / (2 * Attribute.ATTRIBUTE_MAX))
        ,MAX_PACE)));
    }

    public static void gameTest(int trials, double team1Shooting, double team1Defense, double team2Shooting, double team2Defense, boolean extra){
        if (trials < 1) return;
        Game game = new Game(Team.randomTeam(), Team.randomTeam());
        
        for (int i = 0; i < trials; i++){
            game = new Game(Team.randomTeam(), Team.randomTeam());
            if (!(team1Shooting == -1 || team1Defense == -1 ||  team2Shooting == -1|| team2Defense == -1)){
                for(int j = 0; j < game.team1.getRoster().noPlayers(); j++){
                    game.team1.getRoster().getPlayer(j).setAttributeValue("Paint D", team1Defense);
                    game.team1.getRoster().getPlayer(j).setAttributeValue("Perimeter D", team1Defense);
                    game.team1.getRoster().getPlayer(j).setAttributeValue("3pt", team1Shooting);
                    game.team1.getRoster().getPlayer(j).setAttributeValue("Rim Finishing", team1Shooting);
                    game.team1.getRoster().getPlayer(j).setAttributeValue("Midrange", team1Shooting);
                    
                }
                for(int j = 0; j < game.team2.getRoster().noPlayers(); j++){
                    game.team2.getRoster().getPlayer(j).setAttributeValue("Paint D", team2Defense);
                    game.team2.getRoster().getPlayer(j).setAttributeValue("Perimeter D", team2Defense);
                    game.team2.getRoster().getPlayer(j).setAttributeValue("3pt", team2Shooting);
                    game.team2.getRoster().getPlayer(j).setAttributeValue("Rim Finishing", team2Shooting);
                    game.team2.getRoster().getPlayer(j).setAttributeValue("Midrange", team2Shooting);
                    }
            }
           
            game.playGame();

            if (extra){
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println(game.toString(true));
            }
            

        }
    }


    public static void gameTest(int trials, boolean extra){
        gameTest(trials, -1, -1, -1, -1, extra);
    }

    public void playGame(){
        possessions = calculatePossessions();
        teamstats1 = new TeamStats(team1);
        teamstats2 = new TeamStats(team2);
        for (int i = 0; i < possessions; i++){
            runPossession();
        }
        
        // overtime
        while (teamstats1.getScore() == teamstats2.getScore()){
            possessions = (int)((GAME_LENGTH / 8) * (60/(SHOT_CLOCK_LENGTH/2)) * Math.max(MIN_PACE,
                Math.min(
                ((team1.getRosterAttributeMean("Pace") + (team2.getRosterAttributeMean("Pace"))) / (2 * Attribute.ATTRIBUTE_MAX))
                ,MAX_PACE)));
            for (int i = 0; i < possessions; i++){
                runPossession();
            }

        }
    }

    private void runPossession(){
        if (Math.random() > TURNOVER_RATE + TURNOVER_SCALAR * (Attribute.ATTRIBUTE_MAX - team1.getRosterAttributeMean("Offensive Discipline"))/Attribute.ATTRIBUTE_MAX){
            ShotAttempt attempt = takeShot(team1, team2);
            while (!teamstats1.changeScore(attempt) && !attempt.getFouled()) {
                if (!rebound(team1, teamstats1, team2, teamstats2)) {
                    break;
                }
                attempt = takeShot(team1, team2);
            }
            
        } else {
            teamstats1.turnover();
        }
        if (Math.random() > TURNOVER_RATE + TURNOVER_SCALAR * (Attribute.ATTRIBUTE_MAX - team2.getRosterAttributeMean("Offensive Discipline"))/Attribute.ATTRIBUTE_MAX){
            ShotAttempt attempt = takeShot(team2, team1);
            while (!teamstats2.changeScore(attempt) && !attempt.getFouled()) {
                if (!rebound(team2, teamstats2, team1, teamstats1)) {
                    break;
                }
                attempt = takeShot(team2, team1);
            }
        } else {
            teamstats2.turnover();
        }
    }

    /**
     * 
     * @param shootingTeam
     * @param defendingTeam
     * @return whether an offensive rebound is taken
     */
    private boolean rebound(Team shootingTeam, TeamStats shootingTeamStats, Team defendingTeam, TeamStats defendingTeamStats){
        Player oRebounder = shootingTeam.getRoster().getOffensiveRebounder();
        Player dRebounder = defendingTeam.getRoster().getDefensiveRebounder();
        if (oRebounder.getAttributeValue("Offensive Rebounding") * Math.random() > dRebounder.getAttributeValue("Defensive Rebounding") * 3.0 * Math.random()){
            shootingTeamStats.getPlayerStats().get(shootingTeam.getRoster().getPosition(oRebounder)).incrementORebound();
            return true;
        } else {
            defendingTeamStats.getPlayerStats().get(defendingTeam.getRoster().getPosition(dRebounder)).incrementDRebound();
        }
        return false;
    }
    

    private ShotAttempt takeShot(Team shootingTeam, Team defendingTeam){
        Player shootingPlayer = shootingTeam.getRoster().chooseShooterAtRandom();
        Player assister = shootingTeam.getRoster().getAssister();
        ShotAttempt shotAttempt = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, shootingPlayer.getShotLocation(), assister != null);
        int fts = foulChance(shotAttempt);
        if (shootingTeam == team1){
            if (fts == 0 || fts == 1){
                teamstats1.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(shotAttempt);
                if (shotAttempt.make() > 1 && assister != null) teamstats1.getPlayerStats().get(shootingTeam.getRoster().getPosition(assister)).addAssist();
            }
            for (int i = 0; i < fts; i++){
                ShotAttempt ft = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, CourtLocations.FT, false);
                teamstats1.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(ft);
                teamstats1.changeScore(ft);
            }
        } else if (shootingTeam == team2){
            if (fts == 0 || fts == 1){
                teamstats2.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(shotAttempt);
                if (shotAttempt.make() > 1 && assister != null) teamstats2.getPlayerStats().get(shootingTeam.getRoster().getPosition(assister)).addAssist();
            }
            for (int i = 0; i < fts; i++){
                ShotAttempt ft = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, CourtLocations.FT, false);
                teamstats2.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(ft);
                teamstats2.changeScore(ft);
            }
        } else {
            System.out.println("CRITICAL ERROR: shooting team not in game");
            return null;
        }
        return(shotAttempt);
    }

    /**
     * returns the number of FTs on a shot, better disciplined-defenders foul less, better shooters get more fouls
     * @param shotAttempt
     * @return number of FTs to be shot
     */
    private int foulChance(ShotAttempt shotAttempt){
        CourtLocations courtLocation = shotAttempt.getCourtLocation();
        boolean fouled;

        Player defender = shotAttempt.getDefendingTeam().getRoster().getDefender(courtLocation);
        Player shooter = shotAttempt.getShooter();
        double skill;
        double foulDifficulty = FOUL_DIFFICULTY;
        switch (courtLocation){
            case CourtLocations.PAINT: {
                skill = shooter.getAttributeValue("Contested Rim Finishing");
                break;
            }
            case CourtLocations.MIDRANGE: {
                skill = shooter.getAttributeValue("Contested Midrange");
                break;
            }
            case CourtLocations.THREE: {
                skill = shooter.getAttributeValue("Contested 3pt");
                foulDifficulty *= THREE_FOUL_DIFFICULTY;
                break;
            }
            default: return -1;
        }
        fouled = (defender.getAttributeValue("Defensive Discipline") * foulDifficulty * Math.random() < skill);
        if (!fouled) {
            shotAttempt.setFouled(false);
            return 0;
        }

        shotAttempt.setFouled(true);

        if (!hardFoul(defender, skill) && shotAttempt.make() != 0){
            return 1;
        }
        
        if (courtLocation == CourtLocations.THREE){
            return 3;
        } else {
            return 2;
        }
    }

    private boolean hardFoul(Player defender, double skill){
       return (defender.getAttributeValue("Hard Fouls") * Math.random() > skill * Math.random());
    }

    public Team getWinner(){
        return (teamstats1.getScore() > teamstats2.getScore()) ? team1 : team2;
    }

    public Player getHighScorer(){
        Player player = null;
        int max = 0;
        if (getWinner() == team1){
            for (int i = 0; i < team1.getRoster().noPlayers(); i++){
                int t = teamstats1.getPlayerStats().get(i).getPoints();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team1.getRoster().getPlayer(i);
                    max = t;
                }
            }
        } else if (getWinner() == team2){
            for (int i = 0; i < team2.getRoster().noPlayers(); i++){
                int t = teamstats2.getPlayerStats().get(i).getPoints();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team2.getRoster().getPlayer(i);
                    max = t;
                }
            }
        }
        return player;
    }

    public Player getPOTG(){
        Player player = null;
        double max = 0;
        if (getWinner() == team1){
            for (int i = 0; i < team1.getRoster().noPlayers(); i++){
                double t = teamstats1.getPlayerStats().get(i).calculateBPER();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team1.getRoster().getPlayer(i);
                    max = t;
                }
            }
        } else if (getWinner() == team2){
            for (int i = 0; i < team2.getRoster().noPlayers(); i++){
                double t = teamstats1.getPlayerStats().get(i).calculateBPER();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team2.getRoster().getPlayer(i);
                    max = t;
                }
            }
        }
        return player;
    }

    public String toString(){
        String string;
        if (teamstats1.getScore() == teamstats2.getScore()){
            string = "Game has not been played!";
        } else if (getWinner().equals(team1)){
             string = team1.getName();
            string += " won ";
            string += teamstats1.getScore(); 
            string += "-";
            string += teamstats2.getScore();
            string += "\n\t Player of the game: ";
            string += getPOTG().getName();
            string += " ";
            string += teamstats1.getStatsFromPlayer(getPOTG()).toString();
        } else {
            string = team2.getName();
            string += " won ";
            string += teamstats2.getScore(); 
            string += "-";
            string += teamstats1.getScore();
            string += "\n\t Player of the game: ";
            string += getPOTG().getName();
            string += " ";
            string += teamstats2.getStatsFromPlayer(getPOTG()).toString();
        }
        
        
        return string;
    }

    public String toString(boolean full){
        String string;
        if (!full){
            if (teamstats1.getScore() == teamstats2.getScore()){
                string = "Game has not been played!";
            } else if (getWinner().equals(team1)){
                string = team1.getName();
                string += " won ";
                string += teamstats1.getScore(); 
                string += "-";
                string += teamstats2.getScore();
            } else {
                string = team2.getName();
                string += " won ";
                string += teamstats2.getScore(); 
                string += "-";
                string += teamstats1.getScore();
            }
        } else {
            if (teamstats1.getScore() == teamstats2.getScore()){
                string = "Game has not been played!";
            } else if (getWinner().equals(team1)){
                string = team1.getName();
                string += " won ";
                string += teamstats1.getScore(); 
                string += "-";
                string += teamstats2.getScore();
                string += "\n\n";
                string += "Player of the game: ";
                string += getPOTG().getName();
                string += "\n\n";
                string += team1.getName();
                string += "\n\n";
                string += teamstats1.toString();
                string += "\n";
                string += team2.getName();
                string += "\n\n";
                string += teamstats2.toString();
            } else {
                string = team2.getName();
                string += " won ";
                string += teamstats2.getScore(); 
                string += "-";
                string += teamstats1.getScore();
                string += "\n\n";
                string += "Player of the game: ";
                string += getPOTG().getName();
                string += "\n\n";
                string += team2.getName();
                string += "\n\n";
                string += teamstats2.toString();
                string += "\n";
                string += team1.getName();
                string += "\n\n";
                string += teamstats1.toString();
            }
        } 
        
        
        
        return string;
    }

    
}


