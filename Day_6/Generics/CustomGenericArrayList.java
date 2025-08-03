package Day_6.Generics;

public class CustomGenericArrayList<T> {

    private Object[] data;
    private static int DEFAULT_SIZE = 10;
    private int size = 0;

    public CustomGenericArrayList() {
        this.data = new Object[DEFAULT_SIZE];
    }

    public void add(T element){
        if(isFull()){ 
            resize();
        }
        data[size++] = element;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public void resize(){
        Object temp[] = new Object[data.length * 2];
        for(int i=0; i< data.length; i++){
            temp[i] = data[i];
        }
        data = temp;
    }    

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        T removed = (T)(data[index]);
        for(int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;     
        return removed;   
    }

    public void set(int index, T value) {
        data[index] = value;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T)(data[index]);
    }

    @Override
    public String toString() {
        return "CustomGenericArrayList{" +
                "data=" + java.util.Arrays.toString(data) +
                ", size=" + size +
                '}';
    }
    public static void main(String[] args) {
        CustomGenericArrayList<Integer> list = new CustomGenericArrayList<>();
        
        // list.add("mmk");// Error, Sice list is of type Integer

        // list.add(3);
        // list.add(5);
        // list.add(9);
        // list.add(1);
        // System.out.println(list);
        // list.remove(2);
        // System.out.println(list);

        for(int i = 0; i < 13; i++) {
            list.add(i * 2);
        }
        System.out.println(list);
    }
}
