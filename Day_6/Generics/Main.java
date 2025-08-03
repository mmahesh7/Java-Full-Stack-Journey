package Day_6.Generics;

public class Main implements GenericInterface<Integer> {
    
    @Override
    public void display(Integer value) {
        System.out.println("Value: " + value);
    }
}
