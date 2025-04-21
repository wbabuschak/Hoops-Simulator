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
        int trials = 500;
        for (int i = 0; i < trials; i++){
            if (teamstats1.changeScore(new ShotAttempt(0, team1, team2, CourtLocations.PAINT))){
                madeShots++;
            }
        }

        System.out.println("Made " + madeShots + " out of " + trials + " shot attempts");
        
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

        System.out.println("Made " + madeShots + " out of " + trials + " shot attempts");
        
    }
}


