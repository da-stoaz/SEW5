package net.stoaz.a.designpatterns;

public class DescriptionVisitor implements ShapeVisitor<String> {
    @Override
    public String visitCircle(Circle circle) {
        return "Circle(radius=" + circle.getRadius() + ")";
    }

    @Override
    public String visitRectangle(Rectangle rectangle) {
        return "Rectangle(width=" + rectangle.getWidth() + ", height=" + rectangle.getHeight() + ")";
    }
}