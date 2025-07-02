public class Roster {
    public static final int ROSTER_SIZE = 12;
    public static boolean forceRosterLimit = false;
    public static double skillReliance = 2.0;
    public static final int MINUTES_RFACTOR = 5;
    public static final double ASSIST_RATE = 0.3;

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

    public void sort(){
        
        for (int i = 0; i < ROSTER_SIZE - 1; i++){
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
        return true;
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


    public Roster(){
        players = new Player[ROSTER_SIZE];
        minutes = new int[ROSTER_SIZE];
        for (int i = 0; i < ROSTER_SIZE; i++){
            minutes[i] = calculateRosterMinutes(i);
        }
        initializeMinutes();
    }

    public int[] getMinutes(){
        return minutes;
    }

    public void initializeMinutes(){
        int existingSum = 0;
        int[] fetch = new int[ROSTER_SIZE];
        for (int i = 0; i < ROSTER_SIZE; i++){
            existingSum += minutes[i];
        }
        int currentSum = 0;
        for (int i = 0; i < ROSTER_SIZE; i++){
            fetch[i] =  (int) ((double) 5 * Game.GAME_LENGTH * minutes[i] / (double)existingSum);
            currentSum += fetch[i];
        }
        
        for (int i = 0; i < ROSTER_SIZE; i++){
            for (int j = 0; j < MINUTES_RFACTOR; j++){
                int k = 1 - (int) (2.0 * Math.random());
                fetch[i] += k;
                currentSum += k;
            }
        }

        for (int i = 0; i < ROSTER_SIZE; i++){
            if (fetch[i] < 0){
                fetch[i] = 0;
            }
        }
        
        for (int i = 0; currentSum < 5 * Game.GAME_LENGTH; i++){
            fetch[i % ROSTER_SIZE]++;
            currentSum++;
        }
        
        for (int i = 0; currentSum > 5 * Game.GAME_LENGTH; i++){
            if (fetch[i % ROSTER_SIZE] > 0){
                fetch[i % ROSTER_SIZE]--;
                currentSum--;
            }
        }

        for (int i = 0; i < ROSTER_SIZE; i++){
            if (fetch[i] < 5 && Math.random() * fetch[i] < 1){
                currentSum-= fetch[i];
                fetch[i] = 0;
                
            }
        }

        for (int i = 0; currentSum < 5 * Game.GAME_LENGTH; i++){
            fetch[i % ROSTER_SIZE]++;
            currentSum++;
        }
        
        for (int i = 0; i < ROSTER_SIZE; i++){
            
        }
        minutes = fetch;
    }

    public int calculateRosterMinutes(int i) {
        if (i < 5) {
            return 30;
        }

        return Math.max((int) (20 - 4 * (i - 5)), 0);
    }


    public Player chooseShooterAtRandom(){
        double total = 0.0;
        for (int i = 0; i < ROSTER_SIZE; i++) {
            if (minutes[i] == 0){
                continue;
            }
            total += Math.pow(minutes[i], 3);
            total += skillReliance * players[i].getAttributeValue("3pt");
            total += skillReliance * players[i].getAttributeValue("Midrange");
            total += skillReliance * players[i].getAttributeValue("Rim Finishing");

        }

        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < ROSTER_SIZE; i++) {
            if (minutes[i] == 0){
                continue;
            }
            cumulative += Math.pow(minutes[i], 3);
            cumulative += skillReliance * players[i].getAttributeValue("3pt");
            cumulative += skillReliance * players[i].getAttributeValue("Midrange");
            cumulative += skillReliance * players[i].getAttributeValue("Rim Finishing");
            if (r <= cumulative) {
                return (players[i]);
            }
        }
        // should never occur
        return null;

    }

    public Player getOffensiveRebounder(){
        double total = 0.0;
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            total += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Offensive Rebounding")/Attribute.ATTRIBUTE_AVERAGE;
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            cumulative += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Offensive Rebounding")/Attribute.ATTRIBUTE_AVERAGE;
            if (r <= cumulative) {
                return getPlayer(i);
            }
        }
        // should never return null
        return null;
    }

        public Player getDefensiveRebounder(){
        double total = 0.0;
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            //System.out.println("total: " + total);
            total += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Defensive Rebounding")/Attribute.ATTRIBUTE_AVERAGE;
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            //System.out.println("cumulative: " + cumulative);
            cumulative += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Defensive Rebounding")/Attribute.ATTRIBUTE_AVERAGE;
            if (r <= cumulative) {
                return getPlayer(i);
            }
        }
        //System.out.println("DEBUG");
        return null;
    }

    public Player getAssister(){
        double total = 0.0;
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            total += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_AVERAGE;
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            cumulative += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_AVERAGE;
            if (r <= cumulative) {
                if (((getPlayer(i).getAttributeValue("Passing")/Attribute.ATTRIBUTE_MAX) * ASSIST_RATE > Math.random())){
                    return getPlayer(i);
                }
            }
        }
        // returns null if no player is credited with assist
        return null;
    }


    public Player getDefender(CourtLocations courtLocation){
        double total = 0.0;
        String defenderAttribute;
        switch(courtLocation){
            case CourtLocations.PAINT:
                defenderAttribute = "Paint D";
                break;
            case CourtLocations.MIDRANGE:
                defenderAttribute = "combined_defense";
                break;
            case CourtLocations.THREE:
                defenderAttribute = "Perimeter D";
                break;
            // should never occur
            default:
                return null;
        }
        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            if (defenderAttribute.equals("combined_defense")){
                total += Math.pow(getMinutes()[i], 3) * (getPlayer(i).getAttributeValue("Paint D") + getPlayer(i).getAttributeValue("Perimeter D"))/ (2 * Attribute.ATTRIBUTE_AVERAGE);
            } else {
                total += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue(defenderAttribute)/Attribute.ATTRIBUTE_AVERAGE;
            }
            
        }
        double r = Math.random() * total;
        double cumulative = 0.0;

        for (int i = 0; i < Roster.ROSTER_SIZE; i++) {
            if (getMinutes()[i] == 0){
                continue;
            }
            if (defenderAttribute.equals("combined_defense")){
                cumulative += Math.pow(getMinutes()[i], 3) * (getPlayer(i).getAttributeValue("Paint D") + getPlayer(i).getAttributeValue("Perimeter D"))/ (2 * Attribute.ATTRIBUTE_AVERAGE);
            } else {
                cumulative += Math.pow(getMinutes()[i], 3) * getPlayer(i).getAttributeValue(defenderAttribute)/Attribute.ATTRIBUTE_AVERAGE;
            }
            if (r <= cumulative) {
                return getPlayer(i);
            }
        }
        // returns null if no player is credited with assist
        return null;
    }


    public String toString(){
        String fullString = "";
        for (int i = 0; i < ROSTER_SIZE; i++){
            fullString += players[i].toString();
        }
        return fullString;
    }
}
