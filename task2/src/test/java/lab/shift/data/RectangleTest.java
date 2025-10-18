package lab.shift.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleTest {

    private static final double DELTA = 0.01;

    @Test
    @DisplayName("Проверка вычисления площади прямоугольника")
    void testGetArea() {
        Rectangle rectangle = new Rectangle(5, 10);
        assertEquals(50.0, rectangle.getArea(), DELTA);
    }

    @Test
    @DisplayName("Проверка вычисления периметра прямоугольника")
    void testGetPerimeter() {
        Rectangle rectangle = new Rectangle(5, 10);
        assertEquals(30.0, rectangle.getPerimeter(), DELTA);
    }

    @Test
    @DisplayName("Проверка правильного определения длины и ширины")
    void testLengthAndWidthAssignment() {
        Rectangle rectangle = new Rectangle(10, 5); // Длина > Ширина
        String info = rectangle.getDetailedInfo();
        assertTrue(info.contains("Длина: 10,00 мм"));
        assertTrue(info.contains("Ширина: 5,00 мм"));
    }

    @Test
    @DisplayName("Проверка вычисления диагонали")
    void testDiagonalCalculation() {
        Rectangle rectangle = new Rectangle(3, 4);
        String info = rectangle.getDetailedInfo();
        assertTrue(info.contains("Длина диагонали: 5,00 мм"));
    }

    @Test
    @DisplayName("Проверка получения имени фигуры")
    void testGetName() {
        Rectangle rectangle = new Rectangle(5, 10);
        assertEquals("Rectangle", rectangle.getName());
    }
}