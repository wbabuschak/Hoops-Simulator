import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class Main {
    public static void main(String[] args) throws Exception {
        Game game = new Game(Team.randomTeam(), Team.randomTeam());
        game.playGame();
        


        for (int i = 0; i < 100; i++){
            game = new Game(Team.randomTeam(), Team.randomTeam());
            game.playGame();
            System.out.println(game.toString(true));

        } 
        /*
        game.shootingTest(CourtLocations.PAINT, 0.0);
        game.shootingTest(CourtLocations.MIDRANGE, 0.0);
        game.shootingTest(CourtLocations.THREE, 0.0);
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        game.shootingTest(CourtLocations.PAINT, 99.0);
        game.shootingTest(CourtLocations.MIDRANGE, 99.0);
        game.shootingTest(CourtLocations.THREE, 99.0);
        */

        // for (int i = 0; i < 100; i++){
        //     System.out.println(Player.randomPlayer().toString());
        // }
        
    }
}
