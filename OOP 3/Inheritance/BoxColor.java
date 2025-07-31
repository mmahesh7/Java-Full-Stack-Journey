package Inheritance;
public class BoxColor extends BoxWeight {
    String color;

    public BoxColor() {
        super();
        this.color = "undefined";
    }

    public BoxColor(BoxColor other) {
        super(other);
        this.color = other.color;
    }

    public BoxColor(int length, int width, int height, int weight, String color) {
        super(length, width, height, weight);
        this.color = color;
    }

    public BoxColor(int side, int weight, String color) {
        super(side, weight);
        this.color = color;
    }
    
}
