package Day_5.Interfaces_Sample;

public class CDPlayer implements Media {

    @Override
    public void start() {
        System.out.println("CD Player started");
    }

    @Override
    public void stop() {
        System.out.println("CD Player stopped");
    }
    
}  
