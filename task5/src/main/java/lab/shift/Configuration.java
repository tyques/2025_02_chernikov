package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private static final String CONFIG_FILE = "config.properties";
    public static final String LOAD_CONFIG_EXCEPTION = "Не удалось загрузить файл конфигурации {}";
    public static final String CONFIG_NOT_FOUND = "Ресурс '{}' не найден в classpath";

    private int producerCount;
    private int consumerCount;
    private int producerTime;
    private int consumerTime;
    private int storageSize;

    public boolean loadProperties() {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                LOGGER.atWarn().addArgument(CONFIG_FILE).log(CONFIG_NOT_FOUND);
                return false;
            }
            properties.load(is);
        } catch (IOException e) {
            LOGGER.atError().addArgument(CONFIG_FILE).setCause(e).log(LOAD_CONFIG_EXCEPTION);
            return false;
        }

        try {
            producerCount = parseAndValidateInt(properties, "producerCount");
            consumerCount = parseAndValidateInt(properties, "consumerCount");
            producerTime = parseAndValidateInt(properties, "producerTime");
            consumerTime = parseAndValidateInt(properties, "consumerTime");
            storageSize = parseAndValidateInt(properties, "storageSize");
            return true;
        } catch (IllegalArgumentException e) {
            LOGGER.atError().setCause(e).log("Ошибка валидации конфигурации: {}", e.getMessage());
            return false;
        }
    }

    private int parseAndValidateInt(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Параметр '" + key + "' не найден в config.properties");
        }
        int parsedValue = Integer.parseInt(value);
        if (parsedValue <= 0) {
            throw new IllegalArgumentException("Параметр '" + key + "' должен быть положительным числом. Текущее значение: " + parsedValue);
        }
        return parsedValue;
    }

    public int getProducerCount() {
        return producerCount;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public int getProducerTime() {
        return producerTime;
    }

    public int getConsumerTime() {
        return consumerTime;
    }

    public int getStorageSize() {
        return storageSize;
    }
}