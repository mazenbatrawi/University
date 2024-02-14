package A;

// Truck class son of Vehicles.
public class Truck extends Vehicles {
    // Class attributes.
    private boolean airConditionOn;
    private int power, numberOfSeats;
    private static int truckCounter = 0, powerSum = 0;

    // No arg. constructor
    public Truck(){

    }

    // Arg. constructor.
    public Truck(String modelName, String modelNo, String brand, String engineType, double tankSize,
                 double fuelConsumption, Owner owner ,int power, int numberOfSeats) {
        super(modelName, modelNo, brand, engineType, tankSize, fuelConsumption, owner);
        if(!engineType.equalsIgnoreCase("diesel") || tankSize <= 0 || fuelConsumption <= 0 ||
                power <= 0 || numberOfSeats < 1){
            throw new IllegalArgumentException("Error in truck with model number " + modelNo + ", check your data");
        }
        else{
            this.airConditionOn = false;
            this.power = power;
            this.numberOfSeats = numberOfSeats;
            truckCounter++;
            powerSum += this.power;
        }
    }

    // Setters and getters.
    public boolean isAirConditionOn() {
        return airConditionOn;
    }

    public void setAirConditionOn(boolean airConditionOn) {
        this.airConditionOn = airConditionOn;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    // The methods that were abstract in the father are implemented.
    @Override
    public double costFor100Km(PetroleumType x){
        // Checking if the engine type is correct. Otherwise, throwing an exception.
        return 100.0 * PetroleumType.getDieselPrice() / fuelConsumption;
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
                + ", Movable distance: " + movableDistance() + " KM, Power: " + getPower() + " Horse, Air condition on: " +isAirConditionOn()
                + ", Cost for 100 Km: " + costFor100Km(x) + " NIS\n";
    }

    // An extra method that calculates the average power of the trucks.
    public static String powerAverage(){
        if(truckCounter > 0){
            return "\nThe average power for the trucks is: " + powerSum / truckCounter + " Horse";
        }
        else{
            return "\nThere are no trucks to calculate the average power of them.";
        }
    }

}
