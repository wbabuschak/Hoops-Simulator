import org.w3c.dom.Attr;

public class ShotAttempt {
    /**
     * 
     */
    public static final double RIM_SHOT_MAX = 1.5;
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
     * 
     */
    public static final double THREE_MAX = 0.85;
    /**
     * The worst possible shot attempt from three has a 1% chance of going in
     */
    public static final double THREE_MIN = 0.01;
    /**
     * The greatest possible defensive impact is 1
     */
    public static final double DEFENSIVE_IMPACT = 0.75;

    /**
     * The greatest possible assist impact is 1 (i.e. assisted shots are always max skill)
     */
    public static final double ASSIST_EFFECT = 0.5;
    

    private Player player;
    private Team shootingTeam;
    private Team defendingTeam;
    private CourtLocations courtLocation;
    private boolean make;

    public ShotAttempt(Player player, Team shootingTeam, Team defendingTeam, CourtLocations courtLocation, boolean assisted){
        this.player = player;
        this.shootingTeam = shootingTeam;
        this.defendingTeam = defendingTeam;
        this.courtLocation = courtLocation;
        
        double shotQuality = 0.0;
        double gravitySharpness = 0.15;
        boolean contested;
        double shootingSkill = 0.0;
        switch(courtLocation){
            case CourtLocations.FT:
                // shot quality based strictly on shooter's FT skill
                shotQuality = player.getAttributeValue("Free Throw")/Attribute.ATTRIBUTE_MAX;
                make = Math.max(shotQuality * FT_MAX, FT_MIN) > Math.random();
                break;
            case CourtLocations.PAINT:
                // shots are contested if the defending team has a greater defense than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Paint D") > player.getAttributeValue("Rim Finishing"); 
                // if shots are contested, use the lower of contested and non-contested paint skill
                shootingSkill = !contested ? player.getAttributeValue("Rim Finishing") : Math.min(player.getAttributeValue("Rim Finishing"),player.getAttributeValue("Contested Rim Finishing"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * (ASSIST_EFFECT);
                // adjust for team gravity, 3:1 weighting for player skill vs team 3pt gravity
                shotQuality = (
                    3.0 * (shootingSkill / Attribute.ATTRIBUTE_MAX) +
                    1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                ) / (
                    4.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))
                );
                // defensive impact
                shotQuality *= (1-DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * ((Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Paint D")) / Attribute.ATTRIBUTE_MAX);
                make = Math.max(Math.min(shotQuality * RIM_SHOT_MAX,1.0), RIM_SHOT_MIN) > Math.random();
                //System.out.println("shotQuality " + shotQuality + " " + courtLocation);
                break;
            case CourtLocations.MIDRANGE:
                // shots are contested if the defending team has a greater defense (average of paint + perimeter) than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Paint D") + defendingTeam.getRosterAttributeMean("Perimeter D")> 2 * player.getAttributeValue("Rim Finishing"); 
                // if shots are contested, use the lower of contested and non-contested midrange skill
                shootingSkill = !contested ? player.getAttributeValue("Midrange") : Math.min(player.getAttributeValue("Midrange"),player.getAttributeValue("Contested Midrange"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * (ASSIST_EFFECT);
                // adjust for team gravity, 6:1:1 weighting for player skill vs team 3pt gravity vs team rim gravity
                shotQuality = 
                (6.0 * (player.getAttributeValue("Midrange") / Attribute.ATTRIBUTE_MAX) 
                + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("Rim Finishing") - Attribute.ATTRIBUTE_AVERAGE))))  
                / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 8.0;
                // defensive impact
                shotQuality *= (1-DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * (
                (Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Perimeter D")) + 
                (Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Paint D"))
                ) 
                / (2 * Attribute.ATTRIBUTE_MAX);
                make = Math.max(Math.min(shotQuality * (RIM_SHOT_MAX + THREE_MAX) / 2,1.0), (RIM_SHOT_MIN + THREE_MIN) / 2) > Math.random();
                //System.out.println("shotQuality " + shotQuality + " " + courtLocation);
                break;
            case CourtLocations.THREE:
                // shots are contested if the defending team has a greater defense than the player has finishing skill
                contested = defendingTeam.getRosterAttributeMean("Perimeter D") > player.getAttributeValue("3pt"); 
                // if shots are contested, use the lower of contested and non-contested 3pt skill
                shootingSkill = !contested ? player.getAttributeValue("3pt") : Math.min(player.getAttributeValue("3pt"),player.getAttributeValue("Contested 3pt"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * (ASSIST_EFFECT);
                //System.out.println("after decision " + shootingSkill);
                // adjust for team gravity, 3:1 weighting for player skill vs team rim gravity
                shotQuality = (
                    3.0 * (player.getAttributeValue("3pt") / Attribute.ATTRIBUTE_MAX) +
                    1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                ) / (
                    4.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))
                );
                //System.out.println("adjusted for team gravity " + shootingSkill);
                // defensive impact
                //System.out.println("shotQuality = " + shotQuality + " defendingTeam.getRosterAttributeMean(\"Perimeter D\") = " + defendingTeam.getRosterAttributeMean("Perimeter D") + " Attribute.ATTRIBUTE_MAX = " + Attribute.ATTRIBUTE_MAX);
                //System.out.println((1.0 - DEFENSIVE_IMPACT * (defendingTeam.getRosterAttributeMean("Perimeter D") / Attribute.ATTRIBUTE_MAX)));
                shotQuality *= (1-DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * ((Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Perimeter D")) / Attribute.ATTRIBUTE_MAX);
                //System.out.println("before defense = " + before + " after defense= " + shotQuality);
                //System.out.println("after defensive impact " + shotQuality);
                make = Math.max(Math.min(shotQuality * THREE_MAX, 1.0), THREE_MIN) > Math.random();
                //System.out.println("shotQuality " + shotQuality + " " + courtLocation);
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

    public Player getShooter(){
        return player;
    }
    
}
