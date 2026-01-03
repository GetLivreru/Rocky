package object;

public class Equals {
    public static void main(String[] args) {

        Point p = new Point(1,4);
        Point b = p;
        Point c = new Point(1,2);
        Point p1 = new Point(1,4);

//        System.out.println(p1.equals(p1));
//        System.out.println(p.equals(p1));
    }
}
class Point {
    private double x;
    private double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    void print(){
        System.out.println(x+","+y);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }
}