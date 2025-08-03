# OOP 6 - Generics, Custom ArrayList, Lambda Expressions & Comparators

## Topics 

- **Generics** - Type-safe programming with parameterized types and generic interfaces
- **Custom ArrayList Implementation** - Building a dynamic array with generic support from scratch
- **Lambda Expressions** - Functional programming constructs for cleaner, more concise code
- **Comparators and Comparable** - Object comparison mechanisms for sorting and ordering
- **Practical Applications** - Real-world examples combining all concepts together

## Part 1: Generics - Type Safety and Flexibility

### What are Generics?

Generics provide **type safety** at compile time and eliminate the need for explicit type casting. They allow you to write classes, interfaces, and methods that work with different types while maintaining type safety.

### Benefits of Generics

- **Compile-time type checking** - Errors caught early
- **Elimination of type casting** - No need for explicit casting
- **Code reusability** - Same code works with different types
- **Performance improvement** - No boxing/unboxing overhead

### Generic Classes

A generic class is defined with type parameters that can be specified when creating instances.

**Syntax:**
```java
public class ClassName<T> {
    // T represents a type parameter
}
```

### Generic Interfaces

Interfaces can also be parameterized with generic types, making them flexible for different implementations.

**Basic Generic Interface:**
```java
public interface GenericInterface<T> {
    void display(T value);
}
```

**Implementation:**
```java
public class Main implements GenericInterface<Integer> {
    @Override
    public void display(Integer value) {
        System.out.println("Value: " + value);
    }
}
```

### Key Points About Generics

- **Type Parameters**: `<T>`, `<E>`, `<K, V>` are common conventions
- **Type Erasure**: Generic type information is removed at runtime
- **Wildcards**: `<?>`, `<? extends T>`, `<? super T>` for flexible type bounds
- **Raw Types**: Using generics without type parameters (discouraged)

## Part 2: Custom ArrayList Implementation

### Understanding the Need

ArrayList is a dynamic array that can grow and shrink during runtime. Understanding its implementation helps grasp how Java collections work internally.

### Core Components

1. **Internal Array**: Object array to store elements
2. **Size Tracking**: Current number of elements
3. **Capacity Management**: Automatic resizing when needed
4. **Generic Support**: Type safety through generics

### Complete Custom ArrayList Implementation

```java
package Day_6.Generics;

public class CustomGenericArrayList<T> {
    private Object[] data;                    // Internal storage
    private static int DEFAULT_SIZE = 10;     // Initial capacity
    private int size = 0;                     // Current number of elements

    public CustomGenericArrayList() {
        this.data = new Object[DEFAULT_SIZE];
    }

    public void add(T element){
        if(isFull()){ 
            resize();                         // Automatic resizing
        }
        data[size++] = element;               // Add and increment size
    }

    public boolean isFull() {
        return size == data.length;           // Check if resize needed
    }

    public void resize(){
        Object temp[] = new Object[data.length * 2];  // Double the capacity
        for(int i=0; i< data.length; i++){
            temp[i] = data[i];                // Copy existing elements
        }
        data = temp;                          // Replace old array
    }    

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        T removed = (T)(data[index]);         // Type casting required
        for(int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];            // Shift elements left
        }
        size--;     
        return removed;   
    }

    public void set(int index, T value) {
        data[index] = value;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T)(data[index]);              // Type casting required
    }

    @Override
    public String toString() {
        return "CustomGenericArrayList{" +
                "data=" + java.util.Arrays.toString(data) +
                ", size=" + size +
                '}';
    }
}
```

### Key Implementation Details

#### **1. Why Object Array?**
```java
private Object[] data;  // Not T[] data
```
- **Reason**: Java doesn't allow creation of generic arrays directly
- **Solution**: Use Object array and cast when retrieving

#### **2. Type Casting with @SuppressWarnings**
```java
@SuppressWarnings("unchecked")
public T get(int index) {
    return (T)(data[index]);
}
```
- **Purpose**: Suppress compiler warnings about unchecked casting
- **Safety**: We ensure type safety through our add() method

#### **3. Automatic Resizing Strategy**
```java
public void resize(){
    Object temp[] = new Object[data.length * 2];  // Double capacity
    // Copy all elements to new array
    data = temp;
}
```
- **Growth Factor**: 2x (doubles the size)
- **Time Complexity**: O(n) for copying, but amortized O(1) for additions

### Usage Example

```java
public static void main(String[] args) {
    CustomGenericArrayList<Integer> list = new CustomGenericArrayList<>();
    
    // Type safety - this would cause compile error
    // list.add("mmk");  // Error, since list is of type Integer

    // Adding elements - automatic resizing happens
    for(int i = 0; i < 13; i++) {
        list.add(i * 2);
    }
    System.out.println(list);
    // Output: CustomGenericArrayList{data=[0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, null, null, null, null, null, null, null], size=13}
}
```

## Part 3: Lambda Expressions - Functional Programming

### What are Lambda Expressions?

Lambda expressions provide a **concise way to represent anonymous functions**. They enable functional programming style in Java and are especially useful with collections and interfaces.

**Syntax:**
```java
(parameters) -> expression
(parameters) -> { statements; }
```

### Functional Interfaces

A functional interface has **exactly one abstract method** and can be implemented using lambda expressions.

```java
interface Operation {
    int operation(int a, int b);  // Single abstract method
}
```

### Lambda Expression Examples

#### **1. Basic Lambda Usage**
```java
// Traditional way with anonymous class
Operation add = new Operation() {
    public int operation(int a, int b) {
        return a + b;
    }
};

// Lambda expression - much cleaner!
Operation add = (a, b) -> (a + b);
Operation sub = (a, b) -> (a - b);
Operation mul = (a, b) -> (a * b);
```

#### **2. Using Lambda with Collections**
```java
ArrayList<Integer> arr = new ArrayList<>();
for (int i = 0; i < 5; i++) {
    arr.add(i + 1);
}

// Lambda with forEach - prints each element doubled
arr.forEach((element) -> { 
    System.out.println(element * 2);
});
``` 

#### **3. Complete Lambda Calculator Example**
```java
public class LambdaFunctions {
    public static void main(String[] args) {
        // Creating different operations using lambdas
        Operation add = (a,b) -> (a+b);
        Operation sub = (a,b) -> (a-b);
        Operation mul = (a,b) -> (a*b);

        LambdaFunctions myCal = new LambdaFunctions();
        System.out.println("Addition: " + myCal.operate(5, 3, add));
        System.out.println("Subtraction: " + myCal.operate(5, 3, sub));
        System.out.println("Multiplication: " + myCal.operate(5, 3, mul));
    }
    
    // Method that accepts lambda as parameter
    private int operate(int a, int b, Operation op) {
        return op.operation(a, b);
    }
}

interface Operation {
    int operation(int a, int b);
}
```

**Output:**
```
Addition: 8
Subtraction: 2
Multiplication: 15
```

## Part 4: Comparators and Comparable - Object Comparison

### The Problem: Comparing Custom Objects

```java
// This doesn't work - compilation error!
if(student1 > student2) {
    System.out.println("Student1 is greater");
}
```

**Why it fails**: Java doesn't know how to compare custom objects. Should it compare by marks, roll number, or name?

### Solution 1: Comparable Interface

Implement `Comparable<T>` to define **natural ordering** for your class.

#### **Student Class with Comparable**
```java
public class Student implements Comparable<Student>{
    int rollno;
    float marks;

    public Student(int rollno , float marks){
        this.rollno = rollno;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return marks+"";  // Display marks when printed
    }

    @Override
    public int compareTo(Student other){
        int diff = (int)(this.marks - other.marks);
        return diff;
        // Return: negative if this < other
        //         zero if this == other  
        //         positive if this > other
    }
}
```

#### **Using Comparable for Comparison**
```java
Student koushik = new Student(36, 85.12f);
Student anuvind = new Student(33, 96.55f);

if(koushik.compareTo(anuvind) > 0) {
    System.out.println("Koushik has scored more marks than Anuvind.");
}
else if(koushik.compareTo(anuvind) < 0) {
    System.out.println("Anuvind has scored more marks than Koushik.");
} else {
    System.out.println("Both have scored equal marks.");
}
```

### Solution 2: Comparator Interface

Use `Comparator<T>` for **custom sorting criteria** without modifying the original class.

#### **Traditional Comparator (Anonymous Class)**
```java
Student[] list = {anuvind, koushik, pavan, srikanth, praneeth};

// Sort by roll number using anonymous Comparator
Arrays.sort(list, new Comparator<Student>(){
    public int compare(Student s1, Student s2){
        return s1.rollno - s2.rollno;     // Ascending order
        // return -(s1.rollno - s2.rollno); // Descending order
    }
});
```

#### **Lambda Expression Comparator (Modern Approach)**
```java
// Lambda makes it much cleaner!
Arrays.sort(list, (s1, s2) -> (s1.rollno - s2.rollno)); // Ascending
Arrays.sort(list, (s1, s2) -> (s2.rollno - s1.rollno)); // Descending
```

### Complete Sorting Example

```java
public class Main {
    public static void main(String[] args) {
        Student anuvind = new Student(33, 96.55f);
        Student koushik = new Student(36, 85.12f);
        Student pavan = new Student(44, 89.97f);
        Student srikanth = new Student(30, 90.55f);
        Student praneeth = new Student(50, 196.55f);

        Student[] list = {anuvind, koushik, pavan, srikanth, praneeth};

        System.out.println("Original: " + Arrays.toString(list));
        
        // Sort by marks (using Comparable - natural ordering)
        Arrays.sort(list);
        System.out.println("By Marks: " + Arrays.toString(list));
        
        // Sort by roll number (using Comparator with lambda)
        Arrays.sort(list, (s1, s2) -> (s2.rollno - s1.rollno)); // Descending
        System.out.println("By Roll No: " + Arrays.toString(list));
    }
}
```

### Comparable vs Comparator

| **Comparable** | **Comparator** |
|----------------|----------------|
| Modify the class itself | External comparison logic |
| Single sorting criteria | Multiple sorting criteria |
| `compareTo(T other)` | `compare(T o1, T o2)` |
| Used with `Arrays.sort(array)` | Used with `Arrays.sort(array, comparator)` |

## Part 5: Combining All Concepts - Practical Example

### Generic ArrayList with Lambda Sorting

```java
public class AdvancedExample {
    public static void main(String[] args) {
        // 1. Generic ArrayList
        CustomGenericArrayList<Student> students = new CustomGenericArrayList<>();
        
        // 2. Adding students
        students.add(new Student(33, 96.55f));
        students.add(new Student(36, 85.12f));
        students.add(new Student(44, 89.97f));
        
        // 3. Convert to array for sorting
        Student[] array = new Student[students.size()];
        for(int i = 0; i < students.size(); i++) {
            array[i] = students.get(i);
        }
        
        // 4. Sort using lambda expressions
        Arrays.sort(array, (s1, s2) -> (s2.rollno - s1.rollno));
        
        // 5. Process with lambda
        Arrays.stream(array)
              .forEach(student -> System.out.println("Roll: " + student.rollno + 
                                                   ", Marks: " + student.marks));
    }
}
```

## Error Explanations and Common Pitfalls

### **1. Generic Type Erasure**
```java
// This won't work as expected
CustomGenericArrayList<String> stringList = new CustomGenericArrayList<>();
CustomGenericArrayList<Integer> intList = new CustomGenericArrayList<>();

// At runtime, both are just CustomGenericArrayList due to type erasure
```

### **2. Raw Type Usage**
```java
// Don't do this - loses type safety
CustomGenericArrayList rawList = new CustomGenericArrayList();
rawList.add("String");
rawList.add(123);  // No compile-time error, but runtime issues possible
```

### **3. Comparator Logic Errors**
```java
// Wrong - can cause integer overflow
return s1.rollno - s2.rollno;

// Better - safe comparison
return Integer.compare(s1.rollno, s2.rollno);
```

### **4. Lambda Expression Scope**
```java
int multiplier = 10;
// multiplier must be effectively final
Operation multiply = (a, b) -> a * b * multiplier;
// multiplier = 20;  // This would cause compilation error
```

## Key Takeaways

### **Important Concepts**
- **Generics**: Provide compile-time type safety and eliminate casting
- **Custom Collections**: Understanding internal implementation helps with performance optimization
- **Lambda Expressions**: Enable functional programming and cleaner code
- **Comparison Mechanisms**: Comparable for natural ordering, Comparator for custom sorting

### **Best Practices**
- **Always use generics** for type safety
- **Prefer lambda expressions** over anonymous classes for functional interfaces
- **Implement Comparable** for natural ordering of your classes
- **Use Comparator** for multiple sorting criteria
- **Handle edge cases** in custom implementations (null checks, bounds checking)

