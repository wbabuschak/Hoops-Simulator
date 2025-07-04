import java.util.ArrayList;

public class GameManager {
    public ArrayList<Game> games = new ArrayList<Game>();
    
    public void addGame(Game game){
        games.add(game);
    }
}
