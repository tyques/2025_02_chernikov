package lab.shift;

import lab.shift.config.CommandLineArgs;
import lab.shift.data.Shape;
import lab.shift.factory.ShapeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final CommandLineArgs args;

    public Application(CommandLineArgs args) {
        this.args = args;
    }

    public void run() throws IOException, NumberFormatException {
        logger.info("Входной файл: {}", args.getInputFile());
        logger.info("Место вывода: {}", args.getOutputFile());

        List<String> lines = readInputFile();
        Shape shape = createShapeFromLines(lines);
        logger.info("Фигура '{}' успешно создана.", shape.getName());

        String detailedInfo = shape.getDetailedInfo();
        writeOutput(detailedInfo);
    }

    private List<String> readInputFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args.getInputFile()));
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Файл должен содержать как минимум 2 строки: тип фигуры и параметры.");
        }
        return lines;
    }

    private Shape createShapeFromLines(List<String> lines) throws NumberFormatException {
        String shapeType = lines.get(0).trim();
        double[] params = Arrays.stream(lines.get(1).trim().split("\\s+"))
                .mapToDouble(Double::parseDouble)
                .toArray();

        logger.info("Тип фигуры из файла: {}", shapeType);
        logger.info("Параметры из файла: {}", Arrays.toString(params));

        return ShapeFactory.createShape(shapeType, params);
    }

    private void writeOutput(String content) throws IOException {
        if ("CONSOLE".equalsIgnoreCase(args.getOutputFile())) {
            logger.info("Вывод результата в консоль.");
            System.out.println("\n--- Результат ---");
            System.out.println(content);
            System.out.println("-----------------");
        } else {
            logger.info("Запись результата в файл: {}", args.getOutputFile());
            Path outputPath = Paths.get(args.getOutputFile());
            Files.writeString(outputPath, content);
            logger.info("Результат успешно записан в файл.");
        }
    }
}