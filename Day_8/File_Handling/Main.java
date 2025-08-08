package Day_8.File_Handling;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

public class Main{
    public static void main(String[] args) {
        // InputStreamReader Example
        // InputStreamReader is a bridge from byte streams to character streams.
        // It reads bytes and decodes them into characters using a specified charset.
        // try(InputStreamReader isr = new InputStreamReader(System.in)) {
        //     // Read from the input stream
        //     System.out.print("Enter some characters: ");
        //     int letters = isr.read();
        //     while(isr.ready()) {
        //         System.out.println("Read character: " +  letters);
        //         letters = isr.read();
        //     }
        //     // isr.close();
        //     System.out.println();
        // }catch(IOException e){
        //     System.out.println(e.getMessage());
        // }



        //FileReader Example
        // try(FileReader fr = new FileReader("note.txt")){
        //     int letters = fr.read();//read() returns the next character as an integer. So the type of letters is int.
        //     while(fr.ready()){
        //         System.out.print((char)letters);
        //         letters = fr.read();
        //     }
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }



        //BufferedReader Example (Using InputStreamReader)
        //BufferedReader is used to read text from a character-input stream.
        //But the input stream we enter is a byte input stream(from keyboard).
        //So we need to use InputStreamReader to convert byte stream to character stream.
        // try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
        //     System.out.print("Enter some text: ");
        //     System.out.println("You entered: " + br.readLine());// readLine() reads a line of text.
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }



        //BufferedReader Example (Using FileReader)
        // try (BufferedReader br = new BufferedReader(new FileReader("note.txt"))) {
        //     while (br.ready()) {
        //         System.out.println(br.readLine());
        //     }
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }



        //OutputStream Examples
        // OutputStream os = System.out;
        // os.write("😅");//Range is exceeded, so throws an error

        // try (OutputStreamWriter osw = new OutputStreamWriter(System.out)) {
        //     osw.write('A');//A
        //     osw.write(97);//a
        //     osw.write(10);//prints new line
        //     osw.write("\n");//prints new line
        //     char[] arr = "This is an output stream reader".toCharArray();
        //     osw.write(arr);//This is an output stream reader
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }



        //FileWriter
        //This is the default filewrite constructor which over-writes the current note.txt
        // try (FileWriter fw = new FileWriter("note.txt")) {

        //By adding a boolean parameter(set true) to the constructor we can append text to the note.txt without overriding it
        // try (FileWriter fw = new FileWriter("C:\\Users\\mmahe\\OneDrive\\Desktop\\LinkedIn Posts\\Day_8\\File_Handling\\note.txt", true)) {
        //     fw.write(" new text");// note.txt will contain the following text
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }



        //BufferedWriter Example
        // try (BufferedWriter bw = new BufferedWriter(new FileWriter("note.txt", true))) {
        //     bw.write(" Appended using bufferedWriter");
        //     System.out.println("Successfully appended");;
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }


        //Creating a new file - new_file.txt
        try {
            File fo = new File("new_file.txt");//new file instance
            fo.createNewFile();
            System.out.println("File created");

        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        //writing to the file
        try (FileWriter fw = new FileWriter("new_file.txt", true)) {
            fw.write("Hare Krishna \n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //Reading the file
        try (FileReader fr = new FileReader("new_file.txt")) {
            int letters = fr.read();
            while(fr.ready()){
                System.out.print((char)letters);
                letters = fr.read();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //Deleting a file
        try {
            File file = new File("random_file.txt");
            file.createNewFile();//random file created
            // file.delete();//random file deleted
            if(file.delete()){
                System.out.println("\n"+file.getName());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
    }
}