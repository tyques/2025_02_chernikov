package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите N (например, 10000000): ");
        int n = scanner.nextInt();
        scanner.close();

        logger.info("Начало вычислений для N = {}", n);

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        logger.info("Используется {} потоков", numberOfThreads);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<CalculationTask> tasks = new ArrayList<>();
        int partSize = n / numberOfThreads;
        int start = 1;
        for (int i = 0; i < numberOfThreads; i++) {
            int end;
            if (i == numberOfThreads - 1) {
                end = n;
            } else {
                end = start + partSize - 1;
            }
            tasks.add(new CalculationTask(start, end));
            start += partSize;
        }

        try {
            List<Future<BigDecimal>> futures = executor.invokeAll(tasks);

            BigDecimal totalSum = BigDecimal.ZERO;
            for (Future<BigDecimal> future : futures) {
                totalSum = totalSum.add(future.get());
            }

            logger.info("Все задачи выполнены.");
            System.out.println("Итоговая сумма: " + totalSum);

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ошибка во время выполнения задач", e);
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
            logger.info("Пул потоков остановлен.");
        }
    }
}
