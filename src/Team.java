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
        return name;
    }

    public double getRosterAttributeMean(String attributeName){
        //System.out.println(toString() + " (" + Thread.currentThread().getStackTrace()[2] + ")");
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
        team.getRoster().addPlayer(0, Player.randomPlayer(Role.PG));
        team.getRoster().addPlayer(1, Player.randomPlayer(Role.SG));
        team.getRoster().addPlayer(2, Player.randomPlayer(Role.SF));
        team.getRoster().addPlayer(3, Player.randomPlayer(Role.PF));
        team.getRoster().addPlayer(4, Player.randomPlayer(Role.C));
        team.getRoster().addPlayer(5, Player.randomPlayer(Role.PG));
        team.getRoster().addPlayer(6, Player.randomPlayer(Role.SG));
        team.getRoster().addPlayer(7, Player.randomPlayer(Role.SF));
        team.getRoster().addPlayer(8, Player.randomPlayer(Role.PF));
        team.getRoster().addPlayer(9, Player.randomPlayer(Role.C));
        for (int i = 10; i < Roster.ROSTER_SIZE; i++){
            team.getRoster().addPlayer(i, Player.randomPlayer(Role.ATH));
        }
        team.getRoster().teamSort();
        return team;
    }

    public int[] getRosterMinutes(){
        return roster.getMinutes();
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
