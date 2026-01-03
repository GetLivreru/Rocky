package Stats;

public class StaticExample {
    public static void main(String[] args) {
    People tom = new People(41,"TOM");
        tom.checkAge();
    }
}

class People{
    int age;
    String name;
    static int requirementAge;

    static {
        requirementAge = 100;
        System.out.println("static init");
    }


    People (int age, String name){
        this.age = age;
        this.name = name;
    }
    void checkAge(){
        if (age >= requirementAge){
            System.out.println("You are too late");
        }
        else {
            System.out.println("You are too Young");
        }
    }
}