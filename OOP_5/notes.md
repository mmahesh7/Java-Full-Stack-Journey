
# 📘 OOP 5 - Interfaces and its Types
---

## 📌 Topics Covered

- What are interfaces in Java?
- Differences between abstract classes and interfaces
- Static and default methods in interfaces (Java 8+)
- Multiple interface implementation and method conflict handling
- Interface composition using `NiceCar` and `Engine`/`Media`
- Nested interfaces and their purpose

---

## 🧠 Interface Fundamentals

### ➤ Interface Basics

- Interfaces are **contracts**: they define *what* a class must do, not *how*.
- All interface methods are **implicitly `public` and `abstract`**, unless marked `default`, `static`, or `private` (Java 8+).
- Variables in interfaces are always **public static final** constants.

```java
interface Flyable {
    int MAX_ALTITUDE = 50000; // constant
    void fly();               // abstract method
}
```

### ➤ Interface vs Abstract Class

| Feature               | Interface                         | Abstract Class                 |
|----------------------|-----------------------------------|--------------------------------|
| Inheritance          | Multiple (via `implements`)       | Single (`extends` only)        |
| Method Types         | Abstract, default, static         | Abstract and concrete          |
| Constructors         | ❌ Not allowed                    | ✅ Allowed                     |
| Variables            | static final only                 | Any type (instance allowed)    |
| Access Modifiers     | Methods are public only           | Can use private/protected      |

---

## 🔁 Interface Implementation & Multiple Inheritance

```java
interface A {
    static void greet() {
        System.out.println("Static Hello from A");
    }

    default void fun() {
        System.out.println("Default implementation of fun in A");
    }
}
```

```java
interface B {
    void greet();
    // Optional default method
    // default void fun() { ... }
}
```

```java
public class Main implements A, B {
    @Override
    public void greet() {
        System.out.println("Greeting from Main class");
    }

    public static void main(String[] args) {
        Main main = new Main();
        A.greet(); // Static Hello from A
        main.greet(); // Greeting from Main class
        main.fun();   // Default implementation of fun in A
    }
}
```

🟢 **Note:** Static methods are **not inherited**. Use `InterfaceName.method()` to call.

---

## ⚙️ Interface Composition with Real-World Example

### Engine Interface

```java
public interface Engine {
    static final int PRICE = 50000;

    void start();
    void stop();
    void accelerate();
}
```

### PowerEngine & ElectricEngine Implementations

```java
public class PowerEngine implements Engine {
    public void start() { System.out.println("Power Engine started"); }
    public void stop() { System.out.println("Power Engine stopped"); }
    public void accelerate() { System.out.println("Power Engine accelerating"); }
}
```

```java
public class ElectricEngine implements Engine {
    public void start() { System.out.println("Electric Engine started"); }
    public void stop() { System.out.println("Electric Engine stopped"); }
    public void accelerate() { System.out.println("Electric Engine accelerating"); }
}
```

---

### Media Interface + CDPlayer Implementation

```java
public interface Media {
    void start();
    void stop();
}
```

```java
public class CDPlayer implements Media {
    public void start() { System.out.println("CD Player started"); }
    public void stop() { System.out.println("CD Player stopped"); }
}
```

---

### Composition Pattern – `NiceCar` Class

```java
public class NiceCar {
    private Engine engine;
    private Media player = new CDPlayer();

    public NiceCar() {
        this.engine = new PowerEngine(); // Default engine
    }

    public NiceCar(Engine engine) {
        this.engine = engine; // Dependency injection
    }

    public void start() {
        engine.start();
    }

    public void stop() {
        engine.stop();
    }

    public void startMusic() {
        player.start();
    }

    public void stopMusic() {
        player.stop();
    }

    public void upgradeEngine() {
        this.engine = new ElectricEngine(); // Switch engine at runtime
    }
}
```

---
### Implementation of Composition - Main Class
```java
public class Main {
    public static void main(String[] args) {
        Car car =  new Car();
        
        // car.start();//Car engine started
        // car.stop();//Car engine stopped
        // car.accelerate();//Car is accelerating

        // Media carMedia = new Car();
        // carMedia.stop(); //Output : Car engine stopped
        // But we expect the music to stop, not the engine.

        NiceCar niceCar = new NiceCar();

        niceCar.start();//Power Engine started
        niceCar.startMusic();//CD Player started
        niceCar.upgradeEngine();//Upgrading to Electric Engine
        niceCar.start();//Electric Engine started
    }                      
}
```
---

##  Error Fixes: Method Name Conflict

###  Problem:

```java
class Car implements Engine, Media {
    public void start() {
        System.out.println("Car started");
    }

    public void stop() {
        System.out.println("Car stopped");
    }
}
```

**Issue:** One `start()` method can't clearly define if it's starting the engine or media.

###  Fix: Use Composition Instead

```java
public class NiceCar {
    private Engine engine;
    private Media player;

    public void start() {
        engine.start(); // Handles engine logic
    }

    public void startMusic() {
        player.start(); // Handles music system
    }
}
```

---

## 🔄 Nested Interfaces

```java
class A {
    public interface nestedInterface {
        boolean isOdd(int num);
    }
}

class B implements A.nestedInterface {
    public boolean isOdd(int num) {
        return num % 2 != 0;
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        B obj = new B();
        System.out.println(obj.isOdd(3)); // true
    }
}
```

Use cases:
- Keep related logic grouped
- Hide interfaces from outside access (encapsulation)

---

## ✅ Key Takeaways

- Java interfaces support **multiple inheritance**.
- Use `default` methods to provide optional implementations.
- Call `static` interface methods via the interface name.
- Prefer **composition over inheritance** to avoid method name clashes.
- Interfaces define **what** needs to be done, not **how**.

---




**🔗 Code snippets and full working examples are available in my GitHub repo.**

