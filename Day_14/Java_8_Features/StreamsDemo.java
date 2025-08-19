package Java_8_Features;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsDemo {
    public static void main(String[] args) {
        //It is introduced in java 8
        //A stream is a sequence of elements supports functional and declarative programming

        //How to use streams?
        //Source , Intermediate operation and terminal operation
        
        // Stream is an interface
        //Here is the traditional approach
        List<Integer> list =  Arrays.asList(1,2,3,4,5);
        int cnt= 0;
        for(int i: list) {
            if (i%2 == 0) {
                cnt++;
            }
        }
        System.out.println(cnt);
        //Instead we can achieve the same using stream in this way
        System.out.println(list.stream().filter(x -> x%2 == 0).count());
        

        //* Different Ways to create stream source

        //1. From Collections
        List<Integer> li = Arrays.asList(1,2,3,4,5);
        Stream<Integer> stream = list.stream();

        //2. From Arrays
        String[] array = {"A","B","C"};
        Stream<String> stream1 = Arrays.stream(array);

        //3. using Stream.of()
        Stream<String> stream3 = Stream.of("A","B","C","D");

        //4. Infinite streams
        Stream<Integer> generate = Stream.generate(() -> 1);
        Stream.iterate(1, x->x+1);


        //* Intermediate Operations

        //It transforms one stream into another stream
        //They dont execute until a terminal operation is involved
        List<String> list2 = Arrays.asList("AB","BC","CD","AD");
        Stream<String> filteredStream = list2.stream().filter(x -> x.startsWith("A"));// here filter is not done, since there is no terminal operation
        long res = list2.stream().filter(x -> x.startsWith("A")).count();//Here count() is the terminal operation
        System.out.println(res);//2

        //Few intermediate operations include:
        //filter()
        //map()
        //distinct()
        //sorted()
        //limit()
        //skip()



        /* Terminal operations */

        //1. collect()
        list2.stream().skip(1).collect(Collectors.toList());
        list2.stream().skip(1).toList();

        //2. forEach()
        list2.stream().forEach(x->System.out.println(x));

        //3. reduce()
        //here reduce() returns the sum of elements in list
        list2.stream().reduce((x,y) -> x+y);
        //or
        // Optional<Integer> optionalInteger = list.stream().reduce((x,y) -> x+y);
        // System.out.println(optionalInteger.get());//15
        //or
        Optional<Integer> optionalInteger = list.stream().reduce(Integer::sum);
        System.out.println(optionalInteger.get());//15

        //4. count()


        //5. anyMatch(), allMatch(), nonMatch()
        boolean b = list.stream().anyMatch(x -> x%2 ==0);
        System.out.println(b); // true

        boolean bc = list.stream().allMatch(x -> x%2 ==0);
        System.out.println(bc); // false
        boolean bcd = list.stream().noneMatch(x -> x%2 ==0);
        System.out.println(bcd); // false

        //6. findFirst(), findAny() - these are short-circuit operators
        System.out.println(list.stream().findFirst().get());// return first value of list - 1
        System.out.println(list.stream().findAny().get());// returns random value of list
    }
}
