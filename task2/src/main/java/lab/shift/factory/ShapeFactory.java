package lab.shift.factory;

import lab.shift.data.Circle;
import lab.shift.data.Rectangle;
import lab.shift.data.Shape;
import lab.shift.data.Triangle;

public class ShapeFactory {

    public static Shape createShape(String shapeType, double... params) {
        String type = shapeType.toUpperCase();

        return switch (type) {
            case "CIRCLE" -> {
                if (params.length < 1) {
                    throw new IllegalArgumentException("Для создания круга (CIRCLE) требуется 1 параметр (радиус), но получено: " + params.length);
                }
                yield new Circle(params[0]);
            }
            case "RECTANGLE" -> {
                if (params.length < 2) {
                    throw new IllegalArgumentException("Для создания прямоугольника (RECTANGLE) требуется 2 параметра (стороны), но получено: " + params.length);
                }
                yield new Rectangle(params[0], params[1]);
            }
            case "TRIANGLE" -> {
                if (params.length < 3) {
                    throw new IllegalArgumentException("Для создания треугольника (TRIANGLE) требуется 3 параметра (стороны), но получено: " + params.length);
                }
                yield new Triangle(params[0], params[1], params[2]);
            }
            default -> throw new IllegalArgumentException("Неизвестный тип фигуры: " + shapeType);
        };
    }
}