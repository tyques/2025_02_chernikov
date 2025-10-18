package lab.shift.data;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class Rectangle implements Shape {
    private final double length;
    private final double width;
    private final double diagonal;

    public Rectangle(double side1, double side2) {
        this.length = Math.max(side1, side2);
        this.width = Math.min(side1, side2);
        this.diagonal = sqrt(pow(length, 2) + pow(width, 2));
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }

    @Override
    public String getDetailedInfo() {
        return String.format("Тип фигуры: Прямоугольник\nПлощадь: %.2f кв. мм\nПериметр: %.2f мм\nДлина диагонали: %.2f мм\nДлина: %.2f мм\nШирина: %.2f мм",
                getArea(), getPerimeter(), diagonal, length, width);
    }
}