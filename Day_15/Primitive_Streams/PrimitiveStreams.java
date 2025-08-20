package Primitive_Streams;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class PrimitiveStreams {
    public static void main(String[] args) {
        int nums[] = {1,2,3,4,5};
        Arrays.stream(nums);// This is of IntStream for primitive data types

        // Integer numbers[] = {1,2,3,4,5};
        // Arrays.stream(numbers);// This is Stream<Integer> for non-primitive or wrapper class

        //boxed() method is necessary to perform these methods
        System.out.println(IntStream.range(1, 5).boxed().collect(Collectors.toList()));// [1, 2, 3, 4]
        System.out.println(IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList()));// [1, 2, 3, 4, 5]

        DoubleStream doubles = new Random().doubles(5);
        System.out.println(doubles.boxed().toList());
        // [0.025804634995787223, 0.6795973938864965, 0.9331240657350928, 0.5769588862415057, 0.8236501708278672]

        IntStream intStream = new Random().ints(5);
        System.out.println(intStream.sum());
        //the below are commented because we cannot reuse streams
        // System.out.println(intStream.min());
        // System.out.println(intStream.max());
        // System.out.println(intStream.average());

    }
}
