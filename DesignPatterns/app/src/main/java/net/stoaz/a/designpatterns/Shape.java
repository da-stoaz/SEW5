package net.stoaz.a.designpatterns;

public interface Shape {
    <T> T accept(ShapeVisitor<T> visitor);
}