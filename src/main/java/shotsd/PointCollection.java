package shotsd;

import java.util.List;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class PointCollection {
  private List<Point2D> data = new ArrayList<>();
  private double pixelScale;
  private List<PropertyChangeListener> listeners = new ArrayList<>();
  
  public void addPoint(Point2D p) {
    data.add(p);
    notifyListeners();
  }
  
  public Point2D getPoint(int idx) {
    return data.get(idx);
  }
  
  public int getPointCount() {
    return data.size();
  }
  
  public Point2D.Double getMean() {
    double sumX = 0, sumY = 0;
    double meanX = 0, meanY = 0;
    int pointCount = data.size();
    if (pointCount > 0) {
      for (Point2D p : data) {
        sumX += p.getX();
        sumY += p.getY();
      }
      meanX = sumX / pointCount; 
      meanY = sumY / pointCount; 
    }
    return new Point2D.Double(meanX, meanY);
  }
  
  private double getHypSquared(Point2D a, Point2D b) {
    double deltaX = a.getX() - b.getX();
    double deltaY = a.getY() - b.getY();
    return deltaX * deltaX + deltaY * deltaY;
  }
  
  public double getSD() {
    double sd = 0;
    int pointCount = data.size();
    if (pointCount > 0) {
      Point2D mean = getMean();
      double sumSquaredDiffs = 0;
      for (int i = 0; i < pointCount; i++){
        Point2D p = data.get(i);
        sumSquaredDiffs += getHypSquared(p, mean);
      }
      sd = Math.sqrt(sumSquaredDiffs/pointCount);
    }
    return sd;
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    listeners.add(listener);
  }
  public void notifyListeners() {
    for (PropertyChangeListener l : listeners) {
      l.propertyChange(new PropertyChangeEvent(this, "data", null, null));
    }
  }
}
