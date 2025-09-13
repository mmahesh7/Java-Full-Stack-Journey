package Day_7.Object_Cloning;
import java.util.*;
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Human mk = new Human(20, "Mahesh");
        // Human mkClone = new Human(mk);

        Human mkClone = (Human) mk.clone();
        //This is for shallow copy
        // System.out.println(mkClone.age + " " + mkClone.name);// 20 Mahesh
        // System.out.println(Arrays.toString(mkClone.arr));
        // System.out.println(Arrays.toString(mk.arr));
        // mkClone.arr[0] = 100;
        // System.out.println(Arrays.toString(mkClone.arr));
        // System.out.println(Arrays.toString(mk.arr)); 
        //Ouptput: 
        // [1, 2, 3, 4, 5]
        // [1, 2, 3, 4, 5]
        // [100, 2, 3, 4, 5]
        // [100, 2, 3, 4, 5]
        // On updating the cloned object's array,
        // the original object's array also being updated.
        //This is called **Shallow copy**.


        //This is for deep copy
        System.out.println(mkClone.age + " " + mkClone.name);// 20 Mahesh
        System.out.println(Arrays.toString(mkClone.arr)); 
        System.out.println(Arrays.toString(mk.arr)); 
        mkClone.arr[0] = 100;
        System.out.println(Arrays.toString(mkClone.arr));
        System.out.println(Arrays.toString(mk.arr)); 
        //Output:
        // [1, 2, 3, 4, 5]
        // [1, 2, 3, 4, 5]
        // [100, 2, 3, 4, 5]
        // [1, 2, 3, 4, 5]
    }
}
