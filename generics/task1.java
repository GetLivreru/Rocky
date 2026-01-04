package generics;

public class task1 {
    public static void main(String[] args) {
        Box<Integer> i = new Box<>();
        Box<String> s = new Box<>();
        i.setValue(10);
        s.setValue("Hello");
        System.out.println(i.getValue());
        System.out.println(s.getValue());
    }
}

class Box<T>{
    private T value;

    public T getValue(){
        return value;
    }
    public void setValue(T value){
        this.value = value;
    }
}