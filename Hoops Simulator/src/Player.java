import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
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

    private ArrayList<Attribute> attributes = new ArrayList<>();

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
        return true;
    }

    public Player(){
        parseNames();
        name = "";
        height = -1;
        weight = -1;
        age = -1;
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
    }

    public static Player randomPlayer(){
        Player player = new Player();
        player.name = randomName();
        player.height = -1;
        player.weight = -1;
        player.age = -1;
        
        for (int i = 0; i < player.getAttributes().size(); i++){
            double attValue = 100 * Math.random();
            player.getAttributes().get(i).setValue(attValue);
        }
        return player;
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

    public String toString(){
        String string = name;
        string += " ";
        for (int i = 0; i < getAttributes().size(); i++){
            // string += getAttributes().get(i).getName();
            // string += " ";
            string += (int) getAttributes().get(i).getValue();
            string += " ";
        }
        return string;
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

         if (r <= getAttributeValue("Rim Finishing") / (getAttributeValue("Rim Finishing") + getAttributeValue("Midrange") + getAttributeValue("3pt"))){
            return CourtLocations.PAINT;
         } else if (r <= (getAttributeValue("Rim Finishing") + getAttributeValue("Midrange")) / (getAttributeValue("Rim Finishing") + getAttributeValue("Midrange") + getAttributeValue("3pt"))){
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
