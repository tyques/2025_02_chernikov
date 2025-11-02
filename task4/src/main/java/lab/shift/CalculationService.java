package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class CalculationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationService.class);

    public BigDecimal calculateSum(int n, Function<Integer, BigDecimal> termFunction) {
        int numberOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        LOGGER.info("Используется {} потоков", numberOfThreads);

        BigDecimal totalSum = BigDecimal.ZERO;

        try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            List<CalculationTask> tasks = createTasks(n, numberOfThreads, termFunction);

            List<Future<BigDecimal>> futures = executor.invokeAll(tasks);

            for (Future<BigDecimal> future : futures) {
                totalSum = totalSum.add(future.get());
            }
            LOGGER.info("Все задачи успешно выполнены.");

        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Ошибка во время выполнения задач", e);
            Thread.currentThread().interrupt();
        }

        LOGGER.info("Пул потоков остановлен.");
        return totalSum;
    }

    private List<CalculationTask> createTasks(int n, int numberOfThreads, Function<Integer, BigDecimal> termFunction) {
        List<CalculationTask> tasks = new ArrayList<>();
        int chunkSize = (n + numberOfThreads - 1) / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * chunkSize + 1;
            int end = Math.min((i + 1) * chunkSize, n);

            if (start > n) {
                break;
            }

            tasks.add(new CalculationTask(start, end, termFunction));
        }
        return tasks;
    }
}