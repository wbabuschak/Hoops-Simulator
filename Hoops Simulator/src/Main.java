public class Main {
    public static void main(String[] args) throws Exception {
        
        for (int i = 0; i < 100; i++){
            Game game = new Game(Team.randomTeam(), Team.randomTeam());
            game.playGame();
            System.out.println(game.toString());
        }
        
        // game.shootingTest(CourtLocations.PAINT);
        // game.shootingTest(CourtLocations.MIDRANGE);
        // game.shootingTest(CourtLocations.THREE);
        // game.shootingTest(CourtLocations.FT);

        // for (int i = 0; i < 100; i++){
        //     System.out.println(Player.randomPlayer().toString());
        // }
    }
}
