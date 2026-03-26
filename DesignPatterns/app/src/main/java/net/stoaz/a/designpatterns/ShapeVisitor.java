package net.stoaz.a.designpatterns;

public interface ShapeVisitor<T> {
    T visitCircle(Circle circle);
    T visitRectangle(Rectangle rectangle);

    T visitTriangle(Triangle triangle);

}