package Program;

public class Program{
    public static void main(String[] args) {
        Person person = new Person();

        person.age = 17;
        person.name = "tom";
        person.print();
        person.name = "adielt";
        person.print();
        person.age = 20;
        person.print();
        person.age = 99;
        person.print();
    
    }
}

