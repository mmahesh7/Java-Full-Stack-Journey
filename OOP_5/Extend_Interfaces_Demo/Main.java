package Day_5.Extend_Interfaces_Demo;

public class Main implements A,B {

    @Override
    public void greet() {
        
    }

    public static void main(String[] args) {
        Main main = new Main();
 
        A.greet(); // Calling static method via interface A
        //Why A.greet() : Because static methods in interfaces can be called without an instance
        // Output: Static Hello from A
    }
    
}
