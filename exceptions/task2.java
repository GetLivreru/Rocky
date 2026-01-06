package exceptions;

import java.util.Scanner;

public class task2 {
    public static void main(String[] args) throws Throwable {

        //        Scanner in = new Scanner(System.in);
        //        System.out.print("Введите имя: ");
        //        String name = in.nextLine(); // считываем введенную строку
        //
        //        try{
        //
        //            // если строка имеет имеет меньше 2-х символов, генерируем исключение
        //            if(name.length() < 2) throw new Throwable("Hz bro pashol nahoi");
        //            System.out.println("Привет, " + name);
        //        }
        //        catch(Throwable ex){
        //            System.out.println("Возникла ошибка: " + ex.getMessage());
        //        }
        //    }
        try {
            var Tom = new Person("Tom", -20);
            Tom.print();
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
class Person {
    private String name;
    private int age;

    Person(String name,int age){
        if (age < 1 || age > 110) throw new IllegalArgumentException("Wrong age bro: " + age );
        this.name = name;
        this.age = age;
    }
    void print(){
        System.out.printf("Name: %s; Age: %d\n",name,age);
    }
}
