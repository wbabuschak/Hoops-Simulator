public class Game {
    public static final int GAME_LENGTH = 48;
    public static final int SHOT_CLOCK_LENGTH = 24;
    public static final double MIN_PACE = 0.35;
    public static final double MAX_PACE = 0.90;


    Team team1;
    Team team2;
    TeamStats teamstats1;
    TeamStats teamstats2;

    public Game(Team team1, Team team2){
        this.team1 = team1;
        this.team2 = team2;
    }

    public void playGame(){
        teamstats1 = new TeamStats(team1);
        teamstats2 = new TeamStats(team2);
        int possessions = (int)(GAME_LENGTH * (60/(SHOT_CLOCK_LENGTH/2)) * Math.max(MIN_PACE,
            Math.min(
            ((team1.getRosterAttributeMean("Pace") + (team2.getRosterAttributeMean("Pace"))) / (2 * Attribute.ATTRIBUTE_MAX))
            ,MAX_PACE)));

        for (int i = 0; i < possessions; i++){
            teamstats1.changeScore(takeShot(team1, team2));
            teamstats2.changeScore(takeShot(team2, team1));
        }
        // overtime
        while (teamstats1.getScore() == teamstats2.getScore()){
            possessions = (int)((GAME_LENGTH / 8) * (60/(SHOT_CLOCK_LENGTH/2)) * Math.max(MIN_PACE,
                Math.min(
                ((team1.getRosterAttributeMean("Pace") + (team2.getRosterAttributeMean("Pace"))) / (2 * Attribute.ATTRIBUTE_MAX))
                ,MAX_PACE)));

            for (int i = 0; i < possessions; i++){
                teamstats1.changeScore(takeShot(team1, team2));
                teamstats2.changeScore(takeShot(team2, team1));
            }

        }
    }

    private static ShotAttempt takeShot(Team shootingTeam, Team defendingTeam){
        Player shootingPlayer = shootingTeam.getRoster().choosePlayerAtRandom();
        return(new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, shootingPlayer.getShotLocation()));
    }

    public Team getWinner(){
        return (teamstats1.getScore() > teamstats2.getScore()) ? team1 : team2;
    }

    public void shootingTest(CourtLocations courtLocation){
        Player shooter;
        Player teammate;
        int madeShots;
        int trials = 10000;
        System.out.println(courtLocation + " test, " + trials + " trials");

        // max skill, max gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        team2 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter 3pt
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
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
        trials = 10000;
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
        team2 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter 3pt
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 0)) System.err.println("3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 0)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing teammate set error");
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
        trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %05.2f%% (max player, min gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        // max skill, max gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        team2 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter 3pt
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                break;
            default: 
                return;
        }
        // no skill, max gravity
        shooter = new Player();
        teammate = new Player();
        team1 = new Team();
        team2 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter 3pt
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 0)) System.err.println("Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
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
        trials = 10000;
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
        team2 = new Team();
        teamstats1 = new TeamStats(team1);
        switch(courtLocation){
            case CourtLocations.PAINT:
                // max shooter 3pt
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 0)) System.err.println("3pt teammate set error");
                break;
            case CourtLocations.MIDRANGE:
                // max shooter midrange
                if (!shooter.setAttributeValue("Midrange", 0)) System.err.println("Midrange set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                if (!teammate.setAttributeValue("3pt", 0)) System.err.println("3pt teammate set error");
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing teammate set error");
                break;
            case CourtLocations.THREE:
                // max shooter 3pt
                if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
                // minimize team gravity
                if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
                if (!teammate.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing teammate set error");
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
        trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(shooter, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %5.2f%% (min player, min gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        
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
        } else {
            string = team2.getName();
            string += " won ";
            string += teamstats2.getScore(); 
            string += "-";
            string += teamstats1.getScore();
        }
        return string;
    }
}


