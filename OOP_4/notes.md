
# 📘 OOP 4 | Access Control, Object Class and Abstract Classes

Today’s focus was on **Access Control**, the universal **Object Class** and how **Abstract Classes** help define common structure while forcing subclasses to complete the missing behavior.

The core OOP principles involved were **encapsulation** and **abstraction**.



---

## Access Control Modifiers in Java

Access modifiers define the visibility of classes, methods, and variables. They are:

- `public`
- `private`
- `protected`
- default (no modifier)

### 🔒 Access Modifier Visibility Table

| Modifier    | Same Class | Same Package | Subclass (Other Pkg) | Other Pkg (Non-Subclass) |
|-------------|------------|--------------|-----------------------|---------------------------|
| `public`    | +         | +           | +                    | +                        |
| `protected` | +         | +           | +                    |                         |
| default     | +         | +           |                     |                         |
| `private`   | +         |            |                     |                         |

---


We’ll use a class `A` from `package1` for demonstration.

### 📄 package1/A.java

```java
package package1;

public class A {
    public int num1 = 10;       // Accessible everywhere
    private int num2 = 20;      // Only within class A
    protected int num3 = 30;    // Same package + subclasses outside package
    int num4 = 40;              // Default: only within same package

    public void display() {
        System.out.println("Inside class A:");
        System.out.println("Public num1: " + num1);
        System.out.println("Private num2: " + num2);
        System.out.println("Protected num3: " + num3);
        System.out.println("Default num4: " + num4);
    }
}
```


### 1️⃣ Same Package Access – Class B

`package1/B.java`

```java
package package1;

public class B {
    public static void main(String[] args) {
        A objA = new A();

        System.out.println("Inside class B (same package):");
        System.out.println("Public num1: " + objA.num1);         // ✅
        // System.out.println("Private num2: " + objA.num2);     // ❌ Compile Error
        System.out.println("Protected num3: " + objA.num3);      // ✅
        System.out.println("Default num4: " + objA.num4);        // ✅
    }
}
```

---

### 2️⃣ Different Package + Subclass – Class C

`package2/C.java`

```java
package package2;

import package1.A;

public class C extends A {
    public void accessMembers() {
        System.out.println("Inside subclass C (different package):");
        System.out.println("Public num1: " + num1);          // ✅
        // System.out.println("Private num2: " + num2);      // ❌
        System.out.println("Protected num3: " + num3);       // ✅ (Inherited)
        // System.out.println("Default num4: " + num4);      // ❌
    }

    public static void main(String[] args) {
        C obj = new C();
        obj.accessMembers();
    }
}
```

---

### 3️⃣ Different Package + Non-Subclass – Class D

`package2/D.java`

```java
package package2;

import package1.A;

public class D {
    public static void main(String[] args) {
        A obj = new A();

        System.out.println("Inside class D (non-subclass, different package):");
        System.out.println("Public num1: " + obj.num1);        // ✅
        // System.out.println(obj.num2); // ❌ private
        // System.out.println(obj.num3); // ❌ protected
        // System.out.println(obj.num4); // ❌ default
    }
}
```

---

## ✅ When to Use Which Access Modifier?

| Use Case | Best Modifier |
|----------|---------------|
| Use when the class/member should be globally available | `public` |
| Use when you want full encapsulation; hide data from outside access | `private` |
|  Use when you want to allow access in inherited classes, but restrict it elsewhere. | `protected` |
| Use when you want to restrict usage to within the package only. | default (no modifier) |

---


## The `Object` Class in Java

The Object class is the "God" of all classes in Java. 
Every Java class directly or indirectly extends `java.lang.Object`.

public class Human { ... } is the same as public class Human extends Object { ... }

This means that every object you create, no matter the class, inherits the methods from the Object class. Let's look at the most important ones.

Let's use this simple Human class for demonstration:

```java
public class Human {
    String name;
    int age;

    public Human(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {
        Human h = new Human("Kunal", 24);
        System.out.println(h.toString());   // Default: Class@HashCode
        System.out.println(h.hashCode());
    }
}
```

---

### 🔁 Overriding `Object` Class Methods

```java
package Day_4.Object;

public class ObjectDemo extends Object {
    int num;
    float gpa;

    public ObjectDemo(int num, float gpa) {
        this.num = num;
        this.gpa = gpa;
    }
    public ObjectDemo(int num) {
        this(num, 0.0f); // Default GPA if not provided
    }
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        // return super.hashCode(); This is from Object class
        return num; // Custom hash code based on the num field
    }
    
    @Override
    public boolean equals(Object obj) {
        // return super.equals(obj);// This is from Object class
        // Custom equality check can be implemented here
        return this.num == ((ObjectDemo)obj).num;
    }

    public static void main(String[] args) {
        // ObjectDemo obj = new ObjectDemo(34);
        // System.out.println(obj.hashCode());// This will print the hash code based on num
        ObjectDemo obj1 = new ObjectDemo(34, 9.1f);

        ObjectDemo obj2 = new ObjectDemo(34, 8.5f); 
        
        //Comparing two objects
        if(obj1 == obj2) {// Checks for reference equality
            System.out.println("obj1 is equal to obj2");
        } 
        if(obj1.equals(obj2)) { // Checks for value equality
            System.out.println("obj1 is equal to obj2 using equals method");
        } 

        // Output: obj1 is equal to obj2 using equals method
    }
}

```
# 📘 Java Abstract Class

## 🔷 What is an Abstract Class?
An abstract class in Java is a class that cannot be instantiated and is meant to be inherited by other classes. It may contain abstract methods (without implementation) as well as non-abstract methods (with implementation).

---

## 🔹 Key Points:

1. **Cannot Create Objects**  
   - You cannot instantiate (create objects of) an abstract class directly.
   - However, you **can create reference variables** of an abstract class and assign objects of its subclass.

   ```java
   abstract class Animal {
       abstract void sound();
   }

   class Dog extends Animal {
       void sound() {
           System.out.println("Bark");
       }
   }

   public class Main {
       public static void main(String[] args) {
           Animal a = new Dog(); // Allowed
           a.sound();
       }
   }
   ```

2. **Cannot Create constructors of type abstract** 

   Constructors in Java:
   - Have **no return type** — not even `void`.
   - Must have the **same name as the class**.
   - Are **not inherited**, and thus **cannot be overridden**.

   abstract Keyword:
   - `abstract` means: “This method has **no body** and **must be overridden** by a subclass.”
   - But **constructors can't be overridden**, so they **cannot be abstract**.

   ❌ Illegal Example:
    ```java
    abstract class A {
        abstract A(); // ❌ Invalid: constructors can't be abstract
    }
    ```

3. **Cannot Have Abstract Static Methods**
   - Static methods **belong to the class**, not objects.
   - Abstract methods are **meant to be overridden**, which conflicts with the static context.
   - Hence, **abstract static methods are not allowed**.

4. **Cannot Declare Class as Final & Abstract at the Same Time**
   - `final` class: Cannot be inherited.
   - `abstract` class: Meant to be inherited.
   - These are contradictory, so **not allowed together**.

   ❌ `final abstract class A {}` → Compilation Error

5. **Can Contain Static and Final Variables**
   - Abstract classes **can have static and final variables** just like regular classes.

   ```java
   abstract class Config {static String a = "MK"; final int VERSION = 1;}
   ``` 

---
  

## 🔹 Syntax of Abstract Class

```java
//Note: Abstract classes cannot be private
[access_modifier] abstract class ClassName {
    // fields
    // constructors
    // abstract methods (optional)
    // concrete methods
}
```

---

## 🔹 Abstract Methods

- Abstract methods do **not have a body**.
- Must be implemented by the subclass unless the subclass is also abstract.

```java
abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius = 5;
    double area() {
        return Math.PI * radius * radius;
    }
}
``` 

---


## 🔹 When to Use Abstract Classes?

- Used for implementing **Hierarchial Inheritance**, basically when you want to **provide a common base** class with **some shared code**.
- When you want to **force subclasses** to implement certain methods.

---
## 🔧 Practice Problem: Vehicle Class Hierarchy

I implemented a Java program based on the following prompt:

> **Write a Java program to create a vehicle class hierarchy.**  
> The base class should be `Vehicle`, with subclasses `Truck`, `Car`, and `Motorcycle`.  
> Each subclass should have properties like `make`, `model`, `year`, and `fuelType`.  
> Implement methods for calculating `fuelEfficiency`, `distanceTraveled`, and `maximumSpeed`.

This problem helped reinforce the concept of **inheritance** and how subclasses can extend shared behavior while adding their own specific features.

📌 I’ve shared the full solution on GitHub. You can check it out for reference and learning!


