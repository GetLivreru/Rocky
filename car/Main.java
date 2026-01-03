package car;

class Car {
    private String type;

    public Car(String type) {
        this.type = type;
    }
    public final String getType() {
        return type;
    }
    public void displayInfo(){
        System.out.println("Car type: " + type);
    }
}

class ElectricCar extends Car {
    private int batteryCapacity;

    public ElectricCar(String type, int batteryCapacity) {
        super(type);
        this.batteryCapacity = batteryCapacity;
    }

    /*@Override
    public String getType() {
        return "Электрический " + super.getType();
    }
    */
     public int getBatteryCapacity() {
         return batteryCapacity;
     }
     @Override
     public void displayInfo(){
         super.displayInfo();
         System.out.println("Battery Capacity: " + batteryCapacity);
     }
}
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация final метода ===\n");

        // Создаем обычный автомобиль
        Car car = new Car("Седан");
        car.displayInfo();

        System.out.println();

        // Создаем электромобиль
        ElectricCar electricCar = new ElectricCar("Кроссовер", 75);
        electricCar.displayInfo();

        System.out.println("\n=== Объяснение ===");
        System.out.println("Метод getType() объявлен как final в классе Car.");
        System.out.println("Это означает, что его НЕЛЬЗЯ переопределить в подклассах.");
        System.out.println("ElectricCar наследует этот метод, но не может его изменить.");
    }
}
