# Day 15 - Parallel Streams, Additional Stream Operations, Collectors Framework and Primitive Streams



## 1. Parallel Streams

### Definition
Parallel streams enable automatic parallel processing of stream operations by dividing the work across multiple threads using the ForkJoinPool framework for concurrent execution.

### Creating Parallel Streams
```java
// Two ways to create parallel streams
list.stream().parallel()     // Convert existing stream
list.parallelStream()        // Create parallel stream directly
```

### Performance Comparison
```java
// Sequential processing
long startTime = System.currentTimeMillis();
List<Integer> list = Stream.iterate(1, x -> x+1).limit(2000).toList();
List<Long> results = list.stream()
    .map(ParallelStream::factorial)  // Calculate factorial for each number
    .toList();
long endTime = System.currentTimeMillis();
System.out.println("Sequential: " + (endTime - startTime) + " ms"); // 28 ms

// Parallel processing
startTime = System.currentTimeMillis();
results = list.parallelStream()
    .map(x -> factorial(x))
    .toList();
endTime = System.currentTimeMillis();
System.out.println("Parallel: " + (endTime - startTime) + " ms"); // 10 ms
```

### Thread Safety Issues
```java
// This doesn't work correctly with parallel streams!
List<Integer> nums = Arrays.asList(1,2,3,4,5);
AtomicInteger sum = new AtomicInteger(0);
List<Integer> cumulativeSum = nums.parallelStream()
    .map(sum::addAndGet)
    .toList();

System.out.println("Expected: [1,3,6,10,15]");
System.out.println("Actual: " + cumulativeSum); // Wrong result like [15, 14, 12, 9, 5]
```
**Reason:** Multiple threads trying to update the sum simultaneously, causing race conditions.

### Order Preservation
```java
List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

// Unordered parallel execution
numbers.parallelStream().forEach(System.out::print); // 76310415892

// Ordered parallel execution
numbers.parallelStream().forEachOrdered(System.out::print); // 12345678910
```

### When to Use Parallel Streams

** Suitable for:**
- Large datasets (1000+ elements)
- CPU-intensive operations
- Independent operations (no shared state)
- Stateless transformations

** Avoid when:**
- Small datasets (overhead > benefit)
- I/O bound operations
- Operations requiring order preservation
- Shared mutable state

---

## 2. Additional Stream Operations

### Intermediate Operations

#### peek()
**Definition:** Performs an action on each element as it passes through the stream without transforming it.

```java
// Debug and monitor stream processing
long count = Stream.iterate(1, x -> x+1)
    .skip(10)           // Skip first 10 numbers
    .limit(50)          // Take next 50 numbers  
    .peek(System.out::println)  // Print each number as it passes through
    .count();           // Count them (returns 50)
```

**Use Cases:**
- Debugging stream pipelines
- Logging intermediate values
- Monitoring stream flow

#### flatMap()
**Definition:** Transforms each element into a stream and flattens the results into a single stream.

##### Example 1: Flattening Nested Collections
```java
List<List<String>> nestedLists = Arrays.asList(
    Arrays.asList("apple", "banana"),
    Arrays.asList("orange", "kiwi"),
    Arrays.asList("pear", "grape")
);

// Flatten all into one list
List<String> flattenedList = nestedLists.stream()
    .flatMap(list -> list.stream())        // Flatten each list
    .map(String::toUpperCase)              // Convert to uppercase
    .toList();
// Result: [APPLE, BANANA, ORANGE, KIWI, PEAR, GRAPE]
```

##### Example 2: Processing Text Data
```java
List<String> sentences = Arrays.asList(
    "Hello world",
    "Java streams are powerful", 
    "flatMap is useful"
);

// Split each sentence and collect all words
List<String> allWords = sentences.stream()
    .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
    .map(String::toUpperCase)
    .toList();
// Result: [HELLO, WORLD, JAVA, STREAMS, ARE, POWERFUL, FLATMAP, IS, USEFUL]
```

**Use Cases:**
- Processing nested data structures
- Text processing and tokenization
- Combining multiple collections
- Working with optional values

### Terminal Operations

#### toArray()
```java
// Convert stream to array
Object[] array = Stream.of(1,2,3).toArray();
```

#### min() & max()
```java
// Find maximum value
Integer max = Stream.of(1,2,4,66,43)
    .max((o1,o2) -> o1-o2)
    .get(); // 66

// Find minimum value  
Integer min = Stream.of(1,2,4,66,43)
    .min(Comparator.naturalOrder())
    .get(); // 1
```

#### forEachOrdered() vs forEach()
```java
List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

// Unordered execution with parallel streams
numbers.parallelStream().forEach(System.out::print); // Random order: 76310415892

// Maintains original order even with parallel streams
numbers.parallelStream().forEachOrdered(System.out::print); // 12345678910
```

---

## 3. Collectors Framework

### Definition
The `Collectors` utility class provides a set of predefined collectors that implement common reduction operations. Collectors are used with the `collect()` terminal operation to transform stream elements into different data structures.

### Key Collector Types

#### 1. Collection Collectors
```java
// Convert to List
List<String> names = Arrays.asList("Anuvind","Koushik","Srikanth");
List<String> filtered = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList()); // [Anuvind]

// Convert to Set (removes duplicates)
List<Integer> nums = Arrays.asList(1,2,3,4,5,3,2,5);
Set<Integer> uniqueNums = nums.stream()
    .collect(Collectors.toSet()); // [1, 2, 3, 4, 5]

// Convert to specific collection type
ArrayDeque<String> deque = names.stream()
    .collect(Collectors.toCollection(() -> new ArrayDeque<>()));
```

#### 2. String Operations
```java
// Join strings with delimiter
String result = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.joining(", ")); // "ANUVIND, KOUSHIK, SRIKANTH"
```

#### 3. Statistical Collectors
```java
// Generate statistical summary
List<Integer> numbers = Arrays.asList(1,3,5,7,11);
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(x -> x));

System.out.println("Count: " + stats.getCount());    // 5
System.out.println("Sum: " + stats.getSum());        // 27
System.out.println("Average: " + stats.getAverage()); // 5.4
System.out.println("Min: " + stats.getMin());        // 1

// Individual statistical operations
Double average = numbers.stream().collect(Collectors.averagingInt(x -> x)); // 5.4
Long count = numbers.stream().collect(Collectors.counting()); // 5
```

#### 4. Grouping Operations
```java
List<String> words = Arrays.asList("hello", "world","mk","java","streams", "collecting");

// Basic grouping by string length
Map<Integer, List<String>> groupedByLength = words.stream()
    .collect(Collectors.groupingBy(String::length));
// {2=[mk], 4=[java], 5=[hello, world], 7=[streams], 10=[collecting]}

// Grouping with downstream collector (joining)
Map<Integer, String> joinedByLength = words.stream()
    .collect(Collectors.groupingBy(String::length, Collectors.joining(", ")));
// {2=mk, 4=java, 5=hello, world, 7=streams, 10=collecting}

// Grouping with counting
Map<Integer, Long> countByLength = words.stream()
    .collect(Collectors.groupingBy(String::length, Collectors.counting()));
// {2=1, 4=1, 5=2, 7=1, 10=1}

// Custom map implementation
TreeMap<Integer, Long> treeMap = words.stream()
    .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting()));
```

#### 5. Partitioning Operations
```java
// Split data into two groups based on predicate
Map<Boolean, List<String>> partitioned = words.stream()
    .collect(Collectors.partitioningBy(x -> x.length() > 5));
// {false=[hello, world, mk, java], true=[streams, collecting]}
```

#### 6. Mapping Collector
```java
// Transform elements before collecting
List<String> upperCased = words.stream()
    .collect(Collectors.mapping(String::toUpperCase, Collectors.toList()));
// [HELLO, WORLD, MK, JAVA, STREAMS, COLLECTING]
```

#### 7. Map Creation
```java
// Create map from stream elements
Map<String, Integer> nameToLength = names.stream()
    .collect(Collectors.toMap(
        x -> x.toUpperCase(),  // key mapper
        x -> x.length()        // value mapper
    )); // {SRIKANTH=8, ANUVIND=7, KOUSHIK=7}

// Handle duplicate keys with merge function
List<String> fruits = Arrays.asList("Apple","Banana","Banana","Mango","Apple","Banana");
Map<String, Integer> fruitCount = fruits.stream()
    .collect(Collectors.toMap(
        k -> k,           // key
        v -> 1,           // initial value
        (x, y) -> x + y   // merge function for duplicates
    )); // {Apple=2, Mango=1, Banana=3}
```

---

## 4. Primitive Streams

### Definition
Specialized stream implementations for primitive types (int, long, double) that avoid boxing/unboxing overhead and provide additional operations specific to numeric types.

### Stream Types
- **IntStream** - for int values
- **LongStream** - for long values  
- **DoubleStream** - for double values

### Creating Primitive Streams
```java
// From primitive arrays
int[] nums = {1,2,3,4,5};
IntStream intStream = Arrays.stream(nums);        // IntStream (efficient)

// From wrapper arrays
Integer[] numbers = {1,2,3,4,5};
Stream<Integer> stream = Arrays.stream(numbers);  // Stream<Integer> (less efficient)
```

### Range Generation
```java
// Range methods for generating sequences
IntStream.range(1, 5)           // [1, 2, 3, 4] - excludes upper bound
    .boxed()                    // Convert to Stream<Integer>
    .collect(Collectors.toList());

IntStream.rangeClosed(1, 5)     // [1, 2, 3, 4, 5] - includes upper bound
    .boxed()
    .collect(Collectors.toList());
```

### Random Number Generation
```java
// Generate random primitive values
DoubleStream randomDoubles = new Random().doubles(5);  // 5 random doubles
List<Double> doubleList = randomDoubles.boxed().toList();

IntStream randomInts = new Random().ints(5);           // 5 random integers
int sum = randomInts.sum();                           // Built-in sum operation
```

### Boxing - Converting to Object Streams
```java
// Convert primitive stream to object stream for collectors
List<Integer> boxedInts = IntStream.range(1, 5)
    .boxed() // Convert IntStream to Stream<Integer>
    .collect(Collectors.toList());
```

### Advantages of Primitive Streams

**Performance Benefits:**
- **No Boxing/Unboxing**: Direct primitive operations
- **Memory Efficient**: Lower memory footprint
- **Built-in Operations**: sum(), average(), min(), max()
- **Optimized for Numbers**: Designed specifically for numeric computations

---

## 5. Exercises

### Exercise 1: Collecting Names by Length
```java
List<String> names = Arrays.asList("mk","mahesh","java","stream");
System.out.println(names.stream().collect(Collectors.groupingBy(String::length)));
// Output: {2=[mk], 4=[java], 6=[mahesh, stream]}
```

### Exercise 2: Counting Word Occurrences
```java
String sentence = "hello world java hello world";
System.out.println(Arrays.stream(sentence.split(" "))
    .collect(Collectors.groupingBy(x -> x, Collectors.counting())));
// Output: {java=1, world=2, hello=2}
```

### Exercise 3: Partitioning Even and Odd Numbers
```java
List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8,9);
System.out.println(numbers.stream()
    .collect(Collectors.partitioningBy(x -> x%2 == 0)));
// Output: {false=[1, 3, 5, 7, 9], true=[2, 4, 6, 8]}
```

### Exercise 4: Summing Values in a Map
```java
Map<String, Integer> fruitPrices = new HashMap<>();
fruitPrices.put("Apple", 10);
fruitPrices.put("Banana", 12);
fruitPrices.put("Mango", 14);

// Method 1: Using reduce
System.out.println(fruitPrices.values().stream().reduce(Integer::sum).get()); // 36

// Method 2: Using collector
System.out.println(fruitPrices.values().stream().collect(Collectors.summingInt(x->x))); // 36
```

### Exercise 5: Create Map from Stream Elements
```java
List<String> fruits = Arrays.asList("Apple","Banana","Mango");
System.out.println(fruits.stream()
    .collect(Collectors.toMap(x -> x.toUpperCase(), x -> x.length())));
// Output: {APPLE=5, MANGO=5, BANANA=6}
```

### Exercise 6: Using Merge Function for Duplicates
```java
List<String> fruits = Arrays.asList("Apple","Banana","Banana","Mango","Apple","Banana");
System.out.println(fruits.stream()
    .collect(Collectors.toMap(k -> k, v -> 1, (x,y) -> x + y)));
// Output: {Apple=2, Mango=1, Banana=3}
```

---

## Important Notes & Best Practices

### Stream Reusability
**Critical Rule:** Streams cannot be reused after a terminal operation has been called.

```java
List<String> names = Arrays.asList("Mahesh", "Anuvind", "Koushik", "Srikanth");
Stream<String> stream = names.stream();

stream.forEach(System.out::println);  // Terminal operation

// This will throw IllegalStateException
List<String> upperCase = stream.map(String::toUpperCase).toList();
// Exception: stream has already been operated upon or closed
```

### Performance Guidelines

1. **Choose Appropriate Stream Type**
   ```java
   // Use primitive streams for numeric operations
   IntStream.range(1, 1000).sum();              // Efficient
   ```

2. **Parallel Stream Usage**
   - Use for large datasets with CPU-intensive operations
   - Avoid for small datasets or I/O operations

3. **Infinite Stream Safety**
   ```java
   Stream.iterate(0, n -> n + 1)
       .limit(100)          // Always limit infinite streams
       .forEach(System.out::println);
   ```

### Common Patterns

- **Data Transformation**: Use collectors to convert streams to desired data structures
- **Statistical Analysis**: Leverage statistical collectors for numeric data
- **Grouping Operations**: Use groupingBy for organizing data
- **Performance Optimization**: Choose primitive streams for numeric computations

---

## Summary

This comprehensive guide covers advanced Java Streams concepts:

**✅ Parallel Streams** - Performance optimization through concurrent processing  
**✅ Advanced Operations** - Additional intermediate and terminal operations  
**✅ Collectors Framework** - Comprehensive data transformation techniques  
**✅ Primitive Streams** - Efficient handling of numeric data  
**✅ Practical Exercises** - Hands-on examples for concept reinforcement  

These concepts enable efficient data processing in Java applications, from simple transformations to complex parallel computations.

Checkout GitHub for complete code snippets.