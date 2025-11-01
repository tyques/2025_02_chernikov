package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String CONFIG_FILE = "config.properties";

    static void main() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                log.warn(LogMessages.CONFIG_NOT_FOUND, CONFIG_FILE);
                return;
            }
            properties.load(is);
        } catch (IOException e) {
            log.error(LogMessages.LOAD_CONFIG_EXCEPTION, CONFIG_FILE, e);
            return;
        }
        int producerCount = Integer.parseInt(properties.getProperty("producerCount"));
        int consumerCount = Integer.parseInt(properties.getProperty("consumerCount"));
        int producerTime = Integer.parseInt(properties.getProperty("producerTime"));
        int consumerTime = Integer.parseInt(properties.getProperty("consumerTime"));
        int storageSize = Integer.parseInt(properties.getProperty("storageSize"));

        log.info("Приложение запущено с параметрами:");
        log.info(" - Количество производителей: {}", producerCount);
        log.info(" - Количество потребителей: {}", consumerCount);
        log.info(" - Время производства: {} мс", producerTime);
        log.info(" - Время потребления: {} мс", consumerTime);
        log.info(" - Размер склада: {}", storageSize);

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
