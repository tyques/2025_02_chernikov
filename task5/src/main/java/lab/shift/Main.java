package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Main {
    public static final String APP_START_WITH_PARAMS = "Приложение запущено с параметрами:";
    public static final String PRODUCERS_COUNT = " - Количество производителей: {}";
    public static final String CONSUMER_COUNT = " - Количество потребителей: {}";
    public static final String PRODUCER_TIME = " - Время производства: {} мс";
    public static final String CONSUMER_TIME = " - Время потребления: {} мс";
    public static final String STORAGE_SIZE = " - Размер склада: {}";
    public static final String CONFIG_LOAD_ERROR = "Не удалось запустить приложение из-за ошибки конфигурации.";
    public static final String APP_SHUTTING_DOWN = "Все потоки завершены с помощью shutdown hook.";
    public static final String INITIATING_SHUTDOWN = "Инициируется остановка потоков...";
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        ConfigurationLoader loader = new ConfigurationLoader();
        Optional<ApplicationProperties> propertiesOptional = loader.loadProperties();

        if (propertiesOptional.isEmpty()) {
            LOGGER.error(CONFIG_LOAD_ERROR);
            return;
        }

        ApplicationProperties props = propertiesOptional.get();
        logConfiguration(props);

        Storage storage = new Storage(props.getStorageSize());
        List<Thread> threads = new ArrayList<>();

        createAndStartWorkers(props.getProducerCount(), "Producer",
                () -> new Producer(storage, props.getProducerTime()), threads);

        createAndStartWorkers(props.getConsumerCount(), "Consumer",
                () -> new Consumer(storage, props.getConsumerTime()), threads);


        addShutdownHook(threads);
    }

    private static void createAndStartWorkers(int count, String namePrefix, Supplier<Runnable> runnableSupplier, List<Thread> threads) {
        for (int i = 1; i <= count; i++) {
            Thread workerThread = new Thread(runnableSupplier.get(), namePrefix + "-" + i);
            threads.add(workerThread);
            workerThread.start();
        }
    }

    private static void logConfiguration(ApplicationProperties props) {
        LOGGER.atInfo().log(APP_START_WITH_PARAMS);
        LOGGER.atInfo().addArgument(props.getProducerCount()).log(PRODUCERS_COUNT);
        LOGGER.atInfo().addArgument(props.getConsumerCount()).log(CONSUMER_COUNT);
        LOGGER.atInfo().addArgument(props.getProducerTime()).log(PRODUCER_TIME);
        LOGGER.atInfo().addArgument(props.getConsumerTime()).log(CONSUMER_TIME);
        LOGGER.atInfo().addArgument(props.getStorageSize()).log(STORAGE_SIZE);
    }

    private static void addShutdownHook(List<Thread> threads) {
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