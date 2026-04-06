public class arry { 
      
    public static void main(String args[]) {
          
        // ===========================================
        // Все варианты цикла for
        // ===========================================

        // 1. Обычный for (все части на месте)
        System.out.println("=== 1. Обычный for ===");
        for (int i = 0; i < 3; i++) {
            System.out.println("i = " + i);
        }

        // 2. Без инициализации
        System.out.println("\n=== 2. Без инициализации ===");
        int a = 0;
        for (; a < 3; a++) {
            System.out.println("a = " + a);
        }

        // 3. Без условия (бесконечный, нужен break!)
        System.out.println("\n=== 3. Без условия ===");
        for (int b = 0; ; b++) {
            System.out.println("b = " + b);
            if (b >= 2) break;  // без этого - бесконечный цикл!
        }

        // 4. Без итерации
        System.out.println("\n=== 4. Без итерации ===");
        for (int c = 0; c < 3; ) {
            System.out.println("c = " + c);
            c++;  // итерация внутри тела
        }

        // 5. Без инициализации и итерации (как while)
        System.out.println("\n=== 5. Без инициализации и итерации ===");
        int d = 0;
        for (; d < 3; ) {
            System.out.println("d = " + d);
            d++;
        }

        // 6. Без всего - бесконечный цикл!
        System.out.println("\n=== 6. Без всего (for ;;) ===");
        int counter = 0;
        for (;;) {
            System.out.println("Итерация " + counter);
            counter++;
            if (counter >= 3) break;  // обязательно break!
        }

        System.out.println("\n=== Готово! ===");
    }
}
