package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static lab.shift.LogMessages.APP_SHUTTING_DOWN;
import static lab.shift.LogMessages.INITIATING_SHUTDOWN;

public class Main {
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final int APP_RUNTIME_MS = 5000;

    public static void main(String[] args) {
        Configuration config = new Configuration();
        if (!config.loadProperties()) {
            return;
        }

        LOGGER.atInfo().log(LogMessages.APP_START_WITH_PARAMS);
        LOGGER.atInfo().addArgument(config.getProducerCount()).log(LogMessages.PRODUCERS_COUNT);
        LOGGER.atInfo().addArgument(config.getConsumerCount()).log(LogMessages.CONSUMER_COUNT);
        LOGGER.atInfo().addArgument(config.getProducerTime()).log(LogMessages.PRODUCER_TIME);
        LOGGER.atInfo().addArgument(config.getConsumerTime()).log(LogMessages.CONSUMER_TIME);
        LOGGER.atInfo().addArgument(config.getStorageSize()).log(LogMessages.STORAGE_SIZE);

        Storage storage = new Storage(config.getStorageSize());
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= config.getProducerCount(); i++) {
            Thread producerThread = new Thread(new Producer(storage, config.getProducerTime()), "Producer-" + i);
            threads.add(producerThread);
            producerThread.start();
        }

        for (int i = 1; i <= config.getConsumerCount(); i++) {
            Thread consumerThread = new Thread(new Consumer(storage, config.getConsumerTime()), "Consumer-" + i);
            threads.add(consumerThread);
            consumerThread.start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info(INITIATING_SHUTDOWN);
            for (Thread thread : threads) {
                thread.interrupt();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            LOGGER.info(APP_SHUTTING_DOWN);
        }));
    }
}