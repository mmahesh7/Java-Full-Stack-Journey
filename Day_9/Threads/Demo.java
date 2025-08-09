package Day_9.Threads;

// class A extends Thread
// {
// 	public void run()
// 	{
// 		for(int i=1;i<=100;i++)
// 		{
// 			System.out.println("Hi");
//             try {
//                 Thread.sleep(10);
//             } catch (Exception e) {
//                 // TODO: handle exception
//                 System.out.println(e.getMessage());
//             }
// 		}
// 	}
// }

// class B extends Thread
// {
// 	public void run()
// 	{
// 		for(int i=1;i<=100;i++)
// 		{
// 			System.out.println("Hello");
//             try {
//                 Thread.sleep(10);
//             } catch (Exception e) {
//                 // TODO: handle exception
//                 System.out.println(e.getMessage());
//             }
// 		}
// 	}
// }

// class A implements Runnable
// {
// 	public void run()
// 	{
// 		for(int i=1;i<=5;i++)
// 		{
// 			System.out.println("Hi");
//             try {
//                 Thread.sleep(10);
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
// 		}
// 	}
// }

// class B implements Runnable
// {
// 	public void run()
// 	{
// 		for(int i=1;i<=5;i++)
// 		{
// 			System.out.println("Hello");
//             try {
//                 Thread.sleep(10);
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
// 		}
// 	}
// }

public class Demo {
    public static void main(String[] args) throws NumberFormatException {   
    	
    	// A obj1=new A();
    	// B obj2=new B();
    	
        // //prints the default priority value ie 5
    	// System.out.println(obj1.getPriority());

        // //set the priority value to maximum ie 10
        // obj1.setPriority(Thread.MAX_PRIORITY);

        // //prints the new priority - 10
        // System.out.println(obj1.getPriority());

    	// obj1.start();
    	// obj2.start();

        
        Runnable obj1 = new Runnable() {
            public void run() {
                for(int i=1; i<=5; i++){
                    System.out.println("Hi");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //Since, Runnable is an functional interface,
        // we can also write lambda expression as follows:
        Runnable obj2 = () -> {
            for(int i=1; i<=5; i++) {
                System.out.println("Hello");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        


        Thread t1 = new Thread(obj1);
        Thread t2 = new Thread(obj2);

        t1.start();
        t2.start();
    }
    
}