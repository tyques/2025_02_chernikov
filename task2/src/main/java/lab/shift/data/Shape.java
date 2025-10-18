package lab.shift.data;

public sealed interface Shape permits Circle, Rectangle, Triangle {
    double getArea();

    double getPerimeter();

    String getDetailedInfo();

    default String getName() {
        return this.getClass().getSimpleName();
    }
}
