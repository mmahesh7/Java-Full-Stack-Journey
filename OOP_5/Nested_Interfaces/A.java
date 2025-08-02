package Day_5.Nested_Interfaces;

public class A {
    public interface nestedInterface{
        boolean isOdd(int num);
    }
}

class B implements A.nestedInterface {
    @Override
    public boolean isOdd(int num) {
        return num % 2 != 0;
    }
}
