package Polymorphism;

public class Main {
    public static void main(String[] args) {
        Shapes shape = new Shapes();
        shape.area(); // Output: Calculating area of the shape
        
        Shapes triangle = new Triangle();
        triangle.area(); // Output: Calculating area of the triangle: 0.5*b*h

        Shapes circle = new Circle();
        circle.area(); // Output: Calculating area of the circle :pi*r*r

        Shapes square = new Square();
        square.area(); // Output: Calculating area of the square: s*s
    }
}
