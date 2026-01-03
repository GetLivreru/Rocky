package clone;

public class Copy {
    public static void main(String[] args) {
        // Создаем оригинальный объект
        People original = new People(25, "Adilet");

        System.out.println("=== ДО КЛОНИРОВАНИЯ ===");
        System.out.print("Оригинал: ");
        original.display();

        // Создаем клон объекта
        People copy = original.clone();

        System.out.print("Копия: ");
        copy.display();

        // Изменяем копию
        System.out.println("\n=== ИЗМЕНЯЕМ КОПИЮ ===");
        copy.setName("Telida");
        copy.setAge(30);

        // Проверяем результаты
        System.out.println("\n=== ПОСЛЕ ИЗМЕНЕНИЯ ===");
        System.out.print("Оригинал: ");
        original.display();  // Остался неизменным!

        System.out.print("Копия: ");
        copy.display();  // Изменилась только копия!
    }
}

class People implements Cloneable {
    private int age;
    private String name;

    People(int age, String name) {
        this.age = age;
        this.name = name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setAge(int age) {
        this.age = age;
    }

    void display() {
        System.out.printf("Name: %s, Age: %d\n", name, age);
    }

    @Override
    public People clone() {
        try {
            return (People) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Ошибка клонирования", e);
        }
    }
}