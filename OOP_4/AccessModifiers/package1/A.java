package Day_4.AccessModifiers.package1;

public class A {
    public int num1 = 10;       // Accessible everywhere
    private int num2 = 20;      // Only within class A
    protected int num3 = 30;    // Same package + subclasses outside package
    int num4 = 40;              // Default: only within same package

    public void display() {
        System.out.println("Inside class A:");
        System.out.println("Public num1: " + num1);
        System.out.println("Private num2: " + num2);
        System.out.println("Protected num3: " + num3);
        System.out.println("Default num4: " + num4);
    }
}
