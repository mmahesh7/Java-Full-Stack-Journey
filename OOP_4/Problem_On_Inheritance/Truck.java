package Day_4.Problem_On_Inheritance;

public class Truck extends Vehicle {
    private int capacity;

    public Truck(String make, String model, int year, String fuelType, double fuelEfficiency, int capacity) {
        super(make, model, year, fuelType, fuelEfficiency);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public double calculateFuelEfficiency(){
        return getFuelEfficiency() * 0.8; // Trucks generally have lower fuel efficiency
    }

    @Override
    public double calculateDistanceTravelled() {
        return (getFuelEfficiency() * calculateFuelEfficiency()); // Example calculation based on fuel efficiency 
    }

    @Override
    public double getMaxSpeed() {
        return 100;
    }

}