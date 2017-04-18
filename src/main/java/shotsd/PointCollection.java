package shotsd;

import combinations.Combinations;
import geometry.Circle;
import geometry.Triangle;
import java.awt.Color;
import java.util.List;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class PointCollection {

  private List<Point2D> data = new ArrayList<>();
  private double pixelScale;
  private String groupName;
  private Color groupColor;
  private double range = 25;
  private String rangeUnitName = "Yards";
  private double rangeMultiplier = 12;
  private Optional<Circle> minCircle;
  private List<PropertyChangeListener> listeners = new ArrayList<>();

  public PointCollection(double pixelScale, String groupName, Color color) {
    this.pixelScale = pixelScale;
    this.groupName = groupName;
    this.groupColor = color;
    this.minCircle = Optional.empty();
  }

  public double getPixelScale() {
    return this.pixelScale;
  }

  public Color getGroupColor() {
    return this.groupColor;
  }

  public String getGroupName() {
    return groupName;
  }

  public double getRangeInInches() {
    return range * rangeMultiplier;
  }

  public String getRangeText() {
    return String.format("%4.2f %s", range, rangeUnitName);
  }

  public void setRangeInUnits(double range) {
    this.range = range;
  }

  public void setRangeUnitName(String rangeUnitName) {
    this.rangeUnitName = rangeUnitName;
  }

  public void setRangeMultiplier(double rangeMultiplier) {
    this.rangeMultiplier = rangeMultiplier;
  }

  public void addPoint(Point2D p) {
    data.add(p);
    computeMinCircle();
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

  public Optional<Circle> getMinCircle() {
    return this.minCircle;
  }
  
  private void computeMinCircle() {
//    Optional<Point2D> leftPoint = data.stream().min(Point2D::getX);
//    Optional<Point2D> rightPoint = data.stream().max(Point2D::getX);
//    Optional<Point2D> bottomPoint = data.stream().min(Point2D::getY);
//    Optional<Point2D> topPoint = data.stream().max(Point2D::getY);
    if (data.size() < 2) {
      minCircle = Optional.empty();
    } else if (data.size() == 2) {
      minCircle = Optional.of(
          Circle.ofDiameterPoints(data.get(0), data.get(1)));
    } else {
      System.out.println("Streaming brute force...");
      minCircle = Combinations.kFrom(3, data)
//          .map(Triangle::of)
//          .filter(Triangle::isAcute)
          .map(Circle::of)
          .filter(c -> c.containsAll(data))
          .min(Comparator.comparing(Circle::getRadius));
      System.out.println("Streaming brute force completed, circle is " + minCircle.get());
    }
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
      for (int i = 0; i < pointCount; i++) {
        Point2D p = data.get(i);
        sumSquaredDiffs += getHypSquared(p, mean);
      }
      sd = Math.sqrt(sumSquaredDiffs / pointCount);
    }
    return sd;
  }

  public double getMoaSD() {
    double sd = getSD();
    double sdInches = sd / pixelScale;
    double inches = getRangeInInches();
    double tanTheta = sdInches / inches;
    double angleRads = Math.atan(tanTheta);
    double angleDegrees = 180 * angleRads / Math.PI;
    double angleMinutes = 60 * angleDegrees;
    return angleMinutes;
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
