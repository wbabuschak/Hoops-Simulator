public class ShotAttempt {
    public static final double FT_MAX = 0.95;

    private int playerId;
    private Team shootingTeam;
    private Team defendingTeam;
    private CourtLocations courtLocation;
    private boolean make;

    /**
     * The best possible shot attempt has a 90% chance of going in
     */
    public static double SHOT_MAX = 0.90;
    /**
     * The worst possible shot attempt has a 5% chance of going in
     */
    public static double SHOT_MIN = 0.05;

    public ShotAttempt(int playerId, Team shootingTeam, Team defendingTeam, CourtLocations courtLocation){
        this.playerId = playerId;
        this.shootingTeam = shootingTeam;
        this.defendingTeam = defendingTeam;
        this.courtLocation = courtLocation;
        
        double shotQuality = 0.0;
        double gravitySharpness = 0.15;
        switch(courtLocation){
            case CourtLocations.FT:
                // shot quality based strictly on shooter's FT skill
                shotQuality = shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Free Throw")/Attribute.ATTRIBUTE_MAX * FT_MAX;
                break;
            case CourtLocations.PAINT:
                // shots are contested if the defending team has a greater defense than the player has finishing skill
                boolean contested = defendingTeam.getRosterAttributeMean("Paint Defense") > shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing"); 
                // if shots are contested, use the lower of contested and non-contested paint skill
                double shootingSkill = contested ? shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing") : Math.min(shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing"),shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Contested Rim Finishing"));
                shotQuality = shootingSkill;
                // adjust for team gravity, 3:1 weighting for player skill vs team 3pt gravity
                shotQuality = 
                (3.0 * (shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing") / Attribute.ATTRIBUTE_MAX) + 
                1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE)))) 
                / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 4.0;
                break;
            case CourtLocations.MIDRANGE:
                shotQuality = shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Midrange");
                break;
            case CourtLocations.THREE:
                shotQuality = shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("3pt");
                break;
            default: return;
        }
        make = Math.max(Math.min(shotQuality,SHOT_MAX),SHOT_MIN) < Math.random();
    }

    /**
     * Returns the number of points this shot is worth (0 if miss)
     * @return
     */
    public int make(){
        if (!make){
            return 0;
        }
        switch (courtLocation){
            case CourtLocations.FT: return 1;
            case CourtLocations.THREE: return 3;
            default: return 2;
        }
    }
    
}
