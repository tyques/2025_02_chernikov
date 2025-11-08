package lab.shift;

public class Consumer implements Runnable {
    private final Storage storage;
    private final int consumerTime;

    public Consumer(Storage storage, int consumerTime) {
        this.storage = storage;
        this.consumerTime = consumerTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                storage.get();
                Thread.sleep(consumerTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}