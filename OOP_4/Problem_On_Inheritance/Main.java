package Day_4.Problem_On_Inheritance;


public class Main {
    public static void main(String[] args) {
        Truck truck = new Truck("Tata", "Tata Ultra", 2020, "Diesel", 55.0, 300);
        Car car = new Car("Benz", "A", 2015, "Petrol", 65, 2);
        MotorCycle motorcycle = new MotorCycle("Hero Honda", "Splendor", 2008,"petrol", 85, 9);

        System.out.println("Truck model: " + truck.getModel());
        System.out.println("Truck Fuel Efficiency: " + truck.calculateFuelEfficiency());
        System.out.println("Truck Distance Travelled: " + truck.calculateDistanceTravelled());
        System.out.println("Truck Max Speed: " + truck.getMaxSpeed());

        System.out.println("\nCar model: " + car.getModel());
        System.out.println("Car Fuel Efficiency: " + car.calculateFuelEfficiency());
        System.out.println("Car Distance Travelled: " + car.calculateDistanceTravelled());
        System.out.println("Car Max Speed: " + car.getMaxSpeed());

        System.out.println("\nMotorcycle model: " + motorcycle.getModel());
        System.out.println("Motorcycle Fuel Efficiency: " + motorcycle.calculateFuelEfficiency());
        System.out.println("Motorcycle Distance Travelled: " + motorcycle.calculateDistanceTravelled());
        System.out.println("Motorcycle Max Speed: " + motorcycle.getMaxSpeed());

        //Output:
        // Truck model: Tata Ultra
        // Truck Fuel Efficiency: 44.0
        // Truck Distance Travelled: 2420.0
        // Truck Max Speed: 100.0

        // Car model: A
        // Car Fuel Efficiency: 78.0
        // Car Distance Travelled: 5070.0
        // Car Max Speed: 100.0

        // Motorcycle model: Splendor
        // Motorcycle Fuel Efficiency: 136.0
        // Motorcycle Distance Travelled: 11560.0
        // Motorcycle Max Speed: 150.0
 
    }
}
