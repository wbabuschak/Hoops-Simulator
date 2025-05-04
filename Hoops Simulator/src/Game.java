public class Game {
    public static final int GAME_LENGTH = 48;
    public static final int SHOT_CLOCK_LENGTH = 24;
    public static final double MIN_PACE = 0.35;
    public static final double MAX_PACE = 0.90;
    public static final double PACE_SCALAR = 1.0;
    public static final double TURNOVER_SCALAR = 0.15;
    public static final double TURNOVER_RATE = 0.01;


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
        
        int cnt1 = 0;
        int cnt2 = 0;
        int possessions = 0;
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
            
            possessions+= game.getPossessions();

            cnt1 += game.teamstats1.getScore();
            
            cnt2 += game.teamstats2.getScore();

            if (extra){
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println(game.toString(true));
            }
            

        }
        // System.out.println("team1Shooting " + team1Shooting + " team1Defense " + team1Defense + " team2Shooting " + team2Shooting + " team2Defense " + team2Defense + " Total score = " + cnt1 + " - " + cnt2);
        // System.out.println("team1 PPP = " + ((double) cnt1 / (double) possessions));
        // System.out.println("team2 PPP = " + ((double) cnt2 / (double) possessions));
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
            teamstats1.changeScore(takeShot(team1, team2));
        } else {
            teamstats1.turnover();
        }
        if (Math.random() > TURNOVER_RATE + TURNOVER_SCALAR * (Attribute.ATTRIBUTE_MAX - team2.getRosterAttributeMean("Offensive Discipline"))/Attribute.ATTRIBUTE_MAX){
            teamstats2.changeScore(takeShot(team2, team1));
        } else {
            teamstats2.turnover();
        }
    }

    private ShotAttempt takeShot(Team shootingTeam, Team defendingTeam){
        Player shootingPlayer = shootingTeam.getRoster().chooseShooterAtRandom();
        ShotAttempt shotAttempt = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, shootingPlayer.getShotLocation());
        if (shootingTeam == team1){
            teamstats1.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(shotAttempt);
        } else if (shootingTeam == team2){
            teamstats2.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer)).addShotAttempt(shotAttempt);
        } else {
            System.out.println("CRITICAL ERROR: shooting team not in game");
            return null;
        }
        return(shotAttempt);
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
                int t = teamstats1.getPlayerStats().get(i).getPoints();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team2.getRoster().getPlayer(i);
                    max = t;
                }
            }
        }
        return player;
    }

    public static void shootingTest(CourtLocations courtLocation, double defense){
        Player shooter;
        Player teammate;
        int madeShots;
        int trials = 10000;
        System.out.println(courtLocation + " test, " + trials + " trials" + " one defense = " + defense);

        Player foe = new Player();
        foe.setAttributeValue("Perimeter D", defense);  
        foe.setAttributeValue("Paint D", defense); 
        // add opposing team 
        Team team2 = new Team();
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team2.getRoster().addPlayer(i, foe);
        }
        // max skill, max gravity
        shooter = new Player();
        teammate = new Player();
        Team team1 = new Team();
        
        TeamStats teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter paint
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
                if (!shooter.setAttributeValue("Contested Midrange", 99)) System.err.println("Contested Midrange set error");
                // maximimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                // maximimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing teammate set error");
                
                break;
            case CourtLocations.FT:
                if (!shooter.setAttributeValue("Free Throw", 99)) System.err.println("Free Throw set error");
                break;
            default: 
                return;
        }
        // add shooter and teammates      
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }
        // run test
        madeShots = 0;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %05.2f%% (max player, max gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        if (courtLocation == CourtLocations.FT) return;
        
        // max skill, min gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter paint
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 00)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
                if (!shooter.setAttributeValue("Contested Midrange", 99)) System.err.println("Contested Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 00)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            default: 
                return;
        }
        // add shooter and teammates      
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }
        // run test
        madeShots = 0;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %05.2f%% (max player, min gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        // no skill, max gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // min shooter paint
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // min shooter midrange
                if (!shooter.setAttributeValue("Midrange", 00)) System.err.println("Midrange set error");
                if (!shooter.setAttributeValue("Contested Midrange", 00)) System.err.println("Contested Midrange set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // min shooter 3pt
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            default: 
                return;
        }
        // add shooter and teammates      
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }
        // run test
        madeShots = 0;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %05.2f%% (min player, max gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        // no skill, no gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // min shooter paint
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 00)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // min shooter midrange
                if (!shooter.setAttributeValue("Midrange", 00)) System.err.println("Midrange set error");
                if (!shooter.setAttributeValue("Contested Midrange", 00)) System.err.println("Contested Midrange set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 00)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                if (!teammate.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // min shooter 3pt
                if (!shooter.setAttributeValue("3pt", 00)) System.err.println("3pt set error");
                if (!shooter.setAttributeValue("Contested 3pt", 00)) System.err.println("Contested 3pt set error");
                // maximize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 00)) System.err.println("Rim Finishing teammate set error");
                if (!shooter.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing set error");
                if (!teammate.setAttributeValue("Contested Rim Finishing", 00)) System.err.println("Contested Rim Finishing teammate set error");
                break;
            default: 
                return;
        }
        // add shooter and teammates      
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }

        // run test
        madeShots = 0;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %05.2f%% (min player, min gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        
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
            string += "\n\t High Scorer: ";
            string += getHighScorer().getName();
            string += " ";
            string += teamstats1.getStatsFromPlayer(getHighScorer()).toString();
        } else {
            string = team2.getName();
            string += " won ";
            string += teamstats2.getScore(); 
            string += "-";
            string += teamstats1.getScore();
            string += "\n\t High Scorer: ";
            string += getHighScorer().getName();
            string += " ";
            string += teamstats2.getStatsFromPlayer(getHighScorer()).toString();
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
                string += "High Scorer: ";
                string += getHighScorer().getName();
                string += " ";
                string += teamstats1.getStatsFromPlayer(getHighScorer()).toString();
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


