# Day 9 – Threads in Java

## 1. Introduction to Threads
A **thread** is the smallest unit of execution in a program. Java supports **multithreading**, which allows multiple threads to run concurrently, sharing the CPU time.

### Key Points:
- A **process** is an independent program in execution.
- A **thread** is a lightweight sub-process that runs within a process.
- Multiple threads within a program share the same memory but have their own execution path.

### Use Cases of Threads in Java
Threads allow programs to perform multiple tasks at the same time.

**Simple real-life examples:**
- Downloading files while watching a video  
- Listening to music while browsing the internet  
- Playing a game where the score updates while the character moves  
- Chat applications where messages send and receive at the same time  

---

## 2. Creating Threads in Java

### Method 1 – Extending the Thread Class
```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread is running...");
    }
}

public class Main {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start(); // start() calls run() internally
    }
}
```

### Method 2 – Implementing the Runnable Interface
```java
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Thread via Runnable is running...");
    }
}

public class Main {
    public static void main(String[] args) {
        Runnable r = new MyRunnable();
        Thread t = new Thread(r);
        t.start();
    }
}
```

**Why prefer Runnable over Thread?**
- Java doesn’t support multiple inheritance, so extending `Thread` limits you.
- `Runnable` allows the class to extend another class while still supporting multithreading.

---

## 3. Thread Lifecycle
A thread in Java can be in one of the following states:
1. **NEW** – Created but not yet started.
2. **RUNNABLE** – Ready to run, waiting for CPU scheduling.
3. **RUNNING** – Currently executing.
4. **WAITING** – Waiting indefinitely until notified.
5. **TIMED_WAITING** – Waiting for a specified time (e.g., `sleep()`).
6. **TERMINATED** – Completed execution or stopped.

**Key Methods:**
- `start()` → Moves thread from **NEW** to **RUNNABLE**.
- `run()` → Contains the code executed by the thread.
- `sleep(ms)` → Moves thread to **TIMED_WAITING**.
- `wait()` → Moves thread to **WAITING**.
- `notify()` / `notifyAll()` → Wakes up threads from **WAITING**.
- `stop()` → (Deprecated) Terminates a thread.

---

## 4. Thread Priority
- Priority range: **1 (MIN_PRIORITY)** to **10 (MAX_PRIORITY)**.
- Default priority: **5 (NORM_PRIORITY)**.
- Methods:
  - `setPriority(int priority)` – Suggests thread priority.
  - `getPriority()` – Returns thread priority.
- Priority only gives **hints** to the scheduler; it does not guarantee execution order.

---

## 5. Sleeping Threads
- `Thread.sleep(milliseconds)` pauses execution.
- Puts the thread into **TIMED_WAITING**.
- Throws `InterruptedException`.

Example:
```java
try {
    Thread.sleep(2000); // Sleep for 2 seconds
} catch (InterruptedException e) {
    e.printStackTrace();
}
```

---

## 6. Race Condition and Synchronization

**Race Condition:**  
Occurs when multiple threads modify a shared resource simultaneously, leading to inconsistent results.

Example without synchronization:
```java
class Counter {
    int count = 0;
    public void increment() {
        count++;
    }
}

public class RaceDemo {
    public static void main(String[] args) throws InterruptedException {
        Counter c = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) c.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) c.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Count: " + c.count);
    }
}
```

**Solution – Synchronization:**
```java
class Counter {
    int count = 0;
    public synchronized void increment() {
        count++;
    }
}
```

**join() Method:**
- Makes the current thread wait until another thread finishes.

---

## 7. Runnable vs Thread Class
| Feature | Thread Class | Runnable Interface |
|---------|--------------|--------------------|
| Inheritance | Extends `Thread` | Implements `Runnable` |
| Multiple Inheritance | Not possible | Possible |
| start() Method | Available | Not available (needs `Thread` object) |
| Best Practice | No | Yes |

---

## 8. Thread States Diagram
Below is the **Thread Lifecycle** in Java:

![Thread Lifecycle](thread_lifecycle_bw_fixed.png)

Checkout my github repo to download the image, notes and view the snippets.


