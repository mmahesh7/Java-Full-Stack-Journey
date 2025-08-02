package Day_5.Extend_Interfaces_Demo;

public interface A {

    //static interfacemethods should always have a body,
    // because they can't be overriden or inherited.
    static void greet() {
        System.out.println("Static Hello from A");
    }
    
    default void fun() {
        System.out.println("Default implementation of fun in A");
    }
}
