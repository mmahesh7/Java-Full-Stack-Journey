package Day_5.Interfaces_Sample;

public class Car implements Engine, Brake, Media {

    @Override
    public void brake() {
        System.out.println("Car is braking");
    }

    @Override
    public void start() {
        System.out.println("Car engine started");
    }

    @Override
    public void stop() {
        System.out.println("Car engine stopped");
    }

    @Override
    public void accelerate() {
        System.out.println("Car is accelerating");
    }
    
}
