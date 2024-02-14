package A;

// Abstract class Vehicles has abstract methods implemented in the sons of it. It implements comparable and cloneable.
public abstract class Vehicles implements Comparable <Vehicles>, Cloneable{
    // The attributes of the class.
    protected String modelName, modelNo, brand, engineType;
    protected double tankSize, fuelConsumption;
    public Owner owner;

    // No arg. Constructor.
    public Vehicles(){

    }

    // Arg. constructor.
    public Vehicles(String modelName, String modelNo, String brand, String engineType, double tankSize,
                    double fuelConsumption, Owner owner) {
        this.modelName = modelName;
        this.modelNo = modelNo;
        this.brand = brand;
        this.engineType = engineType;
        this.tankSize = tankSize;
        this.fuelConsumption = fuelConsumption;
        this.owner = owner;
    }

    // Setters and getters.
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public double getTankSize() {
       return tankSize;
    }

    public void setTankSize(double tankSize) {
        this.tankSize = tankSize;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    // The abstract method.
    public abstract double costFor100Km(PetroleumType x);
    public abstract double movableDistance();
    public abstract void setAirConditionOn();
    public abstract void setAirConditionOff();

    // The interface that sorts the vehicles in ascending order cost based.
    @Override
    public int compareTo(Vehicles vehicles){
        PetroleumType temp = new PetroleumType();
        if(this.costFor100Km(temp) > vehicles.costFor100Km(temp)){
            return 1;
        }
        else if(this.costFor100Km(temp) < vehicles.costFor100Km(temp)){
            return -1;
        }
        return 0;
    }



    // The interface that clones a vehicle selected by the user without its owner.
    @Override
    public Object clone() throws CloneNotSupportedException {
        Vehicles obj = (Vehicles) super.clone();
        obj.owner = (Owner) obj.owner.clone();
        obj.owner.setName(null);
        return obj;
    }
}
