package Inheritance;
public class BoxWeight extends Box {
    int weight;

    public BoxWeight() {
        super();
        this.weight = -1;
    }

    //@Override fails here because the method is static in Box class
    static void greeting() {
        System.out.println("Hello from BoxWeight class!");
    }

    public BoxWeight(BoxWeight other) {
        super(other);
        this.weight = other.weight;
    }

    public BoxWeight(int length, int width, int height, int weight) {
        super(length, width, height);
        this.weight = weight;
    }

    public BoxWeight(int weight) {
        super();
        this.weight = weight;
    }
    public BoxWeight(int side, int weight) {
        super(side, side, side);
        this.weight = weight;
    }
}
