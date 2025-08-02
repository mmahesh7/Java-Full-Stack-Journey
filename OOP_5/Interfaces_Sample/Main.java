package Day_5.Interfaces_Sample;

public class Main {
    public static void main(String[] args) {
        Car car =  new Car();
        
        // car.start();//Car engine started
        // car.stop();//Car engine stopped
        // car.accelerate();//Car is accelerating

        // Media carMedia = new Car();
        // carMedia.stop(); //Output : Car engine stopped
        // But we expect the music to stop, not the engine.

        NiceCar niceCar = new NiceCar();

        niceCar.start();//Power Engine started
        niceCar.startMusic();//CD Player started
        niceCar.upgradeEngine();//Upgrading to Electric Engine
        niceCar.start();//Electric Engine started
    }                      
}
