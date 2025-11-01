package lab.shift;

public class Producer implements Runnable {
    private final int id;
    private final Storage storage;
    private final int producerTime;
    private static int resourceId;

    public Producer(int id, Storage storage, int producerTime) {
        this.id = id;
        this.storage = storage;
        this.producerTime = producerTime;
        resourceId = 0;
    }

    @Override
    public void run() {
        while (true){
            Resource resource = new Resource(getNextResourceId());
            try {
                storage.put(resource, id);
                Thread.sleep(producerTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static synchronized int getNextResourceId(){
        return ++resourceId;
    }
}
