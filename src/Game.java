import java.util.Random;

public class Game {
    public static final int GAME_LENGTH = 48;
    public static final int SHOT_CLOCK_LENGTH = 24;
    public static final double MIN_PACE = 0.35;
    public static final double MAX_PACE = 0.90;
    public static final double PACE_SCALAR = 1.6;
    public static final double TURNOVER_SCALAR = 0.15;
    public static final double TURNOVER_RATE = 0.01;
    public static final double REBOUND_CHANCE = 0.7;
    /**
     * A higher foul difficulty means a defender is less likely to foul
     */
    public static final double FOUL_DIFFICULTY = 10;

    /**
     * A higher three foul difficulty means a defender is less likely to foul on a three pointer
     */
    public static final double THREE_FOUL_DIFFICULTY = 0.6;
    private static final double STEAL_CHANCE = 0.3;


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
        return (int)(GAME_LENGTH * PACE_SCALAR * (60/(SHOT_CLOCK_LENGTH)) * Math.max(MIN_PACE,
        Math.min(
        ((team1.getRosterAttributeMean("Pace") + (team2.getRosterAttributeMean("Pace"))) / (2 * Attribute.ATTRIBUTE_MAX))
        ,MAX_PACE)));
    }

    public void playGame(){
        possessions = calculatePossessions();
        teamstats1 = new TeamStats(team1);
        teamstats2 = new TeamStats(team2);
        for (int i = 0; i < possessions; i++){
            runPossession();
        }
        addPlayerMinutes(teamstats1, team1, false);
        addPlayerMinutes(teamstats2, team2, false);
        // overtime
        while (teamstats1.getScore() == teamstats2.getScore()){
            possessions = calculatePossessions() / 8;
            addPlayerMinutes(teamstats1, team1, true);
            addPlayerMinutes(teamstats2, team2, true);
            for (int i = 0; i < possessions; i++){
                runPossession();
            }
        }
        getWinner().wins++;
        team1.gamesPlayed++;
        team2.gamesPlayed++;
    }

    

    private void addPlayerMinutes(TeamStats teamStats, Team team, boolean isOvertime) {
        Random rng = new Random();
        Roster roster = team.getRoster();
        int[] avgMinutesArray = roster.getMinutes();
        int duration = isOvertime ? (GAME_LENGTH / 8) : GAME_LENGTH;

        double[] prelimMinutes = new double[Roster.ROSTER_SIZE];
        double totalPrelim = 0.0;

        // Step 1: preliminary random minutes
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            int avgMinutes = avgMinutesArray[i];
            double stdMinutes = avgMinutes * 0.1;
            double baseMinutes = isOvertime
                ? avgMinutes * ((double) duration / GAME_LENGTH)
                : avgMinutes;

            double randomMinutes = baseMinutes + rng.nextGaussian() * (stdMinutes / (isOvertime ? 3.0 : 1.0));
            randomMinutes = Math.max(0, Math.min(randomMinutes, duration));
            prelimMinutes[i] = randomMinutes;
            totalPrelim += randomMinutes;
        }

        // Step 2: Normalize to total available minutes (duration * 5)
        double targetTotal = duration * 5;
        double ratio = targetTotal / totalPrelim;

        int[] finalMinutes = new int[Roster.ROSTER_SIZE];
        int sumFinal = 0;

        // Step 3: Apply ratio and round
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            finalMinutes[i] = (int) Math.round(prelimMinutes[i] * ratio);
            sumFinal += finalMinutes[i];
        }

        // Step 4: Adjust for rounding errors
        int diff = (int) targetTotal - sumFinal;
        int idx = 0;
        while (diff != 0) {
            // Add or subtract 1 minute to players who can take it
            if (diff > 0) {
                finalMinutes[idx]++;
                diff--;
            } else if (diff < 0 && finalMinutes[idx] > 0) {
                finalMinutes[idx]--;
                diff++;
            }
            idx = (idx + 1) % Roster.ROSTER_SIZE;
        }

        // Step 5: Set minutes
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            Player player = roster.getPlayer(i);
            PlayerStats playerStats = teamStats.getStatsFromPlayer(player);
            playerStats.setMinutes(playerStats.getMinutes() + finalMinutes[i]);
        }
    }


    private void runPossession() {
        runTeamPossession(team1, teamstats1, team2, teamstats2);
        runTeamPossession(team2, teamstats2, team1, teamstats1);
    }

    private void runTeamPossession(Team offenseTeam, TeamStats offenseTeamStats, Team defenseTeam, TeamStats defenseTeamStats) {
        double turnoverThreshold = TURNOVER_RATE + TURNOVER_SCALAR * (Attribute.ATTRIBUTE_MAX - offenseTeam.getRosterAttributeMean("Offensive Discipline")) / Attribute.ATTRIBUTE_MAX;
        // unforced turnover check
        if (Math.random() > turnoverThreshold) {
            // steal attempt
            Player stealer = stealAttempt(defenseTeam, offenseTeam);
            if (stealer == null) {
                ShotAttempt attempt = takeShot(offenseTeam, offenseTeamStats, defenseTeam, defenseTeamStats);
                if (attempt.isBlocked()) {
                    // Handle block event (add block stat to blocker, turnover to offense, etc)
                    defenseTeamStats.getStatsFromPlayer(attempt.getBlocker()).addBlock();
                    attempt.getBlocker().blocks++;
                    offenseTeamStats.turnover();
                } else {
                    // Normal shot processing, scoring, rebounding, fouls
                    while (!offenseTeamStats.changeScore(attempt) && !attempt.getFouled()) {
                        if (!rebound(offenseTeam, offenseTeamStats, defenseTeam, defenseTeamStats)) {
                            break;
                        }
                        attempt = takeShot(offenseTeam, offenseTeamStats, defenseTeam, defenseTeamStats);
                    }
                }
            } else {
                defenseTeamStats.getStatsFromPlayer(stealer).addSteal();
                offenseTeamStats.turnover();
            }
        } else {
            offenseTeamStats.turnover();
        }
    }

    public Player stealAttempt(Team defenseTeam, Team offenseTeam){
        
        Player ballCarrier = offenseTeam.getRoster().getBallHandler();
        Player defender = defenseTeam.getRoster().getDefender(ballCarrier.getShotLocation());

        if (defender.getAttributeValue("Steals") * Math.random() * STEAL_CHANCE > Math.random() * (ballCarrier.getAttributeValue("Offensive Discipline") + ballCarrier.getAttributeValue("Dribbling"))){
            defender.steals++;
            return defender;
        }
        return null;
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
            if (Math.random() < REBOUND_CHANCE) defendingTeamStats.getPlayerStats().get(defendingTeam.getRoster().getPosition(dRebounder)).incrementDRebound();
        }
        return false;
    }
    

    private ShotAttempt takeShot(Team shootingTeam, TeamStats shootingTeamStats, Team defendingTeam, TeamStats defendingTeamStats){
        Player shootingPlayer = shootingTeam.getRoster().chooseShooterAtRandom();
        PlayerStats shootingPlayerStats = shootingTeamStats.getPlayerStats().get(shootingTeam.getRoster().getPosition(shootingPlayer));
        Player assister = shootingTeam.getRoster().getAssister();
        ShotAttempt shotAttempt = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, shootingPlayer.getShotLocation(), assister != null);
        int fts = foulChance(shotAttempt);
        
        if (fts <= 1){
            shootingPlayerStats.addShotAttempt(shotAttempt);
            if (shotAttempt.make() > 1 && assister != null) shootingTeamStats.getPlayerStats().get(shootingTeam.getRoster().getPosition(assister)).addAssist();
        }
        for (int i = 0; i < fts; i++){
            ShotAttempt ft = new ShotAttempt(shootingPlayer, shootingTeam, defendingTeam, CourtLocations.FT, false);
            shootingPlayerStats.addShotAttempt(ft);
            shootingTeamStats.changeScore(ft);
        }

        return shotAttempt;
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
        // System.out.println("~~~~~~~~~~~~~~~~~~~");
        if (getWinner().equals(team1)){
            for (int i = 0; i < team1.getRoster().noPlayers(); i++){
                double t = teamstats1.getPlayerStats().get(i).calculateBPER();
                // System.out.println("\t DEBUG: " + team1.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team1.getRoster().getPlayer(i);
                    max = t;
                }
            }
        } else if (getWinner().equals(team2)){
            for (int i = 0; i < team2.getRoster().noPlayers(); i++){
                double t = teamstats2.getPlayerStats().get(i).calculateBPER();
                // System.out.println("\t DEBUG: " + team2.getRoster().getPlayer(i).getName() + " " + t + " points.");
                if (t > max){
                    player = team2.getRoster().getPlayer(i);
                    max = t;
                }
            }
        }
        return player;
    }

    public String toString(){
        return team1.getName() + " vs " + team2.getName() + ": " + teamstats1.getScore() + "-" + teamstats2.getScore();
    }

    public String toString(boolean full) {
        StringBuilder sb = new StringBuilder();

        if (teamstats1.getScore() == teamstats2.getScore()) {
            sb.append("Game has not been played!");
        } else {
            Team winner, loser;
            TeamStats winnerStats, loserStats;

            if (getWinner().equals(team1)) {
                winner = team1;
                loser = team2;
                winnerStats = teamstats1;
                loserStats = teamstats2;
            } else {
                winner = team2;
                loser = team1;
                winnerStats = teamstats2;
                loserStats = teamstats1;
            }

            sb.append(String.format("%s won %d-%d%n%n",
                    winner.getName(),
                    winnerStats.getScore(),
                    loserStats.getScore()));

            if (full) {
                sb.append(String.format("Player of the game: %s%n%n", getPOTG().getName()));

                sb.append(winner.getName() + "\n\n");
                sb.append(winnerStats.toString());
                sb.append("\n");
                sb.append(loser.getName() + "\n\n");
                sb.append(loserStats.toString());
            }
        }

        return sb.toString();
    }

    
}


