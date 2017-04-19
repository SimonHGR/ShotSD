package geometry;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;

public class Circle {

  private double centerX, centerY;
  private double radius;

  private Circle() {
  }

  public static Circle ofCenterAndRadius(Point2D c, double r) {
    Circle self = new Circle();
    self.centerX = c.getX();
    self.centerY = c.getY();
    self.radius = Math.abs(r);
    return self;
  }

  public static Circle ofDiameterPoints(Set<Point2D> points) {
    Point2D[] pArray = points.toArray(new Point2D.Double[0]);
    return Circle.ofDiameterPoints(pArray[0], pArray[1]);
  }

  public static Circle ofDiameterPoints(Point2D a, Point2D b) {
    Circle self = new Circle();
    double aX = a.getX();
    double bX = b.getX();
    double aY = a.getY();
    double bY = b.getY();
    self.centerX = (aX + bX) / 2;
    self.centerY = (aY + bY) / 2;
    double deltaX = (bX - self.centerX);
    double deltaY = (bY - self.centerY);
    self.radius = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    return self;
  }

  public static Circle of(Set<Point2D> points) {
    Point2D[] pArray = points.toArray(new Point2D.Double[0]);
    return Circle.of(pArray[0], pArray[1], pArray[2]);
  }

  public static Circle of(Triangle t) {
    return Circle.of(t.allPoints());
  }

  public static Circle of(Point2D a, Point2D b, Point2D c) {
    LinearEquation chord1 = LinearEquation.of(a, b);
    LinearEquation radius1 = chord1.getPerpendicularEquationAtX((a.getX() + b.getX()) / 2);
    LinearEquation chord2 = LinearEquation.of(b, c);
    LinearEquation radius2 = chord2.getPerpendicularEquationAtX((b.getX() + c.getX()) / 2);
    Point2D center = radius1.getIntersection(radius2);
    double radiusLength = Triangle.distance(center, a);
    assert Math.abs(Triangle.distance(center, b) - radiusLength) < 0.00001
        && Math.abs(Triangle.distance(center, c) - radiusLength) < 0.00001 : "All three radii should be the same";

    return Circle.ofCenterAndRadius(center, radiusLength);
  }

  public double getRadius() {
    return this.radius;
  }

  public Point2D getCenter() {
    return new Point2D.Double(this.centerX, this.centerY);
  }

  public boolean contains(Point2D p) {
    double pX = p.getX();
    double pY = p.getY();
    double deltaX = pX - centerX;
    double deltaY = pY - centerY;
    double delta = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    return delta < radius + 0.01;
  }

  public boolean containsAll(Collection<Point2D> points) {
    return points
        .stream()
        .allMatch(this::contains);
  }

  @Override
  public String toString() {
    return String.format("Circle: (%6.2f, %6.2f) %6.2f", centerX, centerY, radius);
  }
  
  public String toCode() {
    return String.format(
        "Circle.ofCenterAndRadius(new Point2D.Double(%12.9f, %12.9f),%12.9f)",
        centerX, centerY, radius);
  }
}
