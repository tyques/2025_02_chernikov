package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

public class Storage {
    public static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);
    private final int capacity;
    private final Queue<Resource> queue;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayDeque<>(capacity);
    }

    public synchronized void put(Resource resource) throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while (queue.size() == capacity) {
            LOGGER.atInfo().addArgument(threadName).log(LogMessages.THREAD_WAITING_STORAGE_FULL);
            wait();
        }
        LOGGER.atInfo().addArgument(threadName).log(LogMessages.THREAD_RESUMED);

        queue.add(resource);
        LOGGER.atInfo()
                .addArgument(threadName)
                .addArgument(resource.id())
                .addArgument(queue.size())
                .log(LogMessages.RESOURCE_PRODUCED);

        notifyAll();
    }

    public synchronized Resource get() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        while (queue.isEmpty()) {
            LOGGER.atInfo().addArgument(threadName).log(LogMessages.THREAD_WAITING_STORAGE_EMPTY);
            wait();
        }
        LOGGER.atInfo().addArgument(threadName).log(LogMessages.THREAD_RESUMED);

        Resource resource = queue.poll();
        LOGGER.atInfo()
                .addArgument(threadName)
                .addArgument(resource.id())
                .addArgument(queue.size())
                .log(LogMessages.RESOURCE_CONSUMED);
        notifyAll();
        return resource;
    }
}