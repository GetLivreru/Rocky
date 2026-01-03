package clone;

// Создаем вложенный класс Address, который представляет адрес
// Реализуем интерфейс Cloneable, чтобы разрешить клонирование
class Address implements Cloneable {
    // Приватные поля для хранения улицы и номера дома
    // private - для инкапсуляции, запрещает прямой доступ извне класса
    private String street;       // Название улицы
    private int houseNumber;     // Номер дома

    // Конструктор для создания нового объекта Address
    // Принимает название улицы и номер дома
    public Address(String street, int houseNumber) {
        // Присваиваем значения, переданные в конструктор, локальным полям класса
        this.street = street;           // this указывает на поле текущего объекта
        this.houseNumber = houseNumber; // Избегаем конфликта имен параметров и полей
    }

    // Переопределяем метод clone() для создания точной копии объекта
    // throws CloneNotSupportedException - обработка возможной ошибки клонирования
    @Override
    public Address clone() throws CloneNotSupportedException {
        // Создаем полностью новый объект Address с теми же значениями
        // Это глубокое копирование - новый объект, новая ссылка
        return new Address(this.street, this.houseNumber);
    }

    // Далее идут стандартные геттеры и сеттеры для доступа к полям

    // Геттер возвращает значение улицы
    public String getStreet() {
        return street;
    }

    // Сеттер позволяет изменить значение улицы
    public void setStreet(String street) {
        this.street = street;
    }

    // Геттер возвращает номер дома
    public int getHouseNumber() {
        return houseNumber;
    }

    // Сеттер позволяет изменить номер дома
    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }
}

// Основной класс Person, который содержит адрес
// Также реализует Cloneable для возможности клонирования
class Person implements Cloneable {
    // Приватные поля для хранения информации о человеке
    private String name;     // Имя
    private int age;         // Возраст
    private Address address; // Адрес (сложный объект)

    // Конструктор для создания нового объекта Person
    public Person(String name, int age, Address address) {
        // Присваиваем значения локальным полям класса
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Переопределяем метод clone() для глубокого копирования
    @Override
    public Person clone() throws CloneNotSupportedException {
        // Проверяем адрес на null перед клонированием
        // Тернарный оператор: если адрес не null - клонируем, иначе null
        Address clonedAddress = this.address != null
                ? this.address.clone()  // Вызываем clone() для адреса
                : null;                 // Если адрес null, оставляем null

        // Создаем новый объект Person с теми же значениями
        // Важно: используем клонированный адрес, а не оригинальный
        return new Person(
                this.name,       // Копируем имя
                this.age,        // Копируем возраст
                clonedAddress    // Копируем КЛОНИРОВАННЫЙ адрес
        );
    }

    // Статический метод для демонстрации работы клонирования
    public static void demonstrateCloning() {
        try {
            // Создаем оригинальный адрес
            Address originalAddress = new Address("Ленина", 42);

            // Создаем оригинального человека с этим адресом
            Person originalPerson = new Person("Иван", 30, originalAddress);

            // Клонируем объект Person
            // Создается полностью независимая копия
            Person clonedPerson = originalPerson.clone();

            // Выводим информацию об адресах оригинала и клона
            System.out.println("Оригинальный адрес: " + originalPerson.address);
            System.out.println("Клонированный адрес: " + clonedPerson.address);

            // Изменяем адрес в оригинальном объекте
            originalAddress.setStreet("Новая улица");

            // Показываем, что клонированный объект не изменился
            System.out.println("После изменения:");
            System.out.println("Оригинальный адрес: " + originalPerson.address.getStreet());
            System.out.println("Клонированный адрес: " + clonedPerson.address.getStreet());

        } catch (CloneNotSupportedException e) {
            // Обрабатываем возможную ошибку клонирования
            // Выводим сообщение об ошибке в поток ошибок
            System.err.println("Ошибка клонирования: " + e.getMessage());
        }
    }

    // Точка входа в программу
    public static void main(String[] args) {
        // Вызываем метод демонстрации клонирования
        demonstrateCloning();
    }
}