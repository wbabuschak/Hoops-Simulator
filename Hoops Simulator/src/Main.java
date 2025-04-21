public class Main {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        game.shootingTest(CourtLocations.PAINT);
        game.shootingTest(CourtLocations.MIDRANGE);
        game.shootingTest(CourtLocations.THREE);
        game.shootingTest(CourtLocations.FT);
    }
}
