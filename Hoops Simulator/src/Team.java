public class Team {
    String location;
    String name;
    Roster roster;

    public String toString(){
        return location + " " + name + "\n" + roster.toString();
    }

}
