package Collectors_Exercises;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Exercises {
    public static void main(String[] args) {
        //Exercise 1: Collecting Names by length
        List<String> l1 = Arrays.asList("mk","mahesh","java","stream");
        System.out.println(l1.stream().collect(Collectors.groupingBy(String::length)));
        // {2=[mk], 4=[java], 6=[mahesh, stream]}

        //Exercise 2: Counting word occurences
        String sentence = "hello world java hello world";
        System.out.println(Arrays.stream(sentence.split(" ")).collect(Collectors.groupingBy(x -> x, Collectors.counting())));
        // {java=1, world=2, hello=2}

        //Exercise 3: Partitioning Even and Odd
        List<Integer> l2 = Arrays.asList(1,2,3,4,5,6,7,8,9);
        System.out.println(l2.stream().collect(Collectors.partitioningBy(x -> x%2 == 0)));
        //{false=[1, 3, 5, 7, 9], true=[2, 4, 6, 8]}

        //Exercise 4: Summing values in a map
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 10);
        map.put("Banana", 12);
        map.put("Mango", 14);

        System.out.println( map.values().stream().reduce(Integer::sum).get());// 36
        System.out.println( map.values().stream().collect(Collectors.summingInt(x->x)));//36

        //Exercise 5: Create a map from Stream elements based on lengths
        List<String> fruits = Arrays.asList("Apple","Banana","Mango");
        System.out.println(fruits.stream().collect(Collectors.toMap(x -> x.toUpperCase(), x -> x.length())));
        // {APPLE=5, MANGO=5, BANANA=6}
        
        //Exercise 6: Using merge function
        List<String> fruits2 = Arrays.asList("Apple","Banana","Banana","Mango","Apple","Banana");
        System.out.println(fruits2.stream().collect(Collectors.toMap(k -> k, v -> 1, (x,y) -> x + y)));
        // {Apple=2, Mango=1, Banana=3}
    }
}
