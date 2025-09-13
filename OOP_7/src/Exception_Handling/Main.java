

public class Main {
    public static void main(String[] args) {
        int a = 5;
        int b = 0;
        // int c = a/b; // This will cause an ArithmeticException

        //We can handle the above arithmetic exception using try-catch block
        // try{
        //     // int c = a/b; // ArithmeticException 
        //     divide(a, b);
        // } catch (Exception e) {
        //     System.out.println(e.getMessage());// / by zero
        // } finally {
        //     System.out.println("This will always execute");
        // }

        try {
            String name = "mmk";
            if (name.equals("mmk")) {
                throw new MyExceptions("Name is mmk");
            }
        } catch(MyExceptions e) {
            System.out.println(e.getMessage()); // Name is mmk
        } finally{
            System.out.println("This will always execute");
        }
    }
    //Method to explicitly throw an ArithmeticException
    static int divide(int a, int b) throws ArithmeticException {
        if(b == 0) {
            throw new ArithmeticException("Please do not divide by zero");
        }
        return a / b; 
    }
}