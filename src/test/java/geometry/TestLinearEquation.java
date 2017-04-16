package geometry;

import java.awt.geom.Point2D;
import org.junit.Assert;
import org.junit.Test;

public class TestLinearEquation {
  @Test
  public void test1() {
    LinearEquation e = LinearEquation.of(1, 0); // y = x
    Assert.assertEquals("y == x", 0, e.getY(0), 0.00001);
  }
  
  @Test
  public void test2() {
    LinearEquation e = LinearEquation.of(2, 1); // y = 2x + 1
    Assert.assertEquals("y == 2x + 1 @ x = 0", 1, e.getY(0), 0.00001);
  }
  
  @Test
  public void test3() {
    LinearEquation e = LinearEquation.of(2, 1); // y = 2x + 1
    Assert.assertEquals("y == 2x + 1 @ x = 3", 7, e.getY(3), 0.00001);
  }
  
  @Test
  public void test4() {
    LinearEquation e = LinearEquation.of(
        new Point2D.Double(3,7), new Point2D.Double(-2, -3)); // y = 2x + 1
    Assert.assertEquals("gradient", 2, e.getGradient(), 0.00001);
    Assert.assertEquals("constant", 1, e.getConstant(), 0.00001);
  }
  
  @Test
  public void test5() {
    LinearEquation regular = LinearEquation.of(2, 2);
    LinearEquation normal = regular.getPerpendicularEquationAtX(1);
    LinearEquation expected = LinearEquation.of(-0.5, 4.5);
    Assert.assertEquals("Perpendicular equation", expected, normal);
  }
  
  @Test
  public void test6() {
    LinearEquation regular = LinearEquation.of(2, 2);
    LinearEquation normal = regular.getPerpendicularEquationAtX(1);
    Assert.assertEquals("Y... ", 4, normal.getY(1), 0.00001);
    Assert.assertEquals("Intersection at 1, 4 ", 
        new Point2D.Double(1,4), 
        regular.getIntersection(normal));
  }
  
}
