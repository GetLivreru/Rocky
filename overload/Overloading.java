package overload;

public class Overloading {

    public static void main(String[] args) {
        Overloading overload = new Overloading();
        overload.example();
    }

    public int sum(int a, int b){
        return a+b;
    }
    public double sum(double a, double b){
        return a*b;
    }
    public int sum(int a, int b,int c){
        return a+b+c;
    }
    public void example(){
        System.out.println(sum(1,5));
        System.out.println(sum(3.8,2.2));
        System.out.println(sum(1,2,3));
    }


}
