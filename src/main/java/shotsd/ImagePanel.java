package shotsd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Deque;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

  private BufferedImage image;
  private double scaleFactor;
  private AffineTransform scaleTransform
      = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
  private Deque<PointCollection> pointCollections;

  private void fillDot(Graphics2D g, Point2D p, String groupName) {
    Color color = g.getColor();
    int x = (int) (p.getX() - 10);
    int y = (int) (p.getY() - 10);
    int w = 20;
    int h = 20;
    g.drawOval(x, y, w, h);
    g.setColor(Color.BLACK);
    g.drawString(groupName, x+4, y+16);
    g.setColor(color);
  }

  public ImagePanel(BufferedImage image,
      double scaleFactor,
      Deque<PointCollection> pointCollections) {
    this.image = image;
    this.pointCollections = pointCollections;
    this.scaleFactor = scaleFactor;
    this.scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintComponent((Graphics2D) g, scaleTransform);
  }

  protected void paintComponent(Graphics2D g2d, AffineTransform transform) {
    if (transform != null) {
      g2d.transform(transform);
    }
    g2d.drawImage(image, 0, 0, null);
    g2d.setStroke(new BasicStroke(3));
    for (PointCollection pointCollection : pointCollections) {
      g2d.setColor(pointCollection.getGroupColor());

      for (int i = 0; i < pointCollection.getPointCount(); i++) {
        fillDot(g2d, pointCollection.getPoint(i), pointCollection.getGroupName());
      }

      Point2D meanPoint = pointCollection.getMean();
      int sd = (int) pointCollection.getSD();
      // crosshairs for mean point
      int mX = (int) meanPoint.getX();
      int mY = (int) meanPoint.getY();
      g2d.drawLine(mX, mY - 4*sd, mX, mY + 4*sd);
      g2d.drawLine(mX - 4*sd, mY, mX + 4*sd, mY);

      // circles for standard deviation
      g2d.drawOval(mX - sd, mY - sd, 2*sd, 2*sd);
      g2d.drawOval(mX - 2*sd, mY - 2*sd, 4*sd, 4*sd);
      g2d.drawOval(mX - 3*sd, mY - 3*sd, 6*sd, 6*sd);
      
      // Group circle
      g2d.setColor(Color.BLACK);
      pointCollection
          .getCircles()
          .forEach(c->g2d.drawOval(
              (int)(c.getCenter().getX()-c.getRadius()), 
              (int)(c.getCenter().getY()-c.getRadius()), 
              (int)(2*c.getRadius()), 
              (int)(2*c.getRadius())
              ));
//      // Group circle
//      g2d.setColor(Color.BLACK);
//      pointCollection
//          .getMinCircle()
//          .ifPresent(c->g2d.drawOval(
//              (int)(c.getCenter().getX()-c.getRadius()), 
//              (int)(c.getCenter().getY()-c.getRadius()), 
//              (int)(2*c.getRadius()), 
//              (int)(2*c.getRadius())
//              ));
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(
        (int) (image.getWidth() * scaleFactor),
        (int) (image.getHeight() * scaleFactor));
  }

  public Dimension getRawSize() {
    return new Dimension(image.getWidth(), image.getHeight());
  }

  public void setScale(double scale) {
    scaleFactor = scale;
    scaleTransform = AffineTransform.getScaleInstance(scale, scale);
    revalidate();
    repaint();
  }

  @Override
  public void revalidate() {
    super.revalidate();
//    System.out.println("revalidating image panel");
  }
}
