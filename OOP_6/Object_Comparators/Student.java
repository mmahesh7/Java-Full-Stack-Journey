package Day_6.Object_Comparators;

public class Student implements Comparable<Student>{
    int rollno;
    float marks;

    public Student(int rollno , float marks){
        this.rollno = rollno;
        this.marks = marks;
    }

    @Override
    public String toString() {
        // return rollno+"";
        return marks+"";
    }

    @Override
    public int compareTo(Student other){
        int diff = (int)(this.marks - other.marks);
        return diff;
    }
}
