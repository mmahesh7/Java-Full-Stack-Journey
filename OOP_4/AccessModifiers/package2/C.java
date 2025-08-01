package Day_4.AccessModifiers.package2;

import Day_4.AccessModifiers.package1.A;

public class C extends A {
    public void accessMembers() {
        System.out.println("Inside subclass C (different package):");
        System.out.println("Public num1: " + num1);          // ✅
        // System.out.println("Private num2: " + num2);      // ❌
        System.out.println("Protected num3: " + num3);       // ✅ (Inherited)
        // System.out.println("Default num4: " + num4);      // ❌
    }

    public static void main(String[] args) {
        C obj = new C();
        obj.accessMembers();
    }
}