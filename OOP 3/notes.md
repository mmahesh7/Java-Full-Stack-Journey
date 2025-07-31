
# 📘 OOP 3 | Principles – Inheritance, Polymorphism, Encapsulation, Abstraction

---

## Principles of OOP
- Core concepts that underpin object-oriented design:
  1. Inheritance
  2. Polymorphism
  3. Encapsulation
  4. Abstraction

---

## Inheritance
Inheritance enables a subclass to acquire fields and methods from a superclass, promoting code reuse and logical hierarchy.

**Syntax**:
```java
class Parent {
    void greet() { System.out.println("Hello from Parent"); }
}

class Child extends Parent {
    void play() { System.out.println("Child playing"); }
}
```

---

## Example: Box

Illustrative classes demonstrating inheritance and constructor chaining.

```java
class Box {
    private double width, height, depth;

    Box() {
        width = height = depth = 0;
    }

    Box(double w, double h, double d) {
        width = w;
        height = h;
        depth = d;
    }
}

class BoxWeight extends Box {
    double weight;

    BoxWeight() {
        super();
        weight = 0;
    }

    BoxWeight(double w, double h, double d, double m) {
        super(w, h, d);
        weight = m;
    }
}
```

--- -->

## Explanation of Box Example

* `Box` defines three dimensions: width, height, and depth.
* `BoxWeight` extends `Box` by adding a `weight` attribute.
* The `super(...)` call invokes the `Box` constructor to initialize inherited fields.

---

## "private" Keyword

The `private` modifier restricts visibility of class members to within the defining class, ensuring data hiding and integrity.

**Example**:

```java
class Student {
    private int age;

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    public int getAge() {
        return age;
    }
}
```

---

## "super" Keyword

The `super` keyword invokes a superclass constructor or accesses superclass members. When calling a superclass constructor, `super(...)` must appear as the first statement in the subclass constructor.

**Example**:

```java
public class Cat extends Animal {
    private String color;

    public Cat(boolean isCarnivore, int legs, String color) {
        super(isCarnivore, legs);  // initializes fields in Animal
        this.color = color;        // subclass-specific initialization
    }

    public String getColor() {
        return color;
    }
}
```

---

## Single Inheritance

A subclass extends one immediate superclass.

**Example**:

```java
class Child extends Parent {
    // inherits Parent’s members
}
```

---

## Multiple Inheritance

Java does not support multiple class inheritance due to ambiguity issues. Multiple inheritance of behavior is achieved via interfaces.

---

## Hierarchical Inheritance

Multiple subclasses extend a single superclass.

**Example**:

```java
class Cat extends Animal { }
class Dog extends Animal { }
```

---

## Hybrid Inheritance

A combination of two or more inheritance types is implemented in Java using interfaces and class hierarchies, avoiding direct multiple class inheritance.

---

## Polymorphism

Polymorphism allows objects to be treated as instances of their superclass, providing flexibility through method overloading and overriding.

---

## Example: Shapes

```java
class Shape {
    void draw() {
        System.out.println("Drawing shape");
    }
}

class Circle extends Shape {
    @Override
    void draw() {
        System.out.println("Drawing circle");
    }
}

Shape s = new Circle();
s.draw();  // Outputs: Drawing circle
```

---

## Types of Polymorphism

1. **Static (Compile-time) Polymorphism** – achieved via method overloading.
2. **Dynamic (Runtime) Polymorphism** – achieved via method overriding.

---

## Static Polymorphism (Method Overloading)

Overloaded methods share the same name but differ in parameter types or counts. The compiler resolves the appropriate method at compile time.

**Example**:

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }
}
```

---

## Dynamic Polymorphism (Method Overriding)

A subclass provides a specific implementation of a method defined in its superclass. The JVM resolves the call at runtime based on the actual object type.

**Example**:

```java
class Animal {
    void sound() {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Bark");
    }
}

Animal a = new Dog();
a.sound();  // Outputs: Bark
```

---

## How Method Overriding Works

* The subclass method replaces the superclass method for instances of the subclass.
* The JVM performs dynamic method dispatch to invoke the correct implementation based on the object’s runtime type.

---

## "final" Keyword

* **`final` method**: cannot be overridden by subclasses.
* **`final` class**: cannot be subclassed.

---

## Overriding Static Methods

Static methods are bound at compile time and are not subject to runtime polymorphism. Therefore, they cannot be overridden.

---

## Encapsulation

Encapsulation binds data and related methods within a class, restricting external access and enforcing controlled manipulation.

**Example**:

```java
class Account {
    private double balance;

    public void deposit(double amt) {
        if (amt > 0) {
            balance += amt;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```

---

## Abstraction

Abstraction exposes essential functionality while concealing implementation details, typically via abstract classes and interfaces.

**Example**:

```java
interface Vehicle {
    void start();
}

class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Engine started");
    }
}
```

---

## Encapsulation vs. Abstraction

| Aspect    | Encapsulation                       | Abstraction                      |
| --------- | ----------------------------------- | -------------------------------- |
| Purpose   | Protect data and enforce validation | Expose necessary operations      |
| Mechanism | `private` fields + getters/setters  | Interfaces and abstract classes  |
| Level     | Implementation-level                | Design-level                     |
| Example   | Class with private attributes       | Interface with method signatures |

---


