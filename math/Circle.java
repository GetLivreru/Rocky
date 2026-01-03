package math;

public class Circle{
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }
    public double calculateArea() {
        return Math.PI * Math.pow(radius, 2);
    }
}

