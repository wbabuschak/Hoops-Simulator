public class Game {
    Team team1;
    Team team2;
    TeamStats teamstats1;
    TeamStats teamstats2;

    public void paintTest(){
        Player shooter = new Player();
        if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
        if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
        if (!shooter.setAttributeValue("3pt", 0)) System.err.println("3pt set error");
        Player teammate = new Player();
        if (!teammate.setAttributeValue("3pt", 0)) System.err.println("3pt teammate set error");
        team1 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }
        System.out.println("Shooter Rim Finishing: " + team1.getRoster().getPlayer(0).getAttributeValue("Rim Finishing"));
        System.out.println("Shooter Contested Rim Finishing: " + team1.getRoster().getPlayer(0).getAttributeValue("Contested Rim Finishing"));
        System.out.println("Team 1 3pt shooting: " + team1.getRosterAttributeMean("3pt"));

        team2 = new Team();
        teamstats1 = new TeamStats(team1);

        int madeShots = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.PAINT))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.PAINT.name() + " shot attempts (no gravity)");
        
        shooter = new Player();
        if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
        if (!shooter.setAttributeValue("Contested Rim Finishing", 99)) System.err.println("Contested Rim Finishing set error");
        if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
        teammate = new Player();
        if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
        team1 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }

        team2 = new Team();
        teamstats1 = new TeamStats(team1);

        madeShots = 0;
        trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.PAINT))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.PAINT.name() + " shot attempts (perfect gravity)");
        
    }

    public void ftTest(){
        Player shooter = new Player();
        if (!shooter.setAttributeValue("Free Throw", 99)) System.err.println("Free Throw set error");
        Player teammate = new Player();
        team1 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }
        System.out.println("Shooter FT: " + team1.getRoster().getPlayer(0).getAttributeValue("Free Throw"));
        
        teamstats1 = new TeamStats(team1);

        int madeShots = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.FT))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.FT.name() + " shot attempts");
        
    }

    public void midrangeTest(){
        Player shooter = new Player();
        if (!shooter.setAttributeValue("Midrange", 99)) System.err.println("Midrange set error");
        if (!shooter.setAttributeValue("Contested Midrange", 99)) System.err.println("Contested Midrange set error");
        if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
        if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
        Player teammate = new Player();
        if (!teammate.setAttributeValue("3pt", 99)) System.err.println("3pt teammate set error");
        if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
        
        team1 = new Team();
        team2 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }

        System.out.println("Shooter Midrange: " + team1.getRoster().getPlayer(0).getAttributeValue("Midrange"));
        System.out.println("Shooter Contested Midrange: " + team1.getRoster().getPlayer(0).getAttributeValue("Contested Midrange"));
        System.out.println("Team 1 3pt shooting: " + team1.getRosterAttributeMean("3pt"));
        System.out.println("Team 1 paint shooting: " + team1.getRosterAttributeMean("Rim Finishing"));

        teamstats1 = new TeamStats(team1);

        int madeShots = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.MIDRANGE))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.MIDRANGE.name() + " shot attempts");
        
    }

    public void threeTest(){
        Player shooter = new Player();
        if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
        if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
        
        if (!shooter.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing set error");
        Player teammate = new Player();
        if (!teammate.setAttributeValue("Rim Finishing", 0)) System.err.println("Rim Finishing teammate set error");
        if (!teammate.setAttributeValue("3pt Finishing", 0)) System.err.println("Rim Finishing teammate set error");
       
        team1 = new Team();
        team2 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }

        System.out.println("Shooter 3pt: " + team1.getRoster().getPlayer(0).getAttributeValue("3pt"));
        System.out.println("Shooter Contested 3pt: " + team1.getRoster().getPlayer(0).getAttributeValue("Contested 3pt"));
        System.out.println("Team 1 paint shooting: " + team1.getRosterAttributeMean("Rim Finishing"));

        teamstats1 = new TeamStats(team1);

        int madeShots = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.THREE))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.THREE.name() + " shot attempts (no gravity)");

        shooter = new Player();
        if (!shooter.setAttributeValue("3pt", 99)) System.err.println("3pt set error");
        if (!shooter.setAttributeValue("Contested 3pt", 99)) System.err.println("Contested 3pt set error");
        
        if (!shooter.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing set error");
        teammate = new Player();
        if (!teammate.setAttributeValue("Rim Finishing", 99)) System.err.println("Rim Finishing teammate set error");
        
        team1 = new Team();
        team2 = new Team();
        team1.getRoster().addPlayer(0, shooter);
        for (int i = 1; i < Roster.ROSTER_SIZE; i++){
            team1.getRoster().addPlayer(i, teammate);
        }

        System.out.println("Team 1 paint shooting: " + team1.getRosterAttributeMean("Rim Finishing"));

        madeShots = 0;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.THREE))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " " + CourtLocations.THREE.name() + " shot attempts (perfect gravity)");
        
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
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, courtLocation))){
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
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, courtLocation))){
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
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, courtLocation))){
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
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, courtLocation))){
                madeShots++;
            }
        }
        System.out.printf("\tMade %4d out of %5d %s shot attempts %5.2f%% (min player, min gravity)\n", madeShots, trials, courtLocation.name(), 100.0 * madeShots / trials);
        
    }
}


