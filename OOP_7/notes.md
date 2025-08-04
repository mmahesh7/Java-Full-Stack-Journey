# OOP 6 – Exception Handling, Enums, Object Cloning & More

## 1. Exception Handling in Java

###  What is an Exception?
- An **Exception** is an event that occurs during program execution and disrupts the normal flow.
- Java uses a robust mechanism to handle exceptions to avoid program crashes.

###  Error vs Exception

| Category   | Description |
|------------|-------------|
| **Error**  | Indicates serious issues (like memory overflow) that are **non-recoverable** |
| **Exception** | Indicates problems that are **recoverable**, and we can handle them using code |

**Examples**:
- `ArithmeticException` (e.g., divide by zero)
- `NullPointerException`

###  Exception Hierarchy

```
Object
 └── Throwable
     ├── Exception (can be handled)
     │    ├── CheckedException
     │    └── UncheckedException
     └── Error (cannot be handled)
```

###  Checked vs Unchecked Exceptions
- **Checked**: Must be handled at compile time (e.g., `IOException`)
- **Unchecked**: Occur at runtime (e.g., `NullPointerException`)

###  Important Keywords

| Keyword | Description |
|---------|-------------|
| `try`   | Wraps code that may throw an exception |
| `catch` | Handles the exception |
| `finally` | Always executes, even if an exception occurs |
| `throw` | Used to explicitly throw an exception |
| `throws` | Declares exceptions in method signature |

###  Syntax

```java
try {
    // risky code
} catch (ExceptionType e) {
    // exception handling
} finally {
    // always executed
}
```

###  Key Points
- Multiple `catch` blocks are allowed
- A `try` must be followed by at least one `catch` or `finally`
- You can create custom exceptions by extending the `Exception` class

###  Custom Exception Example

```java
// Custom Exception
public class MyExceptions extends Exception {
    public MyExceptions(String message) {
        super(message);
    }
}

// Method throwing exception
static int divide(int a, int b) throws ArithmeticException {
    if (b == 0) throw new ArithmeticException("Do not divide by zero!");
    return a / b;
}
```

---

## 2. Enums in Java

### 🔷 What are Enums?
- Enums are special classes that represent **fixed sets of constants**
- Enums are `public static final` by default
- Type-safe alternative to constants

###  Key Characteristics
- Cannot be extended (enums are final)
- Enum **constructors are private**
- Can implement interfaces
- Enum type is the name of the enum itself

###  Example

```java
enum Week implements A {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;

    Week() {
        System.out.println("Constructor called for " + this);
    }

    @Override
    public void Hello() {
        System.out.println("Hello from " + this);
    }
}
```

###  Internal Working

```java
public static final Week Monday = new Week();
```

###  Useful Methods

| Method            | Description                          |
|------------------|--------------------------------------|
| `values()`       | Returns all enum constants as array  |
| `ordinal()`      | Returns position (index)             |
| `valueOf(String)`| Returns enum constant by name        |

---

## 3. Object Cloning

###  What is Cloning?
- Creating an **exact copy** of an object in memory

###  Requirements
- Class must **implement `Cloneable`**
- Must **override the `clone()`** method
- Handle or declare `CloneNotSupportedException`

###  Example

```java
public class Human implements Cloneable {
    int age;
    String name;
    int[] arr;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); // Shallow copy
    }
}
```

---

## 4. Shallow vs Deep Copy

###  Shallow Copy
- **Primitives**: Values are copied
- **Objects/References**: Memory address is copied
- Changes to shared references affect both original & clone

###  Example

```java
Human h1 = new Human(20, "John", new int[]{1,2,3});
Human h2 = (Human) h1.clone(); // Shallow copy

h2.arr[0] = 100;
// h1.arr[0] is also 100 now!
```

###  Deep Copy
- Manual duplication of reference variables (like arrays)

###  Deep Copy Implementation

```java
@Override
public Object clone() throws CloneNotSupportedException {
    Human cloned = (Human) super.clone();
    cloned.arr = new int[this.arr.length];
    for (int i = 0; i < this.arr.length; i++) {
        cloned.arr[i] = this.arr[i];
    }
    return cloned;
}
```

---

## 5. Java Collection Framework (Overview)

###  What is Collection Framework?
- A set of **classes and interfaces** to store and manipulate data in groups

###  Main Hierarchy

```
Collection Interface
 ├── List (ArrayList, LinkedList, Vector)
 ├── Set (HashSet, TreeSet)
 └── Queue (PriorityQueue, Deque)

Map Interface (HashMap, TreeMap, LinkedHashMap)
```

### 💡 Key Interfaces

| Interface | Description |
|----------|-------------|
| Collection | Root interface for all collections |
| List      | Ordered, allows duplicates |
| Set       | No duplicates |
| Map       | Key-value pairs |
| Queue     | Follows FIFO order |

---

## 6. Vector Class in Java

###  What is Vector?
- Legacy class from Java 1.0
- Implements `List`
- **Thread-safe**: All methods are synchronized
- **Dynamically grows** in size

###  Vector vs ArrayList

| Feature         | Vector               | ArrayList           |
|----------------|----------------------|---------------------|
| Thread-safe     |  Yes (synchronized) |  No               |
| Performance     | Slow (due to sync)   | Fast               |
| Legacy          | Yes                  | No                  |
| Growth Policy   | Doubles (100%)       | Grows by 50%       |

###  When to Use Vector
- When working with **multi-threaded** environments
- When working with **legacy code**
- When synchronization is **important**

###  Basic Vector Operations

```java
Vector<String> vector = new Vector<>();
vector.add("Element1");
vector.addElement("Element2"); // Legacy method
vector.remove(0);String element = vector.get(0);
int size = vector.size();
```

---

