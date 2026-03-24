package net.stoaz.a.designpatterns;

public class AreaVisitor implements ShapeVisitor<Double> {
    @Override
    public Double visitCircle(Circle circle) {
        return Math.PI * circle.getRadius() * circle.getRadius();
    }

    @Override
    public Double visitRectangle(Rectangle rectangle) {
        return rectangle.getWidth() * rectangle.getHeight();
    }
}