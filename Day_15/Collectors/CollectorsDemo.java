package Collectors;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CollectorsDemo {
    public static void main(String[] args) {
        //Collectors is a utility class
        //it provides a set of methods to create common collectors
    
        //1. Collecting to a List
        List<String> names = Arrays.asList("Anuvind","Koushik","Srikanth");
        List<String> res = names.stream().filter(name -> name.startsWith("A")).collect(Collectors.toList());
        System.out.println(res);// [Anuvind]
        
        //2. Collecting to a Set
        List<Integer> nums = Arrays.asList(1,2,3,4,5,3,2,5);
        Set<Integer> set = nums.stream().collect(Collectors.toSet());
        System.out.println(set);// [1, 2, 3, 4, 5]

        //3. Collecting to a specific collection
        ArrayDeque<String> collect = names.stream().collect(Collectors.toCollection(()-> new ArrayDeque<>()));
        System.out.println(collect.getClass());// class java.util.ArrayDeque

        //4. joining strings
        String joining = names.stream().map(String::toUpperCase).collect(Collectors.joining(", "));
        System.out.println(joining);// ANUVIND, KOUSHIK, SRIKANTH

        //5. Summarizing data
        //it helps generate statistical summary of collection - sum, miun, max, average, count etc
        List<Integer> numbers = Arrays.asList(1,3,5,7,11);
        //we use the supplier summarizingInt() of IntSummaryStatistics class to achieve this
        IntSummaryStatistics statistics = numbers.stream().collect(Collectors.summarizingInt(x->x));
        System.out.println("Count: " + statistics.getCount());// Count: 5
        System.out.println("Sum: " + statistics.getSum());// Sum: 27
        System.out.println("Average: " + statistics.getAverage());// Average: 5.4
        System.out.println("Min: " + statistics.getMin());// Min: 1

        //6. Collecting averages
        System.out.println(numbers.stream().collect(Collectors.averagingInt(x->x)));// 5.4

        //7. Counting elements
        System.out.println(numbers.stream().collect(Collectors.counting()));// 5
        

        //8. Grouping Elements
        List<String> words = Arrays.asList("hello", "world","mk","java","streams", "collecting");
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length)));
        // {2=[mk], 4=[java], 5=[hello, world], 7=[streams], 10=[collecting]}
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length, Collectors.joining(", "))));
        // {2=mk, 4=java, 5=hello, world, 7=streams, 10=collecting}
        System.out.println(words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting())));
        // {2=1, 4=1, 5=2, 7=1, 10=1}
        TreeMap<Integer, Long> treeMap = words.stream().collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting()));
        System.out.println(treeMap);
        // {2=1, 4=1, 5=2, 7=1, 10=1} -TreeMap

        //9. Partitioning Elements
        //partitions the elements into two groups (true and false) based on a predicate
        System.out.println(words.stream().collect(Collectors.partitioningBy(x-> x.length() > 5)));
        // {false=[hello, world, mk, java], true=[streams, collecting]}

        //10. Mapping and Collecting
        //applies a mapping function before collecting
        System.out.println(words.stream().collect(Collectors.mapping(x -> x.toUpperCase(), Collectors.toList())));
        // [HELLO, WORLD, MK, JAVA, STREAMS, COLLECTING]

        //11. toMap()
        System.out.println(names.stream().collect(Collectors.toMap(x -> x.toUpperCase(), x -> x.length())));
        // {SRIKANTH=8, ANUVIND=7, KOUSHIK=7}
    }
}
