package geometry;

import java.awt.geom.Point2D;
import org.junit.Assert;
import org.junit.Test;

public class TestTriangle {
  @Test
  public void fromThreePoints() {
    Point2D.Double pointA = new Point2D.Double(0.5, 0);
    Point2D.Double pointB = new Point2D.Double(-0.5, 0);
    Point2D.Double pointC = new Point2D.Double(0, 0.866);
    
    Triangle t = Triangle.of(pointA, pointB, pointC);
    Assert.assertTrue("should be acute", t.isAcute());
    Assert.assertArrayEquals("should be PI/3", 
        new double[]{Math.PI/3, Math.PI/3, Math.PI/3}, 
        t.allAngles(), 
        0.0001);
  }

  @Test
  public void obtuse() {
    Point2D.Double pointA = new Point2D.Double(2, 0);
    Point2D.Double pointB = new Point2D.Double(1, 0);
    Point2D.Double pointC = new Point2D.Double(0, 1);
    
    Triangle t = Triangle.of(pointA, pointB, pointC);
    Assert.assertFalse("should be obtuse", t.isAcute());
  }

  @Test
  public void right() {
    Point2D.Double pointA = new Point2D.Double(0, 0);
    Point2D.Double pointB = new Point2D.Double(3, 0);
    Point2D.Double pointC = new Point2D.Double(3, 4);
    
    Triangle t = Triangle.of(pointA, pointB, pointC);
    Assert.assertFalse("should be obtuse", t.isAcute());
    Assert.assertEquals("should be right", t.allAngles()[1], Math.PI/2, 0.00001);
    Assert.assertEquals("should be 3,4,5", t.allSides()[1], 5, 0.00001);
  }
}
