package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.Callable;

public class CalculationTask implements Callable<BigDecimal> {

    private static final Logger logger = LoggerFactory.getLogger(CalculationTask.class);

    private final int start;
    private final int end;

    private static final MathContext MC = new MathContext(100);
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal ONE = BigDecimal.ONE;


    public CalculationTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public BigDecimal call() {
        logger.info("Начало вычислений в диапазоне [{}, {}]", start, end);
        BigDecimal partialSum = BigDecimal.ZERO;
        for (int i = start; i <= end; i++) {
            BigDecimal powerOfTwo = TWO.pow(i);
            BigDecimal term = ONE.divide(powerOfTwo, MC);
            partialSum = partialSum.add(term);
        }
        logger.info("Завершение вычислений в диапазоне [{}, {}]. Результат: {}", start, end, partialSum);
        return partialSum;
    }
}