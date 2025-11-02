package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String CONFIG_FILE = "config.properties";

    static void main() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                LOGGER.atWarn().addArgument(CONFIG_FILE).log(LogMessages.CONFIG_NOT_FOUND);
                return;
            }
            properties.load(is);
        } catch (IOException e) {
            LOGGER.atError().addArgument(CONFIG_FILE).setCause(e).log(LogMessages.LOAD_CONFIG_EXCEPTION);
            return;
        }
        int producerCount = Integer.parseInt(properties.getProperty("producerCount"));
        int consumerCount = Integer.parseInt(properties.getProperty("consumerCount"));
        int producerTime = Integer.parseInt(properties.getProperty("producerTime"));
        int consumerTime = Integer.parseInt(properties.getProperty("consumerTime"));
        int storageSize = Integer.parseInt(properties.getProperty("storageSize"));

        LOGGER.atInfo().log(LogMessages.APP_START_WITH_PARAMS);
        LOGGER.atInfo().addArgument(producerCount).log(LogMessages.PRODUCERS_COUNT);
        LOGGER.atInfo().addArgument(consumerCount).log(LogMessages.CONSUMER_COUNT);
        LOGGER.atInfo().addArgument(producerTime).log(LogMessages.PRODUCER_TIME);
        LOGGER.atInfo().addArgument(consumerTime).log(LogMessages.CONSUMER_TIME);
        LOGGER.atInfo().addArgument(storageSize).log(LogMessages.STORAGE_SIZE);

        Storage storage = new Storage(storageSize);

        for (int i = 1; i <= producerCount; i++) {
            Thread producerThread = new Thread(new Producer(i, storage, producerTime), "Producer-" + i);
            producerThread.start();
        }

        for (int i = 1; i <= consumerCount; i++) {
            Thread consumerThread = new Thread(new Consumer(i, storage, consumerTime), "Consumer-" + i);
            consumerThread.start();
        }
    }
}
