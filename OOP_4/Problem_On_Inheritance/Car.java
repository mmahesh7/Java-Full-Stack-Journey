package Day_4.Problem_On_Inheritance;

public class Car extends Vehicle {
    private int noOfSeats;

    public Car(String make, String model, int year, String fuelType, double fuelEfficiency, int noOfSeats){
        super(make, model, year, fuelType, fuelEfficiency);
        this.noOfSeats = noOfSeats;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    @Override
    public double calculateFuelEfficiency() {
        return getFuelEfficiency() * 1.2; // Cars generally have higher fuel efficiency
    }

    @Override
    public double calculateDistanceTravelled(){
        return (getFuelEfficiency() * calculateFuelEfficiency()); // Example calculation based on fuel efficiency
    }

    @Override
    public double getMaxSpeed() {
        return 100; 
    }

}
