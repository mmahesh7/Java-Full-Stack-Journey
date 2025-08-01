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
