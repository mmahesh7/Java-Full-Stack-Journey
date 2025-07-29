
#  OOP 1 | Introduction & Concepts – Classes, Objects, Constructors, Keywords  
---

##  1. What is Object-Oriented Programming (OOP)?

- A programming approach/style that models/represents real-world entities via **classes** and **objects**.
- **Four pillars of OOP:**
  - Encapsulation
  - Abstraction
  - Inheritance
  - Polymorphism

---

##  2. Classes and Objects

###  Class
- Blueprint/template to create objects.

```java
class Person {
    String name;
    int age;

    void greet() {
        System.out.println("Hello! My name is " + name);
    }
}
```

###  Object
- Instance of a class.

```java
public class Main {
    public static void main(String[] args) {
        Person p1 = new Person();
        p1.name = "Mahesh";
        p1.age = 20;
        p1.greet();
    }
}
```

---

##  3. Constructors
- Special method to initialize an object.
- No return type, same name as the class.

###  Default Constructor

```java
class Person {
    Person() {
        System.out.println("Person created.");
    }
}
```

###  Parameterized Constructor

```java
class Person {
    String name;
    int age;

    Person(String n, int a) {
        name = n;
        age = a;
    }
}
```

###  Constructor Overloading

```java
class Demo {
    Demo() {
        System.out.println("Default constructor");
    }

    Demo(int x) {
        System.out.println("Parameterized constructor with value: " + x);
    }
}
```

###  Copy Constructor
- Used to create a new object by copying another.
- Provides a deep copy of the object's state.

```java
class Student {
    String name;
    int age;

    // Copy constructor
    Student(Student original) {
        this.name = original.name;
        this.age = original.age;
    }

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("Mahesh", 20);
        Student s2 = new Student(s1); // s2 is a copy of s1

        System.out.println(s2.name); // Output: Mahesh
    }
}
```

---

## 4. Memory Management

- **Stack** → Method calls, local variables  
- **Heap** → Objects, reference types  
- **Method Area** → Class-level static and metadata  

### 🔍 Key Points:
- Each object has its own copy of non-static fields.
- Static fields are shared across all objects.
- Garbage Collector reclaims unused memory.

---

## 5. Important Java Keywords

| Keyword     | Description                      |
|-------------|----------------------------------|
| `this`      | Refers to current object         |
| `new`       | Instantiates objects             |
| `final`     | Makes variable/method immutable  |
| `static`    | Shared across all instances      |
| `public`/`private` | Access control            |
| `void`      | No return type                   |

### Using `this` keyword

```java
class Car {
    String model;

    Car(String model) {
        this.model = model;
    }
}
```

---

## 6. Wrapper Classes

- Converts primitive types to object types (Boxing and Unboxing).

### 🔁 Example

```java
Integer num = Integer.valueOf(5);   // Boxing
int x = num.intValue();             // Unboxing
```

### 🔄 Primitive ↔ Wrapper Mapping

| Primitive | Wrapper     |
|-----------|-------------|
| `int`     | `Integer`   |
| `char`    | `Character` |
| `double`  | `Double`    |
| `boolean` | `Boolean`   |

---

## 7. Constructors vs Methods

| Feature        | Constructor           | Method                  |
|----------------|------------------------|--------------------------|
| Name           | Same as class          | Any valid identifier     |
| Return Type    | None                   | Can have return type     |
| Purpose        | Object initialization  | Perform actions/logic    |
| Invocation     | Implicit (`new`)       | Explicit (method call)   |

---

## 8. Object Creation Flow

1. JVM allocates memory in the **heap** for object.
2. Constructor initializes fields.
3. Reference to the object is stored in the **stack**.


