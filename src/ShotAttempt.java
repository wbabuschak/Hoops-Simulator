import org.w3c.dom.Attr;

public class ShotAttempt {
    /**
     * 
     */
    public static final double RIM_SHOT_MAX = 1.5;
    /**
     * The worst possible shot attempt at the rim has a 5% chance of going in
     */
    public static final double RIM_SHOT_MIN = 0.1;
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
    public static final double THREE_MIN = 0.05;
    /**
     * The greatest possible defensive impact is 1
     */
    public static final double DEFENSIVE_IMPACT = 0.7;

    /**
     * The greatest possible assist impact is 1 (i.e. assisted shots are always max skill)
     */
    public static final double ASSIST_EFFECT = 0.4;
    private static final double BLOCK_CHANCE = 0.2;
    private static final double BLOCK_CHANCE_FALLOFF = 0.8;
    

    private Player player;
    private Team shootingTeam;
    private Team defendingTeam;
    private CourtLocations courtLocation;
    private boolean make;
    private boolean fouled;
    private boolean blocked;
    private Player blocker;

    public Player getBlocker() {
        return blocker;
    }

    public void setBlocker(Player blocker) {
        this.blocker = blocker;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean getFouled(){
        return fouled;
    }

    public void setFouled(boolean fouled){
        this.fouled = fouled;
    }

    public Team getDefendingTeam(){
        return defendingTeam;
    }

    public CourtLocations getCourtLocation(){
        return courtLocation;
    }

    public ShotAttempt(Player player, Team shootingTeam, Team defendingTeam, CourtLocations courtLocation, boolean assisted) {
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
                shotQuality = player.getAttributeValue("Free Throw") / Attribute.ATTRIBUTE_MAX;
                make = Math.max(shotQuality * FT_MAX, FT_MIN) > Math.random();
                break;
            case CourtLocations.PAINT:
                contested = defendingTeam.getRosterAttributeMean("Paint D") > player.getAttributeValue("Rim Finishing"); 
                shootingSkill = !contested ? player.getAttributeValue("Rim Finishing") : Math.min(player.getAttributeValue("Rim Finishing"), player.getAttributeValue("Contested Rim Finishing"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * ASSIST_EFFECT;
                shotQuality = (
                    3.0 * (shootingSkill / Attribute.ATTRIBUTE_MAX) +
                    1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                ) / (
                    4.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))
                );
                shotQuality *= (1 - DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * ((Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Paint D")) / Attribute.ATTRIBUTE_MAX);
                make = Math.max(Math.min(shotQuality * RIM_SHOT_MAX, 1.0), RIM_SHOT_MIN) > Math.random();
                break;
            case CourtLocations.MIDRANGE:
                contested = defendingTeam.getRosterAttributeMean("Paint D") + defendingTeam.getRosterAttributeMean("Perimeter D") > 2 * player.getAttributeValue("Rim Finishing"); 
                shootingSkill = !contested ? player.getAttributeValue("Midrange") : Math.min(player.getAttributeValue("Midrange"), player.getAttributeValue("Contested Midrange"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * ASSIST_EFFECT;
                shotQuality = 
                    (6.0 * (player.getAttributeValue("Midrange") / Attribute.ATTRIBUTE_MAX) 
                    + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                    + 1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("Rim Finishing") - Attribute.ATTRIBUTE_AVERAGE))))  
                    / (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))) / 8.0;
                shotQuality *= (1 - DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * (
                    (Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Perimeter D")) + 
                    (Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Paint D"))
                ) / (2 * Attribute.ATTRIBUTE_MAX);
                make = Math.max(Math.min(shotQuality * (RIM_SHOT_MAX + THREE_MAX) / 2, 1.0), (RIM_SHOT_MIN + THREE_MIN) / 2) > Math.random();
                break;
            case CourtLocations.THREE:
                contested = defendingTeam.getRosterAttributeMean("Perimeter D") > player.getAttributeValue("3pt"); 
                shootingSkill = !contested ? player.getAttributeValue("3pt") : Math.min(player.getAttributeValue("3pt"), player.getAttributeValue("Contested 3pt"));
                shotQuality = shootingSkill;
                if (assisted) shotQuality += (Attribute.ATTRIBUTE_MAX - shootingSkill) * ASSIST_EFFECT;
                shotQuality = (
                    3.0 * (player.getAttributeValue("3pt") / Attribute.ATTRIBUTE_MAX) +
                    1.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (shootingTeam.getRosterAttributeMean("3pt") - Attribute.ATTRIBUTE_AVERAGE))))
                ) / (
                    4.0 * (1.0 / (1.0 + Math.exp(-gravitySharpness * (Attribute.ATTRIBUTE_MAX - Attribute.ATTRIBUTE_AVERAGE))))
                );
                shotQuality *= (1 - DEFENSIVE_IMPACT) + DEFENSIVE_IMPACT * ((Attribute.ATTRIBUTE_MAX - defendingTeam.getRosterAttributeMean("Perimeter D")) / Attribute.ATTRIBUTE_MAX);
                make = Math.max(Math.min(shotQuality * THREE_MAX, 1.0), THREE_MIN) > Math.random();
                break;
            default:
                return;
        }

        if (courtLocation != CourtLocations.FT) {
            Player defender = defendingTeam.getRoster().getDefender(courtLocation);

            double blockChance = defender.getAttributeValue("Blocks") 
                                / Attribute.ATTRIBUTE_MAX
                                * BLOCK_CHANCE;

            switch (courtLocation) {
                // falloff applies twice on threes, once on midrange, none on rim shots
                case THREE:
                    blockChance *= BLOCK_CHANCE_FALLOFF;
                case MIDRANGE:
                    blockChance *= BLOCK_CHANCE_FALLOFF;
                default:
                    break;
            }

            double blockThreshold = 1.0 - shotQuality;

            if (Math.random() < blockChance * blockThreshold) {
                blocked = true;
                blocker = defender;
                make = false;
                fouled = false;
                return;
            }
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
