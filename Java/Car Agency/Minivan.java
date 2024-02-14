package A;

// Minivan class son of Vehicles.
public class Minivan extends Vehicles {
    // Class attributes.
    private int numberOfSeats;
    private boolean airConditionOn, hasAutoDoors;
    private static int hasAutoCounter = 0, allMiniCounter = 0;

    // No arg. constructor
    public Minivan(){

    }

    // Arg. constructor.
    public Minivan(String modelName, String modelNo, String brand, String engineType, double tankSize,
                   double fuelConsumption, Owner owner, int numberOfSeats, boolean hasAutoDoors) {
        super(modelName, modelNo, brand, engineType, tankSize, fuelConsumption, owner);
        if((!engineType.equalsIgnoreCase("gasoline") && !engineType.equalsIgnoreCase("hybrid") &&
                !engineType.equalsIgnoreCase("diesel")) || tankSize <= 0 || fuelConsumption <= 0
                || numberOfSeats < 1 ){
            throw new IllegalArgumentException("Error in minivan with model number " + modelNo + ", check your data");
        }
        else{
            this.numberOfSeats = numberOfSeats;
            this.airConditionOn = false;
            this.hasAutoDoors = hasAutoDoors;
            if(this.hasAutoDoors){
                hasAutoCounter++;
            }
            allMiniCounter++;
        }
    }

    // Setters and getters. The getters might have exceptions if the data entered is wrong.
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isAirConditionOn() {
        return airConditionOn;
    }

    public void setAirConditionOn(boolean airConditionOn) {
        this.airConditionOn = airConditionOn;
    }

    public boolean isHasAutoDoors() {
        return hasAutoDoors;
    }

    public void setHasAutoDoors(boolean hasAutoDoors) {
        this.hasAutoDoors = hasAutoDoors;
    }

    // The methods that were abstract in the father are implemented.
    @Override
    public double costFor100Km(PetroleumType x){
        if(engineType.equalsIgnoreCase("gasoline") || engineType.equalsIgnoreCase("hybrid")){
            return 100.0 * PetroleumType.getGasolinePrice() / fuelConsumption;
        }
        else{
            return 100.0 * PetroleumType.getDieselPrice() / fuelConsumption;
        }
    }
    @Override
    public void setAirConditionOn(){
        if(!airConditionOn) {
            fuelConsumption -= 0.2 * fuelConsumption;
        }
        this.airConditionOn = true;
    }
    @Override
    public void setAirConditionOff(){
        if(airConditionOn){
            fuelConsumption *= 10.0 / 8.0;
        }
        this.airConditionOn = false;
    }

    // The distance that the car can move when the tank is full.
    public double movableDistance(){
        return fuelConsumption * tankSize;
    }

    // Overridden toString method to return the needed information.
    @Override
    public String toString() {
        PetroleumType x = new PetroleumType();
        return "Model Name: " + getModelName() + ", Brand: " + getBrand() + ", Owner: " + owner.getName() + ", Engine type: " + getEngineType()
                + ", Tank size: " + getTankSize() + " Litre, Fuel Consumption: " + getFuelConsumption() + " KM/Litre, Number of seats: " + getNumberOfSeats()
                + ", Movable distance: " + movableDistance() + " KM, Has auto doors: " + isHasAutoDoors() +  ", Air condition on: " + isAirConditionOn()
                + ", Cost for 100 Km: " + costFor100Km(x) + " NIS\n";
    }

    // An extra method that calculates the percentage of the minivans that have auto doors.
    public static String hasAutoPercentage(){
        if(allMiniCounter > 0){
            return hasAutoCounter * 100 /  allMiniCounter + "% of the minivans have auto doors.";
        }
        else{
            return "There are no minivans to check how many of them have auto doors.";
        }
    }
}
