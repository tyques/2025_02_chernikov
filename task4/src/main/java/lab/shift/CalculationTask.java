package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class CalculationTask implements Callable<BigDecimal> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationTask.class);

    private final int start;
    private final int end;
    private final Function<Integer, BigDecimal> termFunction;

    public CalculationTask(int start, int end, Function<Integer, BigDecimal> termFunction) {
        this.start = start;
        this.end = end;
        this.termFunction = termFunction;
    }

    @Override
    public BigDecimal call() {
        LOGGER.info("Начало вычислений в диапазоне [{}, {}]", start, end);
        BigDecimal partialSum = BigDecimal.ZERO;
        for (int i = start; i <= end; i++) {
            BigDecimal term = termFunction.apply(i);
            partialSum = partialSum.add(term);
        }
        LOGGER.info("Завершение вычислений в диапазоне [{}, {}].", start, end);
        return partialSum;
    }
}