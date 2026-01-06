package exceptions;

public class task1 {
    public static void main(String[] args) {

//        divide(10, 0);
//        System.out.println("Конец программы");
//        int result = parse("5");
//        System.out.println(result);
//        int solution = parse("t");
//        System.out.println(solution);
        printNumber(0);     // По индексу 0 число 1
        printNumber(10);    // Выход за пределы массива
        printNumber(3);     // For input string: "b"
    }

    static void divide(int a, int b){

        try{

            int result = a / b;
            System.out.printf("Результат: %d\n", result);
        }
        catch(Throwable e){

            System.out.println("Возникло исключение!");
        }
        finally{

            System.out.println("Блок finally");
        }
    }
    static int parse(String s){
        try
        {
            return Integer.parseInt(s);
        }
        catch (Throwable e){
            System.out.println("error perse");
            return 0;
        }
        finally{
            return -1;
        }
    }
    static void printNumber(int index){

        String[] data = {"1", "a", "2", "b", "3", "c"};

        try{
            // пытаемся преобразовать значение массива по индексу в число
            int result =Integer.parseInt(data[index]);
            System.out.printf("По индексу %d число %d\n", index, result);
        }
        catch(ArrayIndexOutOfBoundsException e){  // обрабатываем выход за границы массива

            System.out.println("Выход за пределы массива");
        }
        catch(Exception ex){        // обрабатываем все остальные типы исключений

            System.out.println(ex.getMessage());    // выводим сообщение об исключении
            ex.printStackTrace();
        }
    }

}