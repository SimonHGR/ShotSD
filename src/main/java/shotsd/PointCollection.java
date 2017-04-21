package shotsd;

import combinations.Combinations;
import geometry.Circle;
import java.awt.Color;
import java.util.List;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

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
  private String description;

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
    if (!this.minCircle.isPresent()
        || !minCircle.get().contains(p)) {
      this.minCircle = computeMinCircle();
    }
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

  public Stream<Circle> getCircles() {
    Stream<Circle> triples = Combinations.kFrom(3, data)
        .map(Circle::of);
    Stream<Circle> diameters = Combinations.kFrom(2, data)
        .map(Circle::ofDiameterPoints);
    return Stream.concat(triples, diameters);
  }

  private Optional<Circle> computeMinCircle() {
    System.out.println("Computing minCircle");
    if (data.size() < 2) {
      return Optional.empty();
    } else if (data.size() == 2) {
      return Optional.of(
          Circle.ofDiameterPoints(data.get(0), data.get(1)));
    } else {
      return getCircles()
          .filter(c -> c.containsAll(data))
          .min(Comparator.comparing(Circle::getRadius));
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

  public double inchesToMoa(double inch) {
    double inchesRange = getRangeInInches();
    double tanTheta = inch / inchesRange;
    double angleRads = Math.atan(tanTheta);
    double angleDegrees = 180 * angleRads / Math.PI;
    double angleMinutes = 60 * angleDegrees;
    return angleMinutes;
  }
  
  public double getMoaSD() {
    double sd = getSD();
    double sdInches = sd / pixelScale;
    return inchesToMoa(sdInches);
  }

  public double getGroupSizeInches() {
    return minCircle
        .map(Circle::getRadius)
        .map(r->r/pixelScale)
        .orElse(0.0);
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners() {
    for (PropertyChangeListener l : listeners) {
      l.propertyChange(new PropertyChangeEvent(this, "data", null, null));
    }
  }

  private void dumpDataSet() {
    for (Point2D p : data) {
      System.out.printf("new Point2D.Double(%12.9f, %12.9f),\n", p.getX(), p.getY());
    }
  }

  public void setDescription(String text) {
    this.description = text;
  }
  
  public String getDescription() {
    return this.description;
  }
}
