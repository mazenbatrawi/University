package A;

// PetroleumType class
// This class is static.
public class PetroleumType {

    // Attributes of the class.
    public static final int DIESEL = 1;
    public static final int GASOLINE = 2;
    private static double gasolinePrice = 5.23, dieselPrice = 4.02;
    public static double maxGasoline = 5.23, minGasoline = 5.23, maxDiesel = 4.02, minDiesel = 4.02;

    // Setters and getters, the getters might have exceptions if the data entered is wrong.
    public static double getGasolinePrice() {
        return gasolinePrice;
    }

    public static void setGasolinePrice(double gasolinePrice) {
        if(gasolinePrice > 0){
            if(gasolinePrice > PetroleumType.gasolinePrice){
                maxGasoline = gasolinePrice;
            }
            else{
                minGasoline = gasolinePrice;
            }
            PetroleumType.gasolinePrice = gasolinePrice;
        }
        else{
            throw new IllegalArgumentException("The price can't be zero or less");
        }
    }

    public static double getDieselPrice() {
        return dieselPrice;
    }

    public static void setDieselPrice(double dieselPrice) {
        if(dieselPrice >= 0){
            if(dieselPrice > PetroleumType.dieselPrice){
                maxDiesel = dieselPrice;
            }
            else{
                minDiesel = dieselPrice;
            }
            PetroleumType.dieselPrice = dieselPrice;
        }
        else{
            throw new IllegalArgumentException("The price can't be zero or less");
        }
    }

    // Overridden toString method to display when changing prices.
    @Override
    public String toString() {
        return ("Enter 1 for diesel, 2 for gasoline, and 3 for both: ");
    }

    // An extra method that prints the maximum and minimum prices during the whole process.
    public static String getMinMaxPrices(){
        return "\nThe maximum gasoline price was: " + maxGasoline + " NIS"
                + "\nThe minimum gasoline price was: " + minGasoline + " NIS"
                + "\nThe maximum diesel price was: " + maxDiesel + " NIS"
                + "\nThe minimum diesel price was: " + minDiesel + " NIS";
    }
}
