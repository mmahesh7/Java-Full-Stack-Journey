package Inheritance;
class Box {
    int length;
    int width;
    int height;

    public Box() {
        this.length = -1;
        this.width = -1;
        this.height = -1;
    }
    
    static void greeting(){
        System.out.println("Hello from Box class!");
    }
    public Box(Box other) {
        this.length = other.length;
        this.width = other.width;
        this.height = other.height;
    }

    public Box(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

}