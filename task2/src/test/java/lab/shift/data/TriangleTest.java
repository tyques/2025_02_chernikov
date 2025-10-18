package lab.shift.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriangleTest {

    private static final double DELTA = 0.01;

    @Test
    @DisplayName("Проверка вычисления площади треугольника (египетский треугольник)")
    void testGetArea() {
        Triangle triangle = new Triangle(3, 4, 5);
        assertEquals(6.0, triangle.getArea(), DELTA);
    }

    @Test
    @DisplayName("Проверка вычисления периметра треугольника")
    void testGetPerimeter() {
        Triangle triangle = new Triangle(3, 4, 5);
        assertEquals(12.0, triangle.getPerimeter(), DELTA);
    }

    @Test
    @DisplayName("Проверка вычисления углов для прямоугольного треугольника")
    void testGetAnglesForRightTriangle() {
        Triangle triangle = new Triangle(3, 4, 5);
        // Углы напротив сторон 3, 4, 5
        assertEquals(36.87, triangle.getAngleA(), DELTA);
        assertEquals(53.13, triangle.getAngleB(), DELTA);
        assertEquals(90.0, triangle.getAngleC(), DELTA);
    }

    @Test
    @DisplayName("Проверка вычисления углов для равностороннего треугольника")
    void testGetAnglesForEquilateralTriangle() {
        Triangle triangle = new Triangle(5, 5, 5);
        assertEquals(60.0, triangle.getAngleA(), DELTA);
        assertEquals(60.0, triangle.getAngleB(), DELTA);
        assertEquals(60.0, triangle.getAngleC(), DELTA);
    }

    @Test
    @DisplayName("Проверка получения имени фигуры")
    void testGetName() {
        Triangle triangle = new Triangle(3, 4, 5);
        assertEquals("Triangle", triangle.getName());
    }

    @Test
    @DisplayName("Проверка форматирования детальной информации")
    void testGetDetailedInfo() {
        Triangle triangle = new Triangle(3, 4, 5);
        String expected = "Тип фигуры: Треугольник\n" +
                "Площадь: 6.00 кв. мм\n" +
                "Периметр: 12.00 мм\n" +
                "Сторона A: 3.00 мм. противолежащий угол: 36.87 градусов\n" +
                "Сторона B: 4.00 мм. противолежащий угол: 53.13 градусов\n" +
                "Сторона C: 5.00 мм. противолежащий угол: 90.00 градусов";

        String actual = triangle.getDetailedInfo().replace(",", ".");

        assertEquals(expected, actual);
    }
}