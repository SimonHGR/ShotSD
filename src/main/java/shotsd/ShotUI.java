package shotsd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class ShotUI extends JPanel {

  private JFrame frame = new JFrame("ShotSD");
//  private double scaleFactor = 0.3;
  private AffineTransform scaleTransform = null;

  private Panel imagePanel = new Panel() {
    {
      setOpaque(true);
    }

    private void fillDot(Graphics2D g, Point2D p) {
      int x = (int) (p.getX() - 10);
      int y = (int) (p.getY() - 10);
      int w = 20;
      int h = 20;
      g.setColor(Color.red);
      g.fillOval(x, y, w, h);
    }

    @Override
    public void paint(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setTransform(scaleTransform);
      g2d.drawImage(image, 0, 0, null);
      for (int i = 0; i < pointCollection.getPointCount(); i++) {
        fillDot(g2d, pointCollection.getPoint(i));
      }
      
      // crosshairs for mean point
      g2d.setColor(Color.BLUE);
      Point2D meanPoint = pointCollection.getMean();
      int mX = (int)meanPoint.getX();
      int mY = (int)meanPoint.getY();
      g2d.drawLine(mX, 0, mX, 3000);
      g2d.drawLine(0, mY, 3000, mY);
      
      // circle for standard deviation
      g2d.setColor(Color.ORANGE);
      int sd = (int)pointCollection.getSD();
      
      g2d.drawOval(mX - sd / 2, mY - sd / 2, sd, sd);
      g2d.drawOval(mX - sd, mY - sd, 2*sd, 2*sd);
      g2d.drawOval(mX - 3 * sd / 2, mY - 3 * sd / 2, 3 * sd, 3 * sd);
    }

    @Override
    public Dimension getPreferredSize() {
      double scaleFactor = controlPanel.getScale();
      return new Dimension(
          (int) (image.getWidth() * scaleFactor),
          (int) (image.getHeight() * scaleFactor));
    }
  };
  private JScrollPane scroller = new JScrollPane(imagePanel);
  private ControlPanel controlPanel = new ControlPanel(this);
  private BufferedImage image;
  private BufferedImage overlay;

  private PointCollection pointCollection = new PointCollection();

  private Point2D.Double asPoint2D(Point p) {
    Point2D.Double p2d = new Point2D.Double();
    try {
      scaleTransform.inverseTransform(p, p2d);
    } catch (NoninvertibleTransformException ex) {
      ex.printStackTrace();
    }
    return p2d;
  }

  public void setScale(double scaleFactor) {
    scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
    imagePanel.repaint();
  }

  public ShotUI(BufferedImage image) {
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.image = image;
    int maxDimension = Math.max(image.getWidth(), image.getHeight());
    setScale(controlPanel.getScale());
    imagePanel.addMouseListener(new MouseAdapter() {
      private Point dragStart;

      @Override
      public void mouseClicked(MouseEvent e) {
        dragStart = null;
        Point p = e.getPoint();
        Point2D.Double p2d = asPoint2D(p);
        System.out.println("mouse clicked at " + p + " maps to " + p2d);

        pointCollection.addPoint(p2d);
        imagePanel.repaint();
      }

      @Override
      public void mousePressed(MouseEvent e) {
        System.out.println("mouse press at " + e.getPoint());
        dragStart = e.getPoint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        System.out.println("mouse release at " + e.getPoint());
        pointCollection.setPixelScale(asPoint2D(dragStart), asPoint2D(e.getPoint()));
        dragStart = null;
      }
    });

    frame.add(scroller, BorderLayout.CENTER);
    frame.add(controlPanel, BorderLayout.EAST);
    frame.pack();
    frame.setVisible(true);
  }
}
