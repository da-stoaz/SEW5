package net.stoaz.a.designpatterns;

public class Circle implements Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public <T> T accept(ShapeVisitor<T> visitor) {
        return visitor.visitCircle(this);
    }
}