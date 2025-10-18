package lab.shift.data;

import static java.lang.Math.*;

public final class Triangle implements Shape {
    private final double sideA;
    private final double sideB;
    private final double sideC;

    public Triangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public double getArea() {
        double halfPerimeter = getPerimeter() / 2;
        return sqrt(halfPerimeter * (halfPerimeter - sideA) * (halfPerimeter - sideB) * (halfPerimeter - sideC));
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    /**
     * Вычисляет угол, противолежащий стороне A, в градусах.
     * Используется теорема косинусов.
     */

    public double getAngleA() {
        double cosA = (pow(sideB, 2) + pow(sideC, 2) - pow(sideA, 2)) / (2 * sideB * sideC);
        return toDegrees(acos(cosA));
    }


    public double getAngleB() {
        double cosB = (pow(sideA, 2) + pow(sideC, 2) - pow(sideB, 2)) / (2 * sideA * sideC);
        return toDegrees(acos(cosB));
    }


    public double getAngleC() {
        double cosC = (pow(sideA, 2) + pow(sideB, 2) - pow(sideC, 2)) / (2 * sideA * sideB);
        return toDegrees(acos(cosC));
    }

    @Override
    public String getDetailedInfo() {
        return String.format(
                "Тип фигуры: Треугольник\n" +
                        "Площадь: %.2f кв. мм\n" +
                        "Периметр: %.2f мм\n" +
                        "Сторона A: %.2f мм, противолежащий угол: %.2f градусов\n" +
                        "Сторона B: %.2f мм, противолежащий угол: %.2f градусов\n" +
                        "Сторона C: %.2f мм, противолежащий угол: %.2f градусов",
                getArea(),
                getPerimeter(),
                sideA, getAngleA(),
                sideB, getAngleB(),
                sideC, getAngleC()
        );
    }
}
