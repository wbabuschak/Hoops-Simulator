public class ShotAttempt {
    /**
     * The best possible shot attempt at the rim has a 90% chance of going in
     */
    public static final double RIM_SHOT_MAX = 0.90;
    /**
     * The worst possible shot attempt at the rim has a 5% chance of going in
     */
    public static final double RIM_SHOT_MIN = 0.05;
    /**
     * The best possible shot attempt at the free throw line has a 95% chance of going in
     */
    public static final double FT_MAX = 0.95;
    /**
     * The worst possible shot attempt at the free throw line has a 35% chance of going in
     */
    public static final double FT_MIN = 0.35;
    /**
     * The best possible shot attempt from three has a 50% chance of going in
     */
    public static final double THREE_MAX = 0.50;
    /**
     * The worst possible shot attempt from three has a 1% chance of going in
     */
    public static final double THREE_MIN = 0.01;
    

    private int playerId;
    private Team shootingTeam;
    private Team defendingTeam;
    private CourtLocations courtLocation;
    private boolean make;

    public ShotAttempt(int playerId, Team shootingTeam, Team defendingTeam, CourtLocations courtLocation){
        this.playerId = playerId;
        this.shootingTeam = shootingTeam;
        this.defendingTeam = defendingTeam;
        this.courtLocation = courtLocation;
        
        double shotQuality = 0.0;
        double gravitySharpness = 0.15;
        boolean contested;
        double shootingSkill;
        switch(courtLocation){
            case CourtLocations.FT:
                // shot quality based strictly on shooter's FT skill
                shotQuality = shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Free Throw")/Attribute.ATTRIBUTE_MAX;
                make = Math.max(shotQuality * FT_MAX, FT_MIN) < Math.random();
                break;
            case CourtLocations.PAINT:
                // shots are contested if the defending team has a greater defense than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Paint Defense") > shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing"); 
                // if shots are contested, use the lower of contested and non-contested paint skill
                shootingSkill = contested ? shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing") : Math.min(shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing"),shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Contested Rim Finishing"));
                shotQuality = shootingSkill;
                // adjust for team gravity, 3:1 weighting for player skill vs team 3pt gravity
                shotQuality = 
                (3.0 * (shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing") / Attribute.ATTRIBUTE_MAX) + 
                1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE)))) 
                / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 4.0;
                make = Math.max(shotQuality * RIM_SHOT_MAX, RIM_SHOT_MIN) < Math.random();
                break;
            case CourtLocations.MIDRANGE:
                // shots are contested if the defending team has a greater defense (average of paint + perimeter) than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Paint Defense") + defendingTeam.getRosterAttributeMean("Perimeter Defense")> 2 * shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Rim Finishing"); 
                // if shots are contested, use the lower of contested and non-contested midrange skill
                shootingSkill = contested ? shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Midrange") : Math.min(shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Midrange"),shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Contested Midrange"));
                shotQuality = shootingSkill;
                // adjust for team gravity, 6:1:1 weighting for player skill vs team 3pt gravity vs team rim gravity
                shotQuality = 
                (6.0 * (shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Midrange") / Attribute.ATTRIBUTE_MAX) 
                + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("Rim Finishing") - Attribute.ATTRIBUTE_AVERAGE))))  
                / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 8.0;
                make = Math.max(shotQuality * (RIM_SHOT_MAX + THREE_MAX) / 2, (RIM_SHOT_MIN + THREE_MIN) / 2) < Math.random();
                break;
            case CourtLocations.THREE:
                // shots are contested if the defending team has a greater defense than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Perimeter Defense") > shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("3pt"); 
                // if shots are contested, use the lower of contested and non-contested 3pt skill
                shootingSkill = contested ? shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("3pt") : Math.min(shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("3pt"),shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("Contested 3pt"));
                shotQuality = shootingSkill;
                // adjust for team gravity, 3:1 weighting for player skill vs team rim gravity
                shotQuality = 
                (3.0 * (shootingTeam.getRoster().getPlayer(playerId).getAttributeValue("3pt") / Attribute.ATTRIBUTE_MAX) + 
                1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("Rim Finishing") - Attribute.ATTRIBUTE_AVERAGE)))) 
                / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 4.0;
                make = Math.max(shotQuality * THREE_MAX, THREE_MIN) < Math.random();
                break;
            default: return;
        }
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
