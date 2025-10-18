package lab.shift.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircleTest {

    private static final double DELTA = 0.01;

    @Test
    @DisplayName("Проверка вычисления площади круга")
    void testGetArea() {
        Circle circle = new Circle(10);
        assertEquals(314.15, circle.getArea(), DELTA);
    }

    @Test
    @DisplayName("Проверка вычисления периметра (длины окружности)")
    void testGetPerimeter() {
        Circle circle = new Circle(10);
        assertEquals(62.83, circle.getPerimeter(), DELTA);
    }

    @Test
    @DisplayName("Проверка получения имени фигуры")
    void testGetName() {
        Circle circle = new Circle(10);
        assertEquals("Circle", circle.getName());
    }

    @Test
    @DisplayName("Проверка форматирования детальной информации")
    void testGetDetailedInfo() {
        Circle circle = new Circle(5);
        String expected = "Тип фигуры: Круг\n" +
                "Площадь: 78,54 кв. мм\n" +
                "Периметр: 31,42 мм\n" +
                "Радиус: 5,00 мм\n" +
                "Диаметр: 10,00 мм";
        assertEquals(expected, circle.getDetailedInfo());
    }
}