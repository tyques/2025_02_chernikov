package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

public class Storage {
    public static final Logger log = LoggerFactory.getLogger(Storage.class);
    private final int capacity;
    private final Queue<Resource> queue;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    public synchronized void put(Resource resource, int producerId) throws InterruptedException {
        String threadType = "Producer-" + producerId;
        while (queue.size() == capacity) {
            log.info(LogMessages.THREAD_WAITING_STORAGE_FULL, threadType);
            wait();
        }
        log.info(LogMessages.THREAD_RESUMED, threadType);

        queue.add(resource);
        log.info(LogMessages.RESOURCE_PRODUCED, threadType, resource.id(), queue.size());

        notifyAll();
    }

    public synchronized Resource get(int consumerId) throws InterruptedException {
        String threadType = "Consumer-" + consumerId;
        while (queue.isEmpty()){
            log.info(LogMessages.THREAD_WAITING_STORAGE_EMPTY, threadType);
            wait();
        }
        log.info(LogMessages.THREAD_RESUMED, consumerId);

        Resource resource = queue.poll();
        log.info(LogMessages.RESOURCE_CONSUMED, consumerId, resource.id(), queue.size());
        notifyAll();
        return resource;
    }
}
