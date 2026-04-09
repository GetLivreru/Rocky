public class task {

    private static class Foot implements Runnable {
        private final int number;

        public Foot(int number) {
            this.number = number;
        }

        public void makeSteps() {
            System.out.println("Make step by leg " + number);
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                makeSteps();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var thread1 = new Thread(new Foot(1));
        var thread2 = new Thread(new Foot(2));
        thread1.start();
        thread2.start();

        Thread.sleep(1000);
        
        thread1.interrupt();
        thread2.interrupt();


        thread1.join();
        thread2.join();
        System.out.println("Threads finished.");
    }
}

