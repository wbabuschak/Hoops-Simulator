public class Attribute {
    public static final double ATTRIBUTE_MAX = 99;
    public static final double ATTRIBUTE_AVERAGE = ATTRIBUTE_MAX / 2.0;
    
    private String name;
    private String desc;
    private double value;
    
    
    /**
     * 
     * @param value
     */
    public void setValue(double value){
        this.value = value;
    }
    /**
     * 
     * @return
     */
    public double getValue(){
        return value;
    }
    /**
     * 
     * @param name
     * @param value
     */
    public Attribute(String name, double value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return (int) value + "- " + name;
    }
    
}
