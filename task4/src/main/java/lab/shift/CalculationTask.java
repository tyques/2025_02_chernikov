package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class CalculationTask implements Callable<Double> {
    public static final Logger logger = LoggerFactory.getLogger(CalculationTask.class);
    private final long start;
    private final long end;
    private double result;

    public CalculationTask(long start, long end) {
        this.start = start;
        this.end = end;
        this.result = 0.0;
    }

    @Override
    public Double call() {
        logger.info("Начало вычислений в диапазоне [{}, {}]", start, end);
        for (long i = start; i <= end; i++) {
            result += 1.0 / Math.pow(2, i);
        }
        logger.info("Завершение вычислений в диапазоне [{}, {}]. Результат: {}", start, end, result);
        return result;
    }
}
