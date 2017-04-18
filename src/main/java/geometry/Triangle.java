package geometry;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Triangle {
  private Point2D a, b, c;
  private double lenA, lenB, lenC;
  private double angleA, angleB, angleC; // angles at the named points
  
  private Triangle() {}
  
  public static Triangle of(Set<Point2D> points) {
    Point2D.Double[] pArray = points.toArray(new Point2D.Double[0]);
    return Triangle.of(pArray[0],pArray[1],pArray[2]);
  }
  
  public static Triangle of(Point2D a, Point2D b, Point2D c) {
    Triangle self = new Triangle();
    self.a = a;
    self.b = b;
    self.c = c;
    self.setAllFromPoints();
    return self;
  }
  
  public boolean isAcute() {
    return this.angleA < Math.PI/2
        && this.angleB < Math.PI/2
        && this.angleC < Math.PI/2
        ;
  }
  
  public double[] allAngles() {
    return new double[] {
      this.angleA, this.angleB, this.angleC
    };
  }
  
  public double[] allSides() {
    return new double[] {
      this.lenA, this.lenB, this.lenC
    };
  }
  
  public Set<Point2D> allPoints() {
    Set<Point2D> points = new HashSet<>();
    points.add(this.a);
    points.add(this.b);
    points.add(this.c);
    return points;
  }
  
  private void setAllFromPoints() {
    double aSquared = distanceSquared(b,c);
    double bSquared = distanceSquared(a,c);
    double cSquared = distanceSquared(a,b);
    
    this.lenA = Math.sqrt(aSquared);
    this.lenB = Math.sqrt(bSquared);
    this.lenC = Math.sqrt(cSquared);
    
    this.angleA = Math.acos((bSquared + cSquared -aSquared) / (2 * this.lenB * this.lenC));
    this.angleB = Math.acos((aSquared + cSquared -bSquared) / (2 * this.lenA * this.lenC));
    this.angleC = Math.PI - this.angleA - this.angleB;
  }
  
  public static double distance(Point2D a, Point2D b) {
    return Math.sqrt(distanceSquared(a,b));
  }
  
  public static double distanceSquared(Point2D a, Point2D b) {
      return Point2D.distanceSq(a.getX(), a.getY(), b.getX(), b.getY());
  }
}
