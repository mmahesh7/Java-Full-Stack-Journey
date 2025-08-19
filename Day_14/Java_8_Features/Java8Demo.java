package Java_8_Features;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.sql.rowset.spi.SyncResolver;

public class Java8Demo {
    public static void main(String[] args) {

        //Java 8 provides minimal code and functional programming
        //Features are Lambda Expressions, Streams, Date & Time API


        //lambda expression

        //It is an anonymous function that has no name, no return type and no access modifier
        //It is used to implement functional interfaces
        //Because, Functional interfaces are the interfaces with only one abstract method ie there is only one method with no body/ definition

        Thread t1 = new Thread(() -> { //For Thread class we implement Runnable interface which has abstract run() method
            System.out.println("Hello");
        });
        t1.start(); //Hello

        //For MathOperation interface we have abstract operate() method,
        //here we can use lambda expression to calculate addition, substraction etc
        MathOperation sum = (a,b) -> a+b;
        MathOperation substract = (a,b) -> a-b;
        
        int res = sum.operate(1,3);
        System.out.println(res);// 4
        int res2 = substract.operate(4, 5);
        System.out.println(res2); // -1


        //Predicate
        // is a functional interface
        // it is a boolean valued function, it returns true or false
        // the abstract method used is test()
        Predicate<Integer> isEven = (x) -> (x%2==0); 
        System.out.println(isEven.test(5)); //False
        //It also has some default methods like and, or, andThen etc..
        //By using predicate we can store condition in a variable
        Predicate<String> startswithM = s -> s.toLowerCase().startsWith("m");
        System.out.println(startswithM.test("mk")); // true
        Predicate<String> endswithH = s -> s.toLowerCase().endsWith("h");
        
        Predicate<String> and = startswithM.and(endswithH);
        System.out.println(and.test("mahesh"));

        //Function 
        // also a functional interface
        // it has an input parameter type and return type
        //the abstract method is apply()
        // we perform some work using input and return and output
        Function<Integer, Integer> doubleIt = (x) -> (2*x);
        Function<Integer, Integer> tripleIt = x -> x*3;
        System.out.println(doubleIt.apply(5));
        System.out.println(tripleIt.apply(5));

        System.out.println(doubleIt.andThen(tripleIt).apply(20)); // first doubles 20 = 40 and then triples 40 = 120
        //if we want to triple first and then double we use compose in place of andThen
        System.out.println(doubleIt.compose(tripleIt).apply(20)); // first triples 20 = 60 and then doubles 60 = 120

        //There is a static method in Function called identity() which takes a value and returns it as is
        Function<Integer, Integer> identity = Function.identity();
        Integer res3 = identity.apply(10);
        System.out.println(res3); // 10



        //Consumer
        //It is a functional interface where it just accepts or consumes a value and doesnot return anything
        // The abstract method used here is accept()
        Consumer<Integer> consumer1 = x -> System.out.println(x);
        consumer1.accept(100);//prints 100
        //It is also used for printing collections
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        Consumer<List<Integer>> consumer2 = x -> {
            for(int i: x) {
                System.out.print(i+" ");
            }
            System.out.println();
        };
        consumer2.accept(list);
        //Since consumer has no return type, it can only have andThen() but not compose()

        // Supplier
        //It is also an functional interface
        // It does not take any input value but it returns something(it has a return type)
        //The abstract method used is get()
        Supplier<String> supply = () -> "Supplier String";
        System.out.println(supply.get());


        //Comnbined Example:
        Predicate<Integer> predicate = x -> x%2 == 0;
        Function<Integer, Integer> function = x -> x*x;
        Consumer<Integer> consumer = x -> System.out.println(x);
        Supplier<Integer> supplier = () -> 100;
        
        if(predicate.test(supplier.get())) {
            consumer.accept(function.apply(supplier.get()));// 10000
        }

        //There can also be BiPredicate, Bifunction, BiConsumer which accepts 2 input parameters
        BiPredicate<Integer,Integer> isSumEven = (x,y) -> (x+y)%2 == 0;
        BiFunction<String, String, Integer> sumOfStrLens = (str1, str2) -> (str1 + str2).length(); 
        BiConsumer<String, String> biconsumer = (x,y) -> {
            System.out.println(x);
            System.out.println(y);
        };
        System.out.println(isSumEven.test(1, 11));// true
        System.out.println(sumOfStrLens.apply("IG -","__.mahesh7"));
        biconsumer.accept("IG-","__.mahesh7");


        //UnaryOperator
        //If Function has same input parameter type and return type,
        // then we can use UnaryOperator
        UnaryOperator<Integer> doubler = x -> 2*x;
        System.out.println(doubler.apply(5)); //10
        
        //For BiFunctions we use BinaryOperator
        BinaryOperator<Integer> sum2 = (x,y) -> x+y ;
        System.out.println(sum2.apply(1, 2)); //3

        //Method Referencing(::)
        //with this we can use a method without invoking
        //We use it in place of lambda expression
        List<String> names = Arrays.asList("A","B","C");
        names.forEach(x -> System.out.println(x)); // Lambda Function
        //The above same result can be achieved using method referencing as follows:
        names.forEach(System.out::println);//Method referencing    
    }
}

interface MathOperation {
    int operate(int a, int b);
}