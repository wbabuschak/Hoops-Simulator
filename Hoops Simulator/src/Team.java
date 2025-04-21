public class Team {
    String location;
    String name;
    Roster roster;

    public String toString(){
        return location + " " + name + "\n" + roster.toString();
    }

    public double getRosterAttributeMean(String attributeName){
        return roster.getAverage(attributeName);
    }

    public Roster getRoster(){
        return roster;
    }

    public Team(){
        roster = new Roster();
    }

}
