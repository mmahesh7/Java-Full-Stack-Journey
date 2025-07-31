package Inheritance;
public class Main {
    public static void main(String[] args) {
        // Box box = new Box();
        // System.out.println("Box created with default dimensions: " + box.length + "x" + box.width + "x" + box.height);

        // BoxWeight boxWeight = new BoxWeight(10, 20, 30, 5);
        // System.out.println("BoxWeight created with dimensions: " + boxWeight.length + "x" + boxWeight.width + "x" + boxWeight.height + " and weight: " + boxWeight.weight);

        //Here the reference type is Box, but the object is of type BoxWeight. 
        // So we cannot access weight directly i.e we cannot directly access the members of object class when ref class is different.
        // Box obj = new BoxWeight(1,2,3,4);
        // System.out.println("Box created with dimensions: " + obj.length + "x" + obj.width + "x" + obj.height);//This will work because length, width, and height are accessible from Box class.
        // System.out.println("weight:" + obj.weight); //This will not work because weight is not defined in Box class, it is defined in BoxWeight class.

        // To access weight, we need to cast the object to BoxWeight
        // BoxWeight boxWeight = (BoxWeight) obj;
        // System.out.println("weight: " + boxWeight.weight); // Now this works because we cast obj to BoxWeight
         
        //This will not work because Box class does not have a weight member. So when ref class is child classn and object type is of parent, 
        // we cannot instantiate the member of child class with parent class constructor. So it is throwing an error.
        // BoxWeight obj2 = new Box(1, 2, 3);
        // System.out.println("BoxWeight created with dimensions: " + obj2.length + "x" + obj.width + "x" + obj.height);
        // System.out.println("weight: " + obj2.weight); 

        // BoxPrice box = new BoxPrice();
        // System.out.println(box.length + " x " + box.width + "x " + box.height + " weight: " + box.weight + " price: " + box.price);
        //Output: -1 x -1 x -1 weight: -1 price: -1
        
        // BoxPrice box = new BoxPrice(10, 25,30);
        // System.out.println(box.length + " x " + box.width + "x " + box.height + " weight: " + box.weight + " price: " + box.price);
        //Output: 10 x 10x 10 weight: 25 price: 30

        //If greeting method is non-static, we need to create an instance of class.
        // Box box = new BoxWeight();
        // Box.greeting();

        // Note static methods are not overridden in Java, but can be inherited.

        //Dynamic polymorphism(overriding) 
        //If the greeting method is non-static , it will call the method from the actual object type.
        //For below call, greeting method is static in Box class, so it will call the method from Box class.
        Box.greeting(); // This will call the greeting method from Box class because the method is static in Box class.

    }
}
