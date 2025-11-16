package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static final String APP_START_WITH_PARAMS = "Приложение запущено с параметрами:";
    public static final String PRODUCERS_COUNT = " - Количество производителей: {}";
    public static final String CONSUMER_COUNT = " - Количество потребителей: {}";
    public static final String PRODUCER_TIME = " - Время производства: {} мс";
    public static final String CONSUMER_TIME = " - Время потребления: {} мс";
    public static final String STORAGE_SIZE = " - Размер склада: {}";
    public static final String APP_SHUTTING_DOWN = "Все потоки завершены с помощью shutdown hook";
    public static final String INITIATING_SHUTDOWN = "Инициируется остановка потоков...";
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Configuration config = new Configuration();
        if (!config.loadProperties()) {
            return;
        }

        LOGGER.atInfo().log(APP_START_WITH_PARAMS);
        LOGGER.atInfo().addArgument(config.getProducerCount()).log(PRODUCERS_COUNT);
        LOGGER.atInfo().addArgument(config.getConsumerCount()).log(CONSUMER_COUNT);
        LOGGER.atInfo().addArgument(config.getProducerTime()).log(PRODUCER_TIME);
        LOGGER.atInfo().addArgument(config.getConsumerTime()).log(CONSUMER_TIME);
        LOGGER.atInfo().addArgument(config.getStorageSize()).log(STORAGE_SIZE);

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