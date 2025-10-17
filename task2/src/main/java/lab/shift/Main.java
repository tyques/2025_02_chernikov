package lab.shift;

import lab.shift.config.CommandLineArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Приложение запущено.");

        try {
            CommandLineArgs parsedArgs = CommandLineArgs.parse(args);
            Application runner = new Application(parsedArgs);
            runner.run();
        } catch (IOException e) {
            logger.error("Ошибка чтения или записи файла: {}", e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Ошибка парсинга параметров фигуры. Убедитесь, что параметры являются числами: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при создании фигуры: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла непредвиденная ошибка.", e);
        }

        logger.info("Приложение завершило работу.");
    }
}