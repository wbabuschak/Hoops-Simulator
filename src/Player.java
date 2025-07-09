import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;



public class Player {
    public static final double OVERALL_SCALAR = 2.5;
    public static final double POSITIONAL_BOOST = 0.25;

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

    public ArrayList<Attribute> attributes = new ArrayList<>();

    public double points;
    public double rebounds;
    public double assists;
    public int gamesPlayed;

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
        attributes.add(new Attribute("Rim Finishing", 0.0));
        attributes.add(new Attribute("Contested Rim Finishing", 0.0));
        attributes.add(new Attribute("Midrange", 0.0));
        attributes.add(new Attribute("Contested Midrange", 0.0));
        attributes.add(new Attribute("3pt", 0.0));
        attributes.add(new Attribute("Contested 3pt", 0.0));
        attributes.add(new Attribute("Free Throw", 0.0));
        attributes.add(new Attribute("Passing", 0.0));
        attributes.add(new Attribute("Paint D", 0.0));
        attributes.add(new Attribute("Perimeter D", 0.0));
        attributes.add(new Attribute("Stamina", 0.0));
        attributes.add(new Attribute("Pace", 0.0));
        attributes.add(new Attribute("Offensive Discipline", 0.0));
        attributes.add(new Attribute("Defensive Discipline", 0.0));
        attributes.add(new Attribute("Dribbling", 0.0));
        attributes.add(new Attribute("Offensive Rebounding", 0.0));
        attributes.add(new Attribute("Defensive Rebounding", 0.0));
        attributes.add(new Attribute("Hard Fouls", 0.0));
    }

    public static Player randomPlayer(Role role, double overall){
        Player player = new Player(role);
        player.name = randomName();
        player.height = -1;
        player.weight = -1;
        player.age = -1;
        
        switch(role){
            case PG:
                for (int i = 0; i < player.getAttributes().size(); i++){
                    double attValue = overall + (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2 - (Math.random() * overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Passing")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Dribbling")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Free Throw")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Perimeter D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    
                    player.getAttributes().get(i).setValue(Math.min(attValue, Attribute.ATTRIBUTE_MAX));
                }
                break;
            case SG:
                for (int i = 0; i < player.getAttributes().size(); i++){
                    double attValue = overall + (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2 - (Math.random() * overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("3pt")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Dribbling")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Perimeter D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Defensive Discipline")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    
                    player.getAttributes().get(i).setValue(Math.min(attValue, Attribute.ATTRIBUTE_MAX));
                }
                break;
            case SF:
                for (int i = 0; i < player.getAttributes().size(); i++){
                    double attValue = overall + (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2 - (Math.random() * overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("3pt")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Midrange")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Perimeter D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Paint D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    
                    player.getAttributes().get(i).setValue(Math.min(attValue, Attribute.ATTRIBUTE_MAX));
                }
                break;
            case PF:
                for (int i = 0; i < player.getAttributes().size(); i++){
                    double attValue = overall + (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2 - (Math.random() * overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Rim Finishing")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Midrange")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Paint D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Defensive Discipline")) attValue += POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    player.getAttributes().get(i).setValue(Math.min(attValue, Attribute.ATTRIBUTE_MAX));
                }
                break;
            case C:
                for (int i = 0; i < player.getAttributes().size(); i++){
                    double attValue = overall + (Math.random() * (Attribute.ATTRIBUTE_MAX - overall)) / 2 - (Math.random() * overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Rim Finishing")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Offensive Rebounding")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Defensive Rebounding")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    if (player.getAttributes().get(i).getName().equals("Paint D")) attValue += overall * POSITIONAL_BOOST * Math.random() + Math.random() * (Attribute.ATTRIBUTE_MAX - overall) / 2;
                    player.getAttributes().get(i).setValue(Math.min(attValue, Attribute.ATTRIBUTE_MAX));
                }
                break;
            default:
                return randomPlayer(Role.values()[new Random().nextInt(Role.values().length)], overall);
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
        
        return role + " " + name + " (" + overall() + ")";
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

    
}
