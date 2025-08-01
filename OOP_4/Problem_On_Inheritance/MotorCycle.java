package Day_4.Problem_On_Inheritance;

public class MotorCycle extends Vehicle {
    private double engineRating;

    public MotorCycle(String make, String model, int year, String fuelType, double fuelEfficiency, double engineRating) {
        super(make, model, year, fuelType, fuelEfficiency);
        this.engineRating = engineRating;
    }

    public double getEngineRating() {
        return engineRating;
    }

    @Override
    public double calculateFuelEfficiency(){
        return getFuelEfficiency() * 1.6; // Motorcycles generally have higher fuel efficiency
    }

    @Override
    public double calculateDistanceTravelled() {
        return (getFuelEfficiency() * calculateFuelEfficiency()); 
    }

    @Override 
    public double getMaxSpeed(){
        return 150;
    }
}
