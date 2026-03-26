package net.stoaz.a.designpatterns;


public class Triangle implements Shape {
    private final double a;
    private final double b;
    private final double c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }

    @Override
    public <T> T accept(ShapeVisitor<T> visitor) {
        return visitor.visitTriangle(this); // Double Dispatch
    }
}