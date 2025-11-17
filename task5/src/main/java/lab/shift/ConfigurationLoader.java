package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class ConfigurationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoader.class);
    private static final String CONFIG_FILE = "config.properties";
    public static final String LOAD_CONFIG_EXCEPTION = "Не удалось загрузить файл конфигурации {}";
    public static final String CONFIG_NOT_FOUND = "Ресурс '{}' не найден в classpath";

    public Optional<ApplicationProperties> loadProperties() {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                LOGGER.atWarn().addArgument(CONFIG_FILE).log(CONFIG_NOT_FOUND);
                return Optional.empty();
            }
            properties.load(is);
        } catch (IOException e) {
            LOGGER.atError().addArgument(CONFIG_FILE).setCause(e).log(LOAD_CONFIG_EXCEPTION);
            return Optional.empty();
        }

        try {
            int producerCount = parseAndValidateInt(properties, "producerCount");
            int consumerCount = parseAndValidateInt(properties, "consumerCount");
            int producerTime = parseAndValidateInt(properties, "producerTime");
            int consumerTime = parseAndValidateInt(properties, "consumerTime");
            int storageSize = parseAndValidateInt(properties, "storageSize");

            ApplicationProperties appProps = new ApplicationProperties(
                    producerCount,
                    consumerCount,
                    producerTime,
                    consumerTime,
                    storageSize
            );
            return Optional.of(appProps);
        } catch (IllegalArgumentException e) {
            LOGGER.atError().setCause(e).log("Ошибка валидации конфигурации: {}", e.getMessage());
            return Optional.empty();
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
}