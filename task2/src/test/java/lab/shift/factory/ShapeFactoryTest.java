package lab.shift.factory;

import lab.shift.data.Circle;
import lab.shift.data.Rectangle;
import lab.shift.data.Shape;
import lab.shift.data.Triangle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeFactoryTest {

    @Test
    @DisplayName("Успешное создание круга")
    void testCreateCircle() {
        Shape shape = ShapeFactory.createShape("CIRCLE", 10);
        assertNotNull(shape);
        assertInstanceOf(Circle.class, shape);
        assertEquals(314.15, shape.getArea(), 0.01);
    }

    @Test
    @DisplayName("Успешное создание круга с использованием нижнего регистра")
    void testCreateCircleLowerCase() {
        Shape shape = ShapeFactory.createShape("circle", 10);
        assertNotNull(shape);
        assertInstanceOf(Circle.class, shape);
    }

    @Test
    @DisplayName("Успешное создание прямоугольника")
    void testCreateRectangle() {
        Shape shape = ShapeFactory.createShape("RECTANGLE", 10, 20);
        assertNotNull(shape);
        assertInstanceOf(Rectangle.class, shape);
        assertEquals(200, shape.getArea(), 0.01);
    }

    @Test
    @DisplayName("Успешное создание треугольника")
    void testCreateTriangle() {
        Shape shape = ShapeFactory.createShape("TRIANGLE", 3, 4, 5);
        assertNotNull(shape);
        assertInstanceOf(Triangle.class, shape);
        assertEquals(6, shape.getArea(), 0.01);
    }

    @Test
    @DisplayName("Ошибка при создании фигуры с неизвестным типом")
    void testCreateUnknownShape() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShapeFactory.createShape("SQUARE", 10);
        });
        assertEquals("Неизвестный тип фигуры: SQUARE", exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при создании круга с недостаточным количеством параметров")
    void testCreateCircleWithNotEnoughParams() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShapeFactory.createShape("CIRCLE");
        });
        assertEquals("Для создания круга (CIRCLE) требуется 1 параметр (радиус), но получено: 0", exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при создании прямоугольника с недостаточным количеством параметров")
    void testCreateRectangleWithNotEnoughParams() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShapeFactory.createShape("RECTANGLE", 10);
        });
        assertEquals("Для создания прямоугольника (RECTANGLE) требуется 2 параметра (стороны), но получено: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Ошибка при создании треугольника с недостаточным количеством параметров")
    void testCreateTriangleWithNotEnoughParams() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShapeFactory.createShape("TRIANGLE", 3, 4);
        });
        assertEquals("Для создания треугольника (TRIANGLE) требуется 3 параметра (стороны), но получено: 2", exception.getMessage());
    }
}