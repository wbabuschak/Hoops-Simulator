public class Roster {
    public static final int ROSTER_SIZE = 12;
    public static boolean forceRosterLimit = true;

    private int[] minutes = {};
    private Player[] players;

    public boolean setMinutes(int[] inpMinutes){
        // can only give minutes to players on the roster
        if (inpMinutes.length > players.length){
            return false;
        }

        for (int i = 0; i < inpMinutes.length; i++){
            minutes[i] = inpMinutes[i];
        }
        return true;
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
