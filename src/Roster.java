public class Roster {
    public static final int ROSTER_SIZE = 12;
    public static boolean forceRosterLimit = false;
    public static final double SHOOTING_SKILL_FACTOR = 3.0;
    public static final int MINUTES_RFACTOR = 5;
    public static final double ASSIST_RATE = 0.3;
    public static final double ASSIST_SKILL_FACTOR = 11.0;
    public static final double REBOUND_SKILL_FACTOR = 5.5;
    public static final int MINUTES_FACTOR = 4;

    public Team team;

    private int[] minutes = {};
    public Player[] players;

    public int noPlayers(){
        int cnt = 0;
        for (int i = 0; i < ROSTER_SIZE; i++){
            if (playerExists(i)){
                cnt++;
            }
        }
        //System.out.println("found " + cnt + " players");
        return cnt;
    }

    public void teamSort(){
        for (int i = 0; i < 5; i++){
            for (int j = i + 1; j < ROSTER_SIZE; j++){
                if (players[j].overall() > players[i].overall() && players[j].role == players[i].role){
                    Player temp = players[i];
                    players[i] = players[j];
                    players[j] = temp;
                }
            }
        }
        for (int i = 5; i < ROSTER_SIZE - 1; i++){
            for (int j = i + 1; j < ROSTER_SIZE; j++){
                if (players[j].overall() > players[i].overall()){
                    Player temp = players[i];
                    players[i] = players[j];
                    players[j] = temp;
                }
            }
        }
    }

    /**
     * NYI - returns this roster's average of a certain attribute
     * @param name
     * @return
     */
    public double getAverage(String name){
        double sum = 0;
        for (int i = 0; i < ROSTER_SIZE; i++){
            if (playerExists(i)){
                double val = players[i].getAttributeValue(name);
                if (val != -1){
                    sum += val;
                }
            }
        }
        return ( (double) sum / (double) noPlayers());
    }
    /**
     * NYI
     * @param inpMinutes
     * @return
     */
    public boolean setMinutes(int[] inpMinutes){
        if (inpMinutes.length > players.length) 
            return false;
        for (int i = 0; i < inpMinutes.length; i++)
            minutes[i] = inpMinutes[i];
        return true;
    }
    /**
     * Swaps the roster spot of 2 players
     * @param pos1
     * @param pos2
     */
    public void swapPlayers(int pos1, int pos2){
        Player p1 = players[pos1];
        players[pos1] = players[pos2];
        players[pos2] = p1;
    }
    /**
     * Returns whether a roster spot is filled
     * @param pos
     * @return whether the specified roster spot is filled
     */
    public boolean playerExists(int pos){
        return (players[pos] != null);
    }
    /**
     * Removes a player from this roster
     * @param pos
     * @return the player that was cut
     */
    public Player cut(int pos){
        Player tar = players[pos];
        players[pos] = null;
        return tar;
    }
    /**
     * Adds a non-null player to an empty roster spot
     * @param pos
     * @param player
     * @return whether the player was added
     */
    public boolean addPlayer(int pos, Player player){
        if (players[pos] != null){
            return false;
        }
        if (player == null){
            return false;
        }
        if (pos >= players.length){
            return false;
        }
        players[pos] = player;
        player.team = team;
        return true;
    }


    /**
     * Replace a player on the roster
     * @param pos
     * @param player
     * @return the player that was replaced
     */
    public Player replacePlayer(int pos, Player player){
        if (player == null){
            return null;
        }
        if (pos >= players.length){
            return null;
        }

        Player replaced = players[pos];
        Team replacedTeam = replaced.team;
        replaced.team = players[pos].team;
        
        players[pos] = player;
        players[pos].team = replacedTeam;

        return replaced;
    }
    /**
     * @depricated
     */
    public static boolean validateMinutes(int gameLength, int[] inpMinutes){
        int sum = 0;
        for (int i = 0; i < inpMinutes.length; i++){
            // player minutes can not exceed game length
            if (inpMinutes[i] > gameLength){
                return false;
            }
            sum += inpMinutes[i];
        }
        // team minutes can not exceed 5 * game length
        if (sum > 5 * gameLength){
            return false;
        }
        return true;
    }
    /**
     * Returns the player at a given position
     * @param pos
     * @return
     */
    public Player getPlayer(int pos){
        return players[pos];
    }

    /**
     * Returns the position of a given player
     * @param pos
     * @return
     */
    public int getPosition(Player player){
    //    System.out.println("DEBUG: " + player.getName());
        for (int i = 0; i < ROSTER_SIZE; i++){
      //      System.out.println("\tDEBUG: " + players[i].getName() + " " + players[i].equals(player));
            if (players[i].equals(player)){
                return i;
            }
        }
        return -1;
    }


    public Roster(Team team){
        players = new Player[ROSTER_SIZE];
        minutes = new int[ROSTER_SIZE];
        this.team = team;
        for (int i = 0; i < ROSTER_SIZE; i++){
            minutes[i] = calculateRosterMinutes(i);
        }
        initializeMinutes();
    }

    public int[] getMinutes(){
        return minutes;
    }

    public void initializeMinutes(){
        switch ((int) (Math.random() * 6)){
            case 0:
                // positionless, low curve
                minutes = new int[]{32, 32, 32, 32, 32, 24, 24, 12, 12, 6, 1, 1};
                break;
            case 1:
                // PG + C
                minutes = new int[]{38, 31, 31, 31, 38, 24, 23, 12, 9, 1, 1, 1};
                break;
            case 2:
                // SG + SF
                minutes = new int[]{31, 38, 38, 31, 31, 24, 23, 12, 8, 1, 1, 1};
                break;
            case 3:
                // PF + SC
                minutes = new int[]{31, 31, 31, 38, 38, 24, 23, 12, 8, 1, 1, 1};
                break;
            case 4:
                // Heavy starters, high curve
                minutes = new int[]{36, 36, 36, 36, 36, 24, 18, 10, 5, 1, 1, 1};
                break;
            case 5:
                // Light starters, low curve
                minutes = new int[]{30, 30, 30, 30, 30, 24, 20, 16, 12, 10, 4, 4};
                break;
            default:
                minutes = new int[]{20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
        }
        
    }

    public int calculateRosterMinutes(int i) {
        if (i < 5) {
            return 30;
        }

        return Math.max((int) (20 - 4 * (i - 5)), 0);
    }


    public Player chooseShooterAtRandom() {
        double[] weights = new double[ROSTER_SIZE];
        double totalWeight = 0.0;

        for (int i = 0; i < ROSTER_SIZE; i++) {
            int mins = minutes[i];
            if (mins == 0) {
                weights[i] = 0.0;
                continue;
            }

            double shootingSkill =
                    players[i].getAttributeValue("3pt") +
                    players[i].getAttributeValue("Midrange") +
                    players[i].getAttributeValue("Rim Finishing");

            double scaledSkill = Math.pow(shootingSkill, SHOOTING_SKILL_FACTOR); 

            weights[i] = mins * scaledSkill;
            totalWeight += weights[i];
        }

        if (totalWeight == 0.0) {
            return null;
        }

        double r = Math.random() * totalWeight;
        double cumulative = 0.0;

        for (int i = 0; i < ROSTER_SIZE; i++) {
            cumulative += weights[i];
            if (cumulative >= r) {
                return players[i];
            }
        }

        return null; // Should never happen
    }

   public Player getOffensiveRebounder() {
        double[] weights = new double[Roster.ROSTER_SIZE];
        double totalWeight = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            int mins = getMinutes()[i];
            if (mins == 0) {
                weights[i] = 0.0;
                continue;
            }

            double reboundSkill = getPlayer(i).getAttributeValue("Offensive Rebounding");

            double normalizedSkill = reboundSkill / Attribute.ATTRIBUTE_AVERAGE;
            double scaledSkill = Math.pow(normalizedSkill, REBOUND_SKILL_FACTOR);

            weights[i] = mins * scaledSkill;
            totalWeight += weights[i];
        }

        if (totalWeight == 0.0) {
            return null;
        }

        double r = Math.random() * totalWeight;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            cumulative += weights[i];
            if (cumulative >= r) {
                return getPlayer(i);
            }
        }

        return null; // fallback (should never occur)
    }

    public Player getBallHandler(){
        double total = 0.0;
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            total += Math.pow(getMinutes()[i], MINUTES_FACTOR) * (getPlayer(i).getAttributeValue("Offensive Discipline"));
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            cumulative += Math.pow(getMinutes()[i], MINUTES_FACTOR) * (getPlayer(i).getAttributeValue("Offensive Discipline"));
            if (r <= cumulative) {
                return getPlayer(i);
            }
        }
        // should never return null
        return null;
    }

    public Player getDefensiveRebounder() {
        double[] weights = new double[Roster.ROSTER_SIZE];
        double totalWeight = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            int mins = getMinutes()[i];
            if (mins == 0) {
                weights[i] = 0.0;
                continue;
            }

            double reboundSkill = getPlayer(i).getAttributeValue("Defensive Rebounding");
            double scaledSkill = Math.pow(reboundSkill, REBOUND_SKILL_FACTOR);

            weights[i] = mins * scaledSkill;
            totalWeight += weights[i];
        }

        if (totalWeight == 0.0) {
            return null;
        }

        double r = Math.random() * totalWeight;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            cumulative += weights[i];
            if (cumulative >= r) {
                return getPlayer(i);
            }
        }

        return null; // fallback (should never occur)
    }

    public Player getAssister(){
        double total = 0.0;
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            total += getMinutes()[i] * Math.pow(getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_AVERAGE, ASSIST_SKILL_FACTOR) ;
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            cumulative  += getMinutes()[i] * Math.pow(getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_AVERAGE, ASSIST_SKILL_FACTOR) ;
            if (r <= cumulative) {
                if (((getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_MAX) * ASSIST_RATE > Math.random())){
                    return getPlayer(i);
                }
            }
        }
        // returns null if no player is credited with assist
        return null;
    }


    public Player getDefender(CourtLocations courtLocation) {
        double total = 0.0;
        boolean combinedDefense = false;
        String defenderAttribute = null;

        switch (courtLocation) {
            case CourtLocations.PAINT:
                defenderAttribute = "Paint D";
                break;
            case CourtLocations.MIDRANGE:
                combinedDefense = true;
                break;
            case CourtLocations.THREE:
                defenderAttribute = "Perimeter D";
                break;
            default:
                System.err.println("courtLocation error");
                return null;  // Should never occur
        }

        // Calculate total weight
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            Player p = getPlayer(i);
            if (p == null || getMinutes()[i] == 0) {
                continue;
            }
            double weight = Math.pow(getMinutes()[i], 3);
            if (combinedDefense) {
                weight *= (p.getAttributeValue("Paint D") + p.getAttributeValue("Perimeter D")) / (2.0 * Attribute.ATTRIBUTE_AVERAGE);
            } else {
                weight *= p.getAttributeValue(defenderAttribute) / Attribute.ATTRIBUTE_AVERAGE;
            }
            total += weight;
        }

        if (total == 0) {
            System.err.println("total == 0 error");
            return null;  // No valid defender found
        }

        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            Player p = getPlayer(i);
            if (p == null || getMinutes()[i] == 0) {
                continue;
            }
            double weight = Math.pow(getMinutes()[i], 3);
            if (combinedDefense) {
                weight *= (p.getAttributeValue("Paint D") + p.getAttributeValue("Perimeter D")) / (2.0 * Attribute.ATTRIBUTE_AVERAGE);
            } else {
                weight *= p.getAttributeValue(defenderAttribute) / Attribute.ATTRIBUTE_AVERAGE;
            }
            cumulative += weight;
            if (r <= cumulative) {
                return p;
            }
        }

        System.err.println("fallback error");
        return null; // fallback, but should not happen if total > 0
    }


    public String toString(){
        String fullString = "";
        for (int i = 0; i < ROSTER_SIZE; i++){
            fullString += players[i].toString();
        }
        return fullString;
    }
}
