package generics;

import java.util.ArrayList;
import java.util.List;

public class task3 {
    public static void main(String[] args) {
        List<Dog> dogs = new ArrayList<>();
        List<Animal> animals = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        Dog rex = new Dog("Rex");

        // Демонстрируем, что метод работает с разными типами
        rex.addDogs(dogs);      // ✅ List<Dog>
        rex.addDogs(animals);   // ✅ List<Animal>
        rex.addDogs(objects);   // ✅ List<Object>

        System.out.println("Dogs list size: " + dogs.size());
        System.out.println("Animals list size: " + animals.size());
        System.out.println("Objects list size: " + objects.size());
    }
}


class Animal{
    String name;
    public Animal(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + name;
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

