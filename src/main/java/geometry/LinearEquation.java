package geometry;

import java.awt.geom.Point2D;

public class LinearEquation {
  // represents "y = mx + c"
  private double gradient;
  private double constant;
  
  private LinearEquation() {}
  
  public static LinearEquation of(double gradient, double constant) {
    LinearEquation self = new LinearEquation();
    self.gradient = gradient;
    self.constant = constant;
    return self;
  }
  
  public static LinearEquation of(Point2D a, Point2D b) {
    LinearEquation self = new LinearEquation();
    double aX = a.getX();
    double aY = a.getY();
    double bX = b.getX();
    double bY = b.getY();
    self.gradient = (bY - aY) / (bX - aX);
    self.constant = aY - self.gradient * aX;
    return self;
  }
  
  public static LinearEquation of(double gradient, Point2D p) {
    LinearEquation self = new LinearEquation();
    double pX = p.getX();
    double pY = p.getY();
    self.gradient = gradient;
    self.constant = pY - (gradient * pX);
    return self;
  }
  
  public double getY(double x) {
    return this.gradient * x + this.constant;
  }

  public double getX(double y) {
    return (y - this.constant) / this.gradient;
  }

  public double getGradient() {
    return gradient;
  }

  public double getConstant() {
    return constant;
  }
  
  public double getPerpendicularGradient() {
    return -1 / this.gradient;
  }
  
  public LinearEquation getPerpendicularEquationAtX(double x) {
    double perpendicularGradient = this.getPerpendicularGradient();
    final Point2D.Double crossingPoint = new Point2D.Double(x, this.getY(x));
    return LinearEquation.of(perpendicularGradient, crossingPoint);
  }
  
  public Point2D getIntersection(LinearEquation other) {
    double x = (other.constant - this.constant) / (this.gradient - other.gradient);

    return new Point2D.Double(x, this.getY(x));
  }
  
  @Override
  public boolean equals(Object o) {
    if (o.getClass() != LinearEquation.class) return false;
    LinearEquation other = (LinearEquation)o;
    return this.gradient == other.gradient
        && this.constant == other.constant;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.gradient) ^ (Double.doubleToLongBits(this.gradient) >>> 32));
    hash = 67 * hash + (int) (Double.doubleToLongBits(this.constant) ^ (Double.doubleToLongBits(this.constant) >>> 32));
    return hash;
  }
}
