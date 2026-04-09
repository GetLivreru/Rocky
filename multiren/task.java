public class task {

    private static class StepCoordinator {
        private final Object monitor = new Object();
        private String currentLeg = "Ping";

        public void step(String legNumber) throws InterruptedException {
            synchronized (monitor) {
                while (currentLeg != legNumber) {
                    monitor.wait();
                }

                System.out.println("Make step by leg " + legNumber);
                currentLeg = currentLeg == "Ping" ? "Pong" : "Ping";
                monitor.notifyAll();
            }
        }
    }

    private static class Foot implements Runnable {
        private final String number;
        private final StepCoordinator coordinator;

        public Foot(String number, StepCoordinator coordinator) {
            this.number = number;
            this.coordinator = coordinator;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    coordinator.step(number);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StepCoordinator coordinator = new StepCoordinator();
        var thread1 = new Thread(new Foot("Ping", coordinator), "RightLeg");
        var thread2 = new Thread(new Foot("Pong", coordinator), "LeftLeg");
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

