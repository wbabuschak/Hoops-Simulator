import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;



public class Player {
    public static final double OVERALL_SCALAR = 2.5;
    public static final double POSITIONAL_BOOST = 0.1;

    private String name;
    /**
     * Height units: Inches
     */
    private int height;
    

    /**
     * Weight units: Pounds
     */
    private int weight;
    

    /**
     * Age units: Years
     */
    private int age;

    private static ArrayList<String> firstNames;
    private static ArrayList<String> lastNames;

    public Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ArrayList<Attribute> attributes = new ArrayList<>();

    public double points;
    public double rebounds;
    public double assists;
    public int steals;
    public int blocks;
    public int gamesPlayed;
    public double cumBPER;

    public Team team;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private void parseNames() {
        Scanner scanner;

        firstNames = new ArrayList<String>();
        InputStream inputStream = Player.class.getClassLoader().getResourceAsStream("ctrl/Firstnames.txt");
        scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            firstNames.add(scanner.nextLine());
        }
        scanner.close();
        
        lastNames = new ArrayList<String>();
        inputStream = Player.class.getClassLoader().getResourceAsStream("ctrl/Lastnames.txt");
        scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            lastNames.add(scanner.nextLine());
        }
        scanner.close();

    }

    /**
     * Returns the value of a named attribute
     * @param name
     * @return the value of a name attribute or -1 if it cannot be found
     */
    public double getAttributeValue(String name){
        for (int i = 0; i < attributes.size(); i++){
            if (name.equals(attributes.get(i).getName())){
                return attributes.get(i).getValue();
            }
        }
        return -1;
    }

    public boolean setAttributeValue(String name, double value){
        int i = 0;
        while (i < attributes.size()){
            if (attributes.get(i).getName().equals(name)) break;
            i++;
        }
        if (i == attributes.size()) return false;
        attributes.get(i).setValue(value);
        if (value > Attribute.ATTRIBUTE_MAX){
            System.err.println("Attribute over max: " + name + " " + value + " on " + this.name);
        }
        if (value < 0){
            System.err.println("Attribute negative: " + name + " " + value + " on " + this.name);
        }
        return true;
    }

    public Player(Role role){
        parseNames();
        name = "";
        height = -1;
        weight = -1;
        age = -1;
        this.role = role;
        // Rim Finishing affects scoring ability in the paint
        attributes.add(new Attribute("Rim Finishing", 0.0));
        // Contested Rim Finishing affects scoring ability in the paint when defended heavily
        attributes.add(new Attribute("Contested Rim Finishing", 0.0));
        // Midrange affects scoring ability in the midrange
        attributes.add(new Attribute("Midrange", 0.0));
        // Contested Midrange affects scoring ability in the midrange when defended heavily
        attributes.add(new Attribute("Contested Midrange", 0.0));
        // 3pt affects scoring ability at the three
        attributes.add(new Attribute("3pt", 0.0));
        // Contested 3pt affects scoring ability at the three when defended heavily
        attributes.add(new Attribute("Contested 3pt", 0.0));
        // Free Throw affects scoring ability at the FT
        attributes.add(new Attribute("Free Throw", 0.0));
        // Passing affects ability to generate an assist
        attributes.add(new Attribute("Passing", 0.0));
        // Paint D affects defense in the paint (and midrange)
        attributes.add(new Attribute("Paint D", 0.0));
        // Perimeter D affects defense at the three (and midrange)
        attributes.add(new Attribute("Perimeter D", 0.0));
        attributes.add(new Attribute("Stamina", 0.0));
        // Pace affects number of possessions per game
        attributes.add(new Attribute("Pace", 0.0));
        // Offensive Discipline affects chance to turn the ball over on offense
        attributes.add(new Attribute("Offensive Discipline", 0.0));
        // Defensive Discipline affects chance to foul on defense
        attributes.add(new Attribute("Defensive Discipline", 0.0));
        // Dribbling affects chance to have the ball stolen on offense
        attributes.add(new Attribute("Dribbling", 0.0));
        // Offensive rebounding affects the chance to grab an offensive rebound
        attributes.add(new Attribute("Offensive Rebounding", 0.0));
        // Offensive rebounding affects the chance to grab a defensive rebound
        attributes.add(new Attribute("Defensive Rebounding", 0.0));
        // Hard Fouls affects the chance to for a defender to finishing the shot attempt when fouled
        attributes.add(new Attribute("Hard Fouls", 0.0));
        // Steals affects the chance to steal the ball on defense
        attributes.add(new Attribute("Steals", 0.0));
        // Blocks affects the chance to block the ball on defense
        attributes.add(new Attribute("Blocks", 0.0));
    }

    public static Player randomPlayer(Role role, double overall) {
        Player player = new Player(role);
        player.name = randomName();
        player.weight = -1;
        player.age = -1;

        // Determine position-specific boosted attributes and height
        String[] boostedAttributes;
        double heightRng = Math.random();

        switch (role) {
            case PG:
                boostedAttributes = new String[]{"Passing", "Dribbling", "Free Throw", "Perimeter D"};
                player.height = 72 + (int) (heightRng * 5); // 6' - 6'4"
                break;
            case SG:
                boostedAttributes = new String[]{"3pt", "Dribbling", "Perimeter D", "Defensive Discipline"};
                player.height = 73 + (int) (heightRng * 5); // 6'1 - 6'5"
                break;
            case SF:
                boostedAttributes = new String[]{"3pt", "Midrange", "Perimeter D", "Paint D"};
                player.height = 76 + (int) (heightRng * 6); // 6'4 - 6'9"
                break;
            case PF:
                boostedAttributes = new String[]{"Rim Finishing", "Midrange", "Paint D", "Defensive Discipline"};
                player.height = 79 + (int) (heightRng * 6); // 6'7 - 7'
                break;
            case C:
                boostedAttributes = new String[]{"Rim Finishing", "Offensive Rebounding", "Defensive Rebounding", "Paint D"};
                player.height = 81 + (int) (heightRng * 7); // 6'9 - 7'3"
                break;
            default:
                return randomPlayer(
                    Role.values()[new Random().nextInt(Role.values().length)],
                    overall
                );
        }

        
        int neutralHeight = 78;
        int heightDifference = player.height - neutralHeight;
        player.weight = (int) (210 + heightDifference * 10 * ((Math.random() + Math.random() + Math.random()) / 3));
        List<String> tallerBoosts = List.of(
             "Rim Finishing", "Blocks", "Offensive Rebounding", "Defensive Rebounding"
        );

        List<String> tallerPenalties = List.of(
            "Dribbling", "Steals", "Free Throw", "Passing"
        );
        

        double boostPerInch = 3.0;
        double penaltyPerInch = 3.0;

        for (Attribute attribute : player.getAttributes()) {
            double attValue =
                    overall +
                    (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2
                    - (Math.random() * overall) / 2;

            // Apply positional boost
            for (String name : boostedAttributes) {
                if (attribute.getName().equals(name)) {
                    attValue += POSITIONAL_BOOST *
                                Math.random() *
                                (Attribute.ATTRIBUTE_MAX - overall);
                    break;
                }
            }

            // Apply height effects
            if (heightDifference != 0) {
                if (heightDifference > 0) {
                    if (tallerBoosts.contains(attribute.getName())) {
                        attValue += heightDifference * Math.random() * boostPerInch;
                    } else if (tallerPenalties.contains(attribute.getName())) {
                        attValue -= heightDifference * Math.random() * penaltyPerInch;
                    }
                } else {
                    if (tallerBoosts.contains(attribute.getName())) {
                        attValue += heightDifference * Math.random() * penaltyPerInch;
                    } else if (tallerPenalties.contains(attribute.getName())) {
                        attValue -= heightDifference * Math.random() * boostPerInch;
                    }
                }
            }

            attValue = Math.max(0, Math.min(attValue, Attribute.ATTRIBUTE_MAX));
            attribute.setValue(attValue);
        }

        return player;
    }

    public static Player randomPlayer(Role role){
        double overall =  (1 - Math.pow(1 - Math.random(), OVERALL_SCALAR)) * Attribute.ATTRIBUTE_MAX;
        return randomPlayer(role, overall);
    }

    

    public String getName(){
        return name;
    }

    public Team getTeam(){
        return team;
    }

    /**
     * only to be used for setting attributes en masse
     */
    public ArrayList<Attribute> getAttributes(){
        return attributes;
    }


    public int overall(){
        double sum = 0;
        for (int i = 0; i < attributes.size(); i++){
            sum += attributes.get(i).getValue();
        }
        return (int) (sum / (double) attributes.size());
    }

    public String toString(){
        
        return toFeet(height) + " " + role + " " + name + " (" + overall() + ")";
    }

    public static String randomName(){
        String name = "";
        name += firstNames.get((int) (firstNames.size() * Math.random()));
        name += " ";
        name += lastNames.get((int) (lastNames.size() * Math.random()));
        return name;
    }

    public CourtLocations getShotLocation(){
        double r = Math.random();
        double rim = Math.pow(getAttributeValue("Rim Finishing"), 3);
        double mid = Math.pow(getAttributeValue("Midrange"), 3);
        double three = Math.pow(getAttributeValue("3pt"), 3);
        double total = rim + mid + three;
        
         if (r <= rim / total){
            return CourtLocations.PAINT;
         } else if (r <= (rim + mid) / total){
            return CourtLocations.MIDRANGE;
         } else {
            return CourtLocations.THREE;
         }
    }

    public boolean equals(Player player){
        // System.out.println("\tDEBUG: " + player.toString() + " == " + toString() + " " + player.toString().equals(toString()));
        return player.toString().equals(toString());
    }

    public static String toFeet(int inches){
        int feet = inches / 12;
        int r = inches % 12;
        if (r == 0){
            return feet + "'";
        }
        return feet + "'" + r;
    }

    
}
