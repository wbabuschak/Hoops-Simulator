public class Utility {
    public static double round(double val, int placesAfterDecimal){
        double rounded = val;
        val *= Math.pow(10,placesAfterDecimal);
        int temp = (int) val;
        rounded = (double) temp / Math.pow(10,placesAfterDecimal);
        return rounded;
    }
}
