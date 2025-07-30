# OOP 2 - Packages, Static, Singleton Class
---

## 1. Java Packages

### What are Packages?

Packages are a way to organize classes and interfaces in a modular and manageable structure.

### Why Use Packages?

- Makes code more maintainable and organized
- Prevents class name conflicts
- Controls access with visibility modifiers

### How to Declare a Package

```java
package myPackage;

public class A {
    public static void main(String[] args) {
        System.out.println("Package A");
    }
}
```

### How to Import a Package

```java
import java.util.Scanner;
```

A Java file can have only one `package` declaration. If you don’t declare one, the class belongs to the default package.

---

## 2. Static Keyword in Java

The `static` keyword is used for memory-efficient and class-level operations.

It can be applied to:

- Variables (static fields)
- Methods
- Blocks
- Nested classes

---

### 2.1 Static Variables

These variables are shared across all instances of a class.

#### Example:

```java
class Student {
    static String college = "GNI";
    String name;

    Student(String name) {
        this.name = name;
    }

    void display() {
        System.out.println(name + " studies at " + college);
    }
}
```

Only one copy exists for all objects. Static variables can have any access modifier.

---

### 2.2 Static Methods

These methods belong to the class and do not require an object to be called.

Both static and non-static methods can access static members.But only non-static methods can access non-static members.

#### Example:

```java
class SimpleMath {
    static int square(int x) {
        return x * x;
    }

    public static void main(String[] args) {
        System.out.println(SimpleMath.square(5)); // Output: 25
    }
}
```

- Can access only other static members
- Cannot use `this` or `super`
- Not truly overridden, but can be redefined (method hiding)

---

### 2.3 Static Blocks

Executed once when the class is loaded. Used to initialize static variables.

#### Example:

```java
class Config {
    static int timeout;
    static {
        timeout = 30;
        System.out.println("Static block executed");
    }
}
```

You can have multiple static blocks. They execute in the order they appear.

---

### 2.4 Static Nested Classes

A static nested class doesn’t need an instance of the outer class.


#### Example:

```java
class Outer {
    static class Inner {
        void show() {
            System.out.println("Inside static nested class");
        }
    }

    public static void main(String[] args) {
        Outer.Inner obj = new Outer.Inner();
        obj.show();
    }
}
```

They can only access static members of the outer class.

An outer class cannot be static, but the sub-classes can be static.

You can access a static method or variable using:
* ClassName.staticMethod();
* ClassName.staticVariable;
---

### 2.5 Static vs Non-Static

| Feature             | Static             | Non-Static       |
| ------------------- | ------------------ | ---------------- |
| Belongs to          | Class              | Object           |
| Accessed via        | Class name         | Object reference |
| Memory Allocation   | Once per class     | Once per object  |
| Uses `this`/`super` | No                 | Yes              |
| Overridable         | No (method hiding) | Yes              |

Use static when data or behavior does not vary per object.

---

## 3. Singleton Class

### What is a Singleton?

A design pattern that restricts the instantiation of a class to a **single object**.

### Why Use It?

To control resource usage — useful in logging, DB connections, and configuration.

### Implementation of Singleton

```java
class Singleton {
    private static Singleton instance;

    private Singleton() {
        // private constructor
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### Usage

```java

public class Main {
    public static void main(String[] args) {
        Singleton obj = Singleton.getInstance();
    }
}

```
Only a single object is created for the class, even n-number of objects are instantiated.


---
