
public class OverloadingExample {
    // Перегруженные методы sum
    public int sum(int a, int b) {
        return a + b;
    }

    public double sum(double a, double b) {
        return a + b;
    }

    public int sum(int a, int b, int c) {
        return a + b + c;
    }

    // Пример использования
    public void example() {
        System.out.println(sum(5, 3));        // вызовет первый метод
        System.out.println(sum(5.5, 3.2));    // вызовет второй метод
        System.out.println(sum(5, 3, 4));     // вызовет третий метод
    }
}