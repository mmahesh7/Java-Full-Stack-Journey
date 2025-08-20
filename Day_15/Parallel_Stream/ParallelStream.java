package Parallel_Stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParallelStream {
    public static void main(String[] args) {
        //without using parallel processing
        long startTime = System.currentTimeMillis();
        List<Integer> list = Stream.iterate(1, x-> x+1).limit(2000).toList();
        // List<Long> factorialList = list.stream().map(x -> factorial(x)).toList();
        List<Long> factorialList = list.stream().map(ParallelStream::factorial).toList();
        // List<Long> factorialList = list.stream().map(ParallelStream::factorial).sequential().toList();//sequential() method is a hint to tell the stream pipeline that,
        // from this point onwards, the operations should run in sequential mode.
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken for sequential stream processing: "+(endTime - startTime)+" ms");
        
        //On applying parallel stream
        startTime = System.currentTimeMillis();
        factorialList = list.parallelStream().map(x -> factorial(x)).toList();
        endTime = System.currentTimeMillis();
        System.out.println("Time taken for parallel stream processing: "+(endTime - startTime)+" ms");
        // System.out.println(factorialList);
        //Output:
        // Time taken for sequential stream processing: 21 ms
        // Time taken for parallel stream processing: 9 ms

        //parallel streams are most effective when task are independent
        //they may add overhead for simple tasks or small datasets

        //Example - Cumulative sum
        //[1,2,3,4,5] --> [1,3,6,10,15]
        //Here current index answer is dependent on previous element
        List<Integer> nums = Arrays.asList(1,2,3,4,5);
        // int sum = 0;
        // nums.parallelStream().map(x->{
        //     int i = sum + x;
        //     sum = i;
        //     return i;
        // }).toList();
        //For above it shows an error because in lambda expression the local variables must be final or effectively final
        // in our case variable i cannopt be final because it gets frequently updated for calculating sum
        // It is also not thread-safe
        // So we use AtomicInteger class with addAndGet() method for addition instead of lambda expression
        AtomicInteger sum = new AtomicInteger(0);
        // List<Integer> cumulativeSum = nums.parallelStream().map(x -> sum.addAndGet(x)).toList();
        List<Integer> cumulativeSum = nums.parallelStream().map(sum::addAndGet).toList();
        System.out.println("Expected Cumulative sum: [1,3,6,10,15]");
        System.out.println("Actual result with parallel processing: "+ cumulativeSum);
        //Output:
        // Expected Cumulative sum: [1,3,6,10,15]
        // Actual result with parallel processing: [13, 15, 12, 9, 5]

        //we can observe that the order of result is different because it is not independent so it first runs the threads which are independent 


        /* missed intermediate operartion in yerterday's practice */
        
        //7. peek()
        //it performs an action on element as it is consumed
        // it is similar to terminal operation forEach()
        long count = Stream.iterate(1, x -> x+1).skip(10).limit(50).peek(System.out::println).count();//prints 11 to 60 and returns cnt ie 50
        
        //8. flatMap()
        //handles streams of collections, lists, or arrays where each element is itself a collection
        //Flatten nested structures(e.g., lists within lists), so that they can be processed as a single sequence of elements.
        //it transforms and flatten elements at the same time
        //Example 1
        List<List<String>> listofLists = Arrays.asList(
            Arrays.asList("apple", "banana"),
            Arrays.asList("orange", "kiwi"),
            Arrays.asList("pear", "grape")
        );
        System.out.println(listofLists.stream().flatMap(x -> x.stream()).map(String::toUpperCase).toList());
        //Output: [APPLE, BANANA, ORANGE, KIWI, PEAR, GRAPE]

        //Example 2
        List<String> sentences = Arrays.asList(
            "Hello world",
            "Java streams are powerful",
            "flatMap is useful"
        ); 
        System.out.println(sentences
            .stream()
            .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
            .map(String::toUpperCase).toList()
        );




        /* missed terminal operartion in yerterday's practice */
        //7. toArray()
        //used to convert a stream into an array or terminate it
        Object[] array = Stream.of(1,2,3).toArray();

        //8. min/max
        System.out.println("max: "+ Stream.of(1,2,4,66,43).max((o1,o2) -> o1-o2).get());
        // System.out.println("max: "+ Stream.of(1,2,4,66,43).max(Comparator.naturalOrder()).get());
        System.out.println("min: "+ Stream.of(1,2,4,66,43).min((o1,o2) -> o1-o2).get());
        // System.out.println("min: "+ Stream.of(1,2,4,66,43).min(Comparator.naturalOrder()).get());
        
        //9. forEachOrdered
        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        System.out.println("using forEach with parallel stream");
        numbers.parallelStream().forEach(System.out::print);//76310415892 random order
        System.out.println();
        System.out.println("using forEachOrdered with parallel stream");
        numbers.parallelStream().forEachOrdered(System.out::print);//12345678910 



        /* Stream cannot be reused after a terminal operation has bee called */
        //Example
        // List<String> names = Arrays.asList("Mahesh","Anuvind", "Koushik","Srikanth");
        // Stream<String> stream = names.stream();
        // stream.forEach(System.out::println);
        // List<String> l1 = stream.map(String::toUpperCase).toList();
        //Output: Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
    }

    private static long factorial(int n) {
        long res = 1;
        for(int i=2; i<=n; i++){
            res *= i;
        }
        return res;
    }
}


//Final Output:
// Time taken for sequential stream processing: 28 ms
// Time taken for parallel stream processing: 10 ms
// Expected Cumulative sum: [1,3,6,10,15]
// Actual result with parallel processing: [15, 14, 12, 9, 5]
// 11
// 12
// 13
// 14
// 15
// 16
// 17
// 18
// 19
// 20
// 21
// 22
// 23
// 24
// 25
// 26
// 27
// 28
// 29
// 30
// 31
// 32
// 33
// 34
// 35
// 36
// 37
// 38
// 39
// 40
// 41
// 42
// 43
// 44
// 45
// 46
// 47
// 48
// 49
// 50
// 51
// 52
// 53
// 54
// 55
// 56
// 57
// 58
// 59
// 60
// [APPLE, BANANA, ORANGE, KIWI, PEAR, GRAPE]
// [HELLO, WORLD, JAVA, STREAMS, ARE, POWERFUL, FLATMAP, IS, USEFUL]
// max: 66
// min: 1
// using forEach with parallel stream
// 74965123108
// using forEachOrdered with parallel stream
// 12345678910