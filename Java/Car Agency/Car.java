package A;

// Car class son of Vehicles.
public class Car extends Vehicles {

    // Class attributes.
    private int numberOfSeats;
    private boolean airConditionOn;

    // No arg. constructor
    public Car(){

    }

    // Arg. constructor.
    public Car(String modelName, String modelNo, String brand, String engineType,
                double tankSize, double fuelConsumption, Owner owner, int numberOfSeats) {
        super(modelName, modelNo, brand, engineType, tankSize, fuelConsumption, owner);
        if((!engineType.equalsIgnoreCase("gasoline") && !engineType.equalsIgnoreCase("hybrid"))||
            tankSize <= 0 || fuelConsumption <= 0 || numberOfSeats < 1){
            throw new IllegalArgumentException("Error in car with model number " + modelNo + ", check your data");
        }
        else{
            this.numberOfSeats = numberOfSeats;
            this.airConditionOn = false;
        }
    }

    // Setters and getters.
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

    // The methods that were abstract in the father are implemented.
    @Override
    public void setAirConditionOn(){
        if(!airConditionOn) {
            fuelConsumption -= 0.1 * fuelConsumption;
        }
        this.airConditionOn = true;
    }
    @Override
    public void setAirConditionOff(){
        if(airConditionOn){
            fuelConsumption *= 10.0 / 9.0;
        }
        this.airConditionOn = false;
    }
    @Override
    public double costFor100Km(PetroleumType x){
        return 100.0 * PetroleumType.getGasolinePrice() / fuelConsumption;
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
                + ", Tank size: " + getTankSize() + " Litre , Fuel Consumption: " + getFuelConsumption() + " KM/Litre, "
                + "Number of seats: " + getNumberOfSeats() + ", Movable distance: " + movableDistance()
                + " KM, Air condition on: " + isAirConditionOn() + ", Cost for 100 Km: " + costFor100Km(x) + " NIS\n";
    }
}
