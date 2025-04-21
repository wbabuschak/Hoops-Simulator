import java.util.ArrayList;

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

    private ArrayList<Attribute> attributes = new ArrayList<>();

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

    public String getName(){
        return name;
    }
}
