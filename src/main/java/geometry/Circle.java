package geometry;

import java.awt.geom.Point2D;

public class Circle {
  private double centerX, centerY;
  private double radius;
  
  private Circle() {}
  
  public static Circle ofCenterAndRadius(Point2D c, double r) {
    Circle self = new Circle();
    self.centerX = c.getX();
    self.centerY = c.getY();
    self.radius = r;
    return self;
  }
  public static Circle ofDiameterPoints(Point2D a, Point2D b) {
    Circle self = new Circle();
    double aX = a.getX();
    double bX = b.getX();
    double aY = a.getY();
    double bY = b.getY();
    self.centerX = (aX + bX)/2;
    self.centerY = (aY + bY)/2;
    double deltaX = (bX - self.centerX);
    double deltaY = (bY - self.centerY);
    self.radius = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    return self;
  }
  
  public static Circle of(Point2D a, Point2D b, Point2D c) {
    LinearEquation chord1 = LinearEquation.of(a, b);
    LinearEquation radius1 = chord1.getPerpendicularEquationAtX((a.getX() + b.getX())/2);
    LinearEquation chord2 = LinearEquation.of(b, c);
    LinearEquation radius2 = chord2.getPerpendicularEquationAtX((b.getX() + c.getX())/2);
    Point2D center = radius1.getIntersection(radius2);
    double radiusLength = Triangle.distance(center, a);
    assert Math.abs(Triangle.distance(center, b) - radiusLength) < 0.00001
        && Math.abs(Triangle.distance(center, c) - radiusLength) < 0.00001
        : "All three radii should be the same";
    
    return Circle.ofCenterAndRadius(center, radiusLength);
  }

  public double getRadius() {
    return this.radius;
  }

  public Point2D getCenter() {
    return new Point2D.Double(this.centerX, this .centerY);
  }
  
  public boolean contains(Point2D p) {
    double pX = p.getX();
    double pY = p.getY();
    double deltaX = pX - centerX;
    double deltaY = pY - centerY;
    double delta = Math.sqrt(deltaX*deltaX + deltaY+deltaY);
    return delta < radius;
  }
}