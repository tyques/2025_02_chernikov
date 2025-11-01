package lab.shift;

public class Consumer implements Runnable {
    private final int id;
    private final Storage storage;
    private final int consumerTime;

    public Consumer(int id, Storage storage, int consumerTime) {
        this.id = id;
        this.storage = storage;
        this.consumerTime = consumerTime;
    }

    @Override
    public void run() {
        while (true){
            try {
                Resource resource = storage.get(id);
                Thread.sleep(consumerTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
