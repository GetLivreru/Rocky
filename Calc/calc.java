package Calc;

public class calc{
    public static void main(String[] args) {
        int n = 55;
        Calculator calculator = new Calculator();
        //calculator.sum("hello",5,1,3,4,5,5);
        //calculator.sum("faun", -8);
        //calculator.minus(1,2,3);
        //calculator.sum("Sum of {1, 2, 3}: ", 1, 2, 3);    // Sum of {1, 2, 3}: 6
        //calculator.sum("Zero sum: ");                     // Zero sum: 0
        calculator.twice(n);
    }

}

class Calculator{
    void sum(String message, int ...nums){
         
        System.out.print(message); 
        int result =0;
        for(int n: nums)
            result += n;
        System.out.println(result);
    }
    void minus(int ...nums){
        int result = 0;
        for(int n:nums)
            result -= n;
        System.err.println(result);
    }
    void twice(int n){
        n = n + n;

        System.err.printf("n in twice: %d\n", n);
    }
}