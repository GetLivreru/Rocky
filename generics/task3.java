package generics;

import java.util.List;

public class task3 {
    public static void main(String[] args) {
        Dog d = new Dog("Rex");
        System.out.println(d);
    }
}


class Animal{
    String name;
    public Animal(String name){
        this.name=name;
    }
}

class Dog extends Animal{
    Dog(String name){
        super(name);
    }
    void addDogs(List<? super Dog> animals){
        animals.add(new Dog(this.name));
    }
}

