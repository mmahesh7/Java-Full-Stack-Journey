package Day_7.Enums;

import java.lang.reflect.Constructor;

public class Basic {
    enum Week implements A {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
        //these are constants of the enum
        //These are public static and final by default
        //Sice it is final we cannot create child enums
        // Type is Week

        //Enum constructor is called for all the enum constants
        Week() {
            System.out.println("Constructor called for " + this);
        }
        //Enum constructors private or default, but not public or protected
        //Why? we dont want to create new objects of enum.

        //When a constructor is called, it will look like as follows:
        //Internally : public static final Week Monday = new Week();

        @Override
        public void Hello() {
            System.out.println("Hello from " + this);
        }
    }
    public static void main(String[] args) {
        Week week ;
        week = Week.Monday;
        week.Hello(); // Hello from Monday
        // for(Week day : Week.values()) {
        //     System.out.println(day);
        // }

        System.out.println(week);// Monday

        // .ordinal returns the index of the enum constant
        System.out.println(week.ordinal());// 0

        //Output : 
        // Constructor called for Monday
        // Constructor called for Tuesday
        // Constructor called for Wednesday
        // Constructor called for Thursday
        // Constructor called for Friday
        // Constructor called for Saturday
        // Constructor called for Sunday
        // Hello from Monday
        // Monday
        // 0
        
    }
}
