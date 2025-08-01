package Day_4.AccessModifiers.package1;

public class B {
    public static void main(String[] args) {
        A objA = new A();

        System.out.println("Inside class B (same package):");
        System.out.println("Public num1: " + objA.num1);         // ✅
        // System.out.println("Private num2: " + objA.num2);     // ❌ Compile Error
        System.out.println("Protected num3: " + objA.num3);      // ✅
        System.out.println("Default num4: " + objA.num4);        // ✅
    }
}
