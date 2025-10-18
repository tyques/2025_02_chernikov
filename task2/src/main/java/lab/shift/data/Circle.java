package lab.shift.data;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public final class Circle implements Shape {
    private final double radius;
    private final double diameter;

    public Circle(double radius) {
        this.radius = radius;
        this.diameter = radius * 2;
    }

    @Override
    public double getArea() {
        return PI * pow(radius, 2);
    }

    @Override
    public double getPerimeter() {
        return 2 * PI * radius;
    }

    @Override
    public String getDetailedInfo() {
        return String.format("Тип фигуры: Круг\nПлощадь: %.2f кв. мм\nПериметр: %.2f мм\nРадиус: %.2f мм\nДиаметр: %.2f мм",
                getArea(), getPerimeter(), radius, diameter);
    }
}
