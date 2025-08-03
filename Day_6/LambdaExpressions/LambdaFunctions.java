package Day_6.LambdaExpressions;

import java.util.*;

public class LambdaFunctions {
    public static void main(String[] args) {
        // ArrayList<Integer> arr = new ArrayList<>();

        // for (int i = 0; i < 5; i++) {
        //     arr.add(i+1);
        // }

        // arr.forEach((element) -> { System.out.println(element*2);});

        // Using Lambda Expression to create a custom functional interface
        Opreration add = (a,b) -> (a+b);
        Opreration sub = (a,b) -> (a-b);
        Opreration mul = (a,b) -> (a*b);

        LambdaFunctions myCal = new LambdaFunctions();
        System.out.println("Addition: " + myCal.operate(5, 3, add));
        System.out.println("Subtraction: " + myCal.operate(5, 3, sub));
        System.out.println("Multiplication: " + myCal.operate(5, 3, mul));
    }
    private int operate(int a, int b, Opreration op) {
        return op.operation(a, b);
    }
}

interface Opreration {
    int operation(int a, int b);
}