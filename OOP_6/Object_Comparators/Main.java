// Topics covered:
// - Comparable objects using Comparable interface
// - sorting objects using comparators
// - Lamda Expressions

package Day_6.Object_Comparators;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Student Anuvind = new Student(33, 96.55f);
        Student koushik =  new Student(36, 85.12f);
        Student Pavan = new Student(44, 89.97f);
        Student Srikanth = new Student(30, 90.55f);
        Student Praneeth = new Student(50, 196.55f);

        Student[] list = {Anuvind, koushik, Pavan, Srikanth, Praneeth};

        System.out.println(Arrays.toString(list));
        // Sorting the list of students based on their marks using Comparable interface
        //Because we have overriden the compareTo method in Student class based on marks.
        // Arrays.sort(list);

        //We can also sort the list of Students based on rollno by adding new Comparator to the sort method.
        // Arrays.sort(list, new Comparator<Student>(){
        //     public int compare(Student s1, Student s2){
        //         // return s1.rollno - s2.rollno; // Compare based on rollno in ascending order
        //         return -(s1.rollno - s2.rollno);// Descending order based on rollno
        //     }
        // });

        // Instead of using an seperate class for Comparator, we can use a Lambda Expression.
        // bY using Lambda Expression we can write the above sort method in a single line as follows:
        // Arrays.sort(list, (s1, s2) -> (s1.rollno - s2.rollno)); // Ascending order based on rollno
        Arrays.sort(list, (s1, s2) -> (s2.rollno - s1.rollno)); // Descending order based on rollno
        System.out.println(Arrays.toString(list));

        // if(koushik > Anuvind) {
        //     System.out.println("Koushik has scored more marks than Anuvind.");
        // }
        // Throws an error because java does not know what instance of Student is being compared.
        //Whether it is rollno or marks.
            
        //So inorder to overcome this ambiguity and compare two Student objects.
        //We need to implement Comparable interface and override the compareTo method.
        //So, We have created an custom  interface called GenericInterface that implements Comparable
        //Now our class Student implements Comparable interface of type Student.
        // Override the compareTo() method of Comparable interface.
        // Now we can compare two Student objects based on marks.

        // Example of using Comparable interface on Student class : 
        if(koushik.compareTo(Anuvind) > 0) {
            System.out.println("Koushik has scored more marks than Anuvind.");
        }
        else if(koushik.compareTo(Anuvind) < 0) {
            System.out.println("Anuvind has scored more marks than Koushik.");
        } else {
            System.out.println("Both have scored equal marks.");
        }
    }
}
