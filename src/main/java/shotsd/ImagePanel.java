package shotsd;

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
    int x = (int) (p.getX() - 10);
    int y = (int) (p.getY() - 10);
    int w = 20;
    int h = 20;
    g.setColor(Color.red);
    g.drawOval(x, y, w, h);
    g.drawString(groupName, x+2, y+18);
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
    for (PointCollection pointCollection : pointCollections) {
      for (int i = 0; i < pointCollection.getPointCount(); i++) {
        fillDot(g2d, pointCollection.getPoint(i), pointCollection.getGroupName());
      }

      // crosshairs for mean point
      g2d.setColor(Color.BLUE);
      Point2D meanPoint = pointCollection.getMean();
      int mX = (int) meanPoint.getX();
      int mY = (int) meanPoint.getY();
      g2d.drawLine(mX, 0, mX, 3000);
      g2d.drawLine(0, mY, 3000, mY);

      // circles for standard deviation
      g2d.setColor(Color.ORANGE);
      int sd = (int) pointCollection.getSD();
      g2d.drawOval(mX - sd, mY - sd, 2*sd, 2*sd);
      g2d.drawOval(mX - 2*sd, mY - 2*sd, 4*sd, 4*sd);
      g2d.drawOval(mX - 3*sd, mY - 3*sd, 6*sd, 6*sd);
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
