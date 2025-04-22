import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Team {
    String location;
    String name;
    Roster roster;

    private static ArrayList<String> cities;
    private static ArrayList<String> teamNames;

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
        parseNames();
        roster = new Roster();
    }

    public static Team randomTeam(){
        Team team = new Team();
        team.setName(randomName());
        for (int i = 0; i < Roster.ROSTER_SIZE; i++){
            team.getRoster().addPlayer(i, Player.randomPlayer());
        }
        return team;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    private void parseNames() {
        Scanner scanner;

        cities = new ArrayList<String>();
        InputStream inputStream = Player.class.getClassLoader().getResourceAsStream("ctrl/Citynames.txt");
        scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            cities.add(scanner.nextLine());
        }
        scanner.close();
        
        teamNames = new ArrayList<String>();
        inputStream = Player.class.getClassLoader().getResourceAsStream("ctrl/Teamnames.txt");
        scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            teamNames.add(scanner.nextLine());
        }
        scanner.close();

    }

    public static String randomName(){
        String name = "";
        name += cities.get((int) (cities.size() * Math.random()));
        name += " ";
        name += teamNames.get((int) (teamNames.size() * Math.random()));
        return name;
    }

}
