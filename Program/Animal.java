package Program;

public class Animal{
    int speed;
    String name;
    int legs;
    void printName(){
        System.out.printf("Name: %s \tAge: %d\n", name, speed);
    }
    void printSpeed(){
        System.out.printf("Name: %s \tAge: %d\n", name, speed);
    }
    void print(){
        printName();
        printSpeed();
    }
}
