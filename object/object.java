package object;

public class object {
    public static void main(String[] args) {
//        Object obj = "some obh";
//        System.out.println(obj);
//
//        obj = 21;
//        System.out.println(obj);
//
//        obj = new Person("Tom");
//
//        System.out.println(obj);
        Object obj = new Person("Tom");
        System.out.println(obj.hashCode());
        Object tom = new Person("Jerry");
        System.out.println(tom.hashCode());
        Object tim = new Person("Tim");
        System.out.println(tim.hashCode());
    }
}
    class Person {
        private String name;

        Person(String name) {
            this.name = name;
        }
        @Override
        public int  hashCode() {
            return 10 * name.hashCode();
        }
    }
