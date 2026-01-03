package object;

import java.util.Objects;

public class MathObject {

    public static void main(String[] args) {
        Object tom = new People(12,"Adil");
        System.out.println(tom.hashCode());
    }
}

class People {
    private  int age;
    private  String name;

    People(int age, String name){
        this.age = age;
        this.name = name;
    }
    @Override
    public int hashCode(){
        return 10 * Objects.hash(age, name);
    }
}
