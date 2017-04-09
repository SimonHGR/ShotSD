package shotsd;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Deque;

public class UIMediator {

  private Deque<PointCollection> pointCollections;
  private ShotUI ui;
  private ImagePanel imagePanel;
  private double pixelScale = -1;
  private double scaleFactor = 1;
  private ControlPanel controlPanel;
  private Point rulerStart;

  private MouseListener shotRecorder = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
      Point p = e.getPoint();
      Point2D.Double p2d = toImageScaledPoint2D(p);
      System.out.println("mouse clicked at " + p + " maps to " + p2d);
      pointCollections.getFirst().addPoint(p2d);
      imagePanel.repaint();
    }
  };

  private MouseListener scaleSetterStart = new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
      System.out.println("Ruler start at " + e.getPoint());
      rulerStart = e.getPoint();
      imagePanel.removeMouseListener(this);
      imagePanel.addMouseListener(scaleSetterEnd);
      controlPanel.setMessage("Click end of ruler");
    }
  };

  private MouseListener scaleSetterEnd = new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
      System.out.println("Ruler end at " + e.getPoint());
      double deltaX = rulerStart.getX() - e.getX();
      deltaX *= deltaX;
      double deltaY = rulerStart.getY() - e.getY();
      deltaY *= deltaY;
      pixelScale = Math.sqrt(deltaX + deltaY) / 12;
      System.out.println("Pixels per inch: " + pixelScale);
      imagePanel.removeMouseListener(this);
      controlPanel.setMessage("");
      startGroup();
    }
    
    private void startGroup() {
      // create group sub-UI in control panel
    //  controlPanel.addGroup(new GroupUI());
      controlPanel.setMessage("Enter range for group");
      
      pointCollections.push(new PointCollection());
      // should be called from the GroupUI on initial info
      collectGroup();
    }
    private void collectGroup() {
      imagePanel.addMouseListener(shotRecorder);
      controlPanel.setMessage("Click on shots in this group");
    }
  };

  private Point2D.Double toImageScaledPoint2D(Point p) {
    Point2D.Double p2d = new Point2D.Double();
    AffineTransform.getScaleInstance(1 / scaleFactor, 1 / scaleFactor).transform(p, p2d);
    return p2d;
  }

  public UIMediator() {
  }

  public void addParticipants(Deque<PointCollection> pointCollections, ShotUI ui, ImagePanel imagePanel, ControlPanel controlPanel) {
    this.pointCollections = pointCollections;
    this.ui = ui;
    this.imagePanel = imagePanel;
    this.controlPanel = controlPanel;
    controlPanel.setMessage("Select ruler unitsClick ");
    imagePanel.addMouseListener(scaleSetterStart);
  }

  public void setScale(double scale) {
//    System.out.println("mediator set scale...");
    scaleFactor = scale;
    imagePanel.setScale(scale);
  }
}
