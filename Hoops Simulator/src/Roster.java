public class Roster {
    public static final int ROSTER_SIZE = 12;
    public static boolean forceRosterLimit = true;

    private int[] minutes = {};
    private Player[] players;

    public int noPlayers(){
        int cnt = 0;
        for (int i = 0; i < ROSTER_SIZE; i++){
            if (playerExists(i)){
                cnt++;
            }
        }
        return cnt;
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
        return (sum / (double) noPlayers());
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

}
