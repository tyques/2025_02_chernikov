package lab.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final MathContext MC = new MathContext(100);

    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите N (например, 10000000): ");
        int n = scanner.nextInt();
        scanner.close();

        LOGGER.info("Начало вычислений для N = {}", n);

        Function<Integer, BigDecimal> termFunction = (i) -> {
            BigDecimal powerOfTwo = BigDecimal.valueOf(2).pow(i);
            return BigDecimal.ONE.divide(powerOfTwo, MC);
        };

        CalculationService calculationService = new CalculationService();
        BigDecimal result = calculationService.calculateSum(n, termFunction);

        System.out.println("Итоговая сумма: " + result);
    }
}