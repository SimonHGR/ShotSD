package geometry;

import java.awt.geom.Point2D;

public class Triangle {
  public static double distance(Point2D a, Point2D b) {
    double aX = a.getX();
    double aY = a.getY();
    double bX = b.getX();
    double bY = b.getY();
    double deltaX = aX - bX;
    double deltaY = aY - bY;
    return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
  }
}
