package Day_7.Object_Cloning;

public class Human implements Cloneable{
    int age;
    String name;
    int[] arr;

    public Human(int age, String name) {
        this.age = age;
        this.name = name;
        this.arr = new int[]{1,2,3,4,5};
    }
    // public Human(Human otherHuman) {
    //     this.age = otherHuman.age;
    //     this.name = otherHuman.name;
    // }

    // @Override
    // public Object clone() throws CloneNotSupportedException {
    //     //this is a shallow copy
    //     return super.clone();
    // }

    @Override
    public Object clone() throws CloneNotSupportedException {
        //this is a deep copy
        Human cloned = (Human) super.clone();//this is actually a shallow copy

        // Now we need to clone the array to make it a deep copy
        cloned.arr = new int[cloned.arr.length];
        for (int i = 0; i < cloned.arr.length; i++) {
            cloned.arr[i] = this.arr[i]; // Copy each element
        }
        return cloned; // Return the deep copied object
    }
}
