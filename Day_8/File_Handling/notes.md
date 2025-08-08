
# Day 8: File Handling in Java

Today, I learned about file handling concepts in Java, including streams, key classes, resource management, and file operations.

---

## 1. IOException

- Represents unexpected problems during I/O operations such as file not found, access denied, or corrupted files.  
- Must be handled explicitly using try-catch or declared with `throws`.  
- IOException is a checked exception, ensuring the programmer handles these errors.

---

## 2. Streams in Java

- Abstractions for sequences of data flowing in or out, either bytes or characters.  
- Linked to physical devices (files, keyboard, network).  
- Java I/O is built on stream classes defined in `java.io`.

### Byte Streams

- Handle 8-bit binary data; useful for reading/writing images, videos, etc.  
- Use abstract classes `InputStream` and `OutputStream`.  
- Common subclasses: `FileInputStream`, `FileOutputStream`.

### Character Streams

- Handle 16-bit Unicode characters; useful for text data.  
- Use abstract classes `Reader` and `Writer`.  
- Common subclasses: `FileReader`, `FileWriter`.

---

## 3. Converting Between Byte and Character Streams

- `InputStreamReader` converts byte streams to character streams.  
- `OutputStreamWriter` converts character streams to byte streams.  
- Helps handle encoding and decoding of characters (important for internationalization).

---

## 4. Try-With-Resources

- Introduced in Java 7 to ensure automatic closing of resources implementing `AutoCloseable`.  
- Prevents resource leaks and reduces boilerplate finally blocks.

Example:

```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // use br
} // br closed automatically
```

---

## 5. Reading Files

### Using FileReader

- Reads characters one by one from files.  
- Useful for small files or simple reading.

```java
try (FileReader fr = new FileReader("note.txt")) {
    int ch = fr.read();
    while (fr.ready()) {
        System.out.print((char) ch);
        ch = fr.read();
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

### Using BufferedReader

- Buffers input for efficiency.  
- Adds `readLine()` method for line-wise reading.  
- Recommended for reading large text files.

```java
try (BufferedReader br = new BufferedReader(new FileReader("note.txt"))) {
    while (br.ready()) {
        System.out.println(br.readLine());
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 6. Writing Files

### Using FileWriter

- Writes characters to files.  
- Can overwrite or append based on constructor parameters.

```java
try (FileWriter fw = new FileWriter("note.txt", true)) {
    fw.write(" new text");
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

### Using BufferedWriter

- Buffers output for performance.  
- Adds `newLine()` method to write platform-dependent newlines cleanly.

```java
try (BufferedWriter bw = new BufferedWriter(new FileWriter("note.txt", true))) {
    bw.write(" Appended using bufferedWriter");
    System.out.println("Successfully appended");
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 7. File Class

- Represents file and directory pathnames abstractly.  
- Avoids platform-dependent path issues (Windows `\` vs Linux `/`).  
- Methods include file creation, deletion, checking existence, file properties, and more.

### Creating a New File

```java
try {
    File fo = new File("new_file.txt");
    fo.createNewFile();
    System.out.println("File created");
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

### Deleting a File

```java
try {
    File file = new File("random_file.txt");
    file.createNewFile(); // create file first
    if (file.delete()) {
        System.out.println("\nDeleted file: " + file.getName());
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 8. InputStreamReader Example (From Keyboard)

```java
try (InputStreamReader isr = new InputStreamReader(System.in)) {
    System.out.print("Enter some characters: ");
    int ch = isr.read();
    while (isr.ready()) {
        System.out.println("Read character (int): " + ch);
        ch = isr.read();
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 9. BufferedReader with InputStreamReader (Reading Keyboard Input)

```java
try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
    System.out.print("Enter some text: ");
    System.out.println("You entered: " + br.readLine());
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 10. OutputStream Example (Writes Bytes)

```java
OutputStream os = System.out;
os.write('ᛞ'); // May print garbage due to Unicode beyond 8-bit range
```

---

## 11. OutputStreamWriter Example (Writes Characters)

```java
try (OutputStreamWriter osw = new OutputStreamWriter(System.out)) {
    osw.write('A');        // prints 'A'
    osw.write(97);         // prints 'a'
    osw.write(10);         // newline
    osw.write("\n");       // newline
    char[] arr = "This is an output stream reader".toCharArray();
    osw.write(arr);        // prints the string
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

## 12. Full File Handling Workflow Example

```java
// Creating a file
try {
    File fo = new File("new_file.txt");
    fo.createNewFile();
    System.out.println("File created");
} catch (IOException e) {
    System.out.println(e.getMessage());
}

// Writing to the file
try (FileWriter fw = new FileWriter("new_file.txt", true)) {
    fw.write("Hare Krishna \n");
} catch (IOException e) {
    System.out.println(e.getMessage());
}

// Reading from the file
try (FileReader fr = new FileReader("new_file.txt")) {
    int ch = fr.read();
    while (fr.ready()) {
        System.out.print((char) ch);
        ch = fr.read();
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}

// Deleting a file
try {
    File file = new File("random_file.txt");
    file.createNewFile(); // create file first
    if (file.delete()) {
        System.out.println("\nDeleted file: " + file.getName());
    }
} catch (IOException e) {
    System.out.println(e.getMessage());
}
```

---

### Output :

```
File created
Hare Krishna 
random_file.txt
```
