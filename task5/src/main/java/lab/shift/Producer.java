package lab.shift;

public class Producer implements Runnable {
    private final Storage storage;
    private final int producerTime;

    public Producer(Storage storage, int producerTime) {
        this.storage = storage;
        this.producerTime = producerTime;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Resource resource = Resource.create();
            try {
                storage.put(resource);
                Thread.sleep(producerTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}