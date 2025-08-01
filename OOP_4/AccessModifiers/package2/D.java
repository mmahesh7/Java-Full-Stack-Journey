package Day_4.AccessModifiers.package2;

import Day_4.AccessModifiers.package1.A;

public class D {
    public static void main(String[] args) {
        A obj = new A();

        System.out.println("Inside class D (non-subclass, different package):");
        System.out.println("Public num1: " + obj.num1);        // ✅
        // System.out.println(obj.num2); // ❌ private
        // System.out.println(obj.num3); // ❌ protected
        // System.out.println(obj.num4); // ❌ default
    }
}