package lab.shift.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineArgsTest {

    private static final String NOT_ENOUGH_ARGS_MESSAGE = "Недостаточно аргументов. Требуется указать входной файл и место вывода.\n" +
            "Пример: --input input.txt --output CONSOLE\n" +
            "Или: --input input.txt --output output.txt";

    @Test
    @DisplayName("Успешный парсинг корректных аргументов")
    void testParseValidArgs() {
        String[] args = {"--input", "input.txt", "--output", "output.txt"};
        CommandLineArgs parsedArgs = CommandLineArgs.parse(args);
        assertEquals("input.txt", parsedArgs.getInputFile());
        assertEquals("output.txt", parsedArgs.getOutputFile());
    }

    @Test
    @DisplayName("Успешный парсинг с выводом в консоль")
    void testParseValidArgsConsoleOutput() {
        String[] args = {"--input", "input.txt", "--output", "CONSOLE"};
        CommandLineArgs parsedArgs = CommandLineArgs.parse(args);
        assertEquals("input.txt", parsedArgs.getInputFile());
        assertEquals("CONSOLE", parsedArgs.getOutputFile());
    }

    @Test
    @DisplayName("Успешный парсинг с аргументами в обратном порядке")
    void testParseValidArgsReversed() {
        String[] args = {"--output", "CONSOLE", "--input", "in.txt"};
        CommandLineArgs parsedArgs = CommandLineArgs.parse(args);
        assertEquals("in.txt", parsedArgs.getInputFile());
        assertEquals("CONSOLE", parsedArgs.getOutputFile());
    }

    @Test
    @DisplayName("Ошибка при недостаточном количестве аргументов")
    void testParseTooFewArgs() {
        String[] args = {"--input", "input.txt"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CommandLineArgs.parse(args);
        });
        assertEquals(NOT_ENOUGH_ARGS_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при отсутствии флагов")
    void testParseMissingFlags() {
        String[] args = {"input.txt", "output.txt"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CommandLineArgs.parse(args);
        });
        assertEquals(NOT_ENOUGH_ARGS_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при отсутствии значения для флага --input")
    void testParseMissingInputValue() {
        String[] args = {"--input", "--output", "output.txt"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CommandLineArgs.parse(args);
        });
        assertEquals(NOT_ENOUGH_ARGS_MESSAGE, exception.getMessage());
    }
}