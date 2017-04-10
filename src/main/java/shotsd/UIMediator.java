package shotsd;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Deque;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class UIMediator {

  private final int RULER_LENGTH = 12; // should be reading!

  private Deque<PointCollection> pointCollections;
  private ShotUI ui;
  private ImagePanel imagePanel;
  private double pixelScale = -1;
  private double scaleFactor = 1;
  private ControlPanel controlPanel;
  private Point rulerStart;

  private ActionListener groupStarter = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      // create group sub-UI in control panel
      controlPanel.setMessage("Enter range and click on shots");
      imagePanel.addMouseListener(shotRecorder);
      PointCollection pointCollection = new PointCollection();
      pointCollections.push(pointCollection);
      GroupUI groupUI = new GroupUI(pointCollection, pixelScale);
      controlPanel.addNewGroup(groupUI);
    }
  };

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
      pixelScale = Math.sqrt(deltaX + deltaY) / RULER_LENGTH;
      System.out.println("Pixels per inch: " + pixelScale);
      imagePanel.removeMouseListener(this);
      controlPanel.addNewGroupButtonHandler(groupStarter);
      groupStarter.actionPerformed(new ActionEvent(e.getSource(), 0, null));
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
    scaleFactor = scale;
    imagePanel.setScale(scale);
  }

  void printResults() {
    final Dimension imageBounds = imagePanel.getRawSize();
    final double iRatio = ((double) imageBounds.width) / imageBounds.height;
    System.out.println("iRatio = " + iRatio);

    PrinterJob job = PrinterJob.getPrinterJob();
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    PageFormat pf = job.pageDialog(aset);

    job.setPrintable(new Printable() {
      final int FONT_SIZE = 16;

      public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2D = (Graphics2D)graphics;
        AffineTransform transform = new AffineTransform(pageFormat.getMatrix());
        // these units are 1/72"
        double offsetX = pageFormat.getImageableX();
        double offsetY = pageFormat.getImageableY();
        transform.translate(offsetX, offsetY);
        if (pageIndex == 0) {
          double usefulWidth = pageFormat.getImageableWidth();
          double usefulHeight = pageFormat.getImageableHeight();

          double pageRatio = usefulWidth / usefulHeight;
          System.out.printf("page tl: %6.2f , %6.2f print area: %6.2f , %6.2f\n",
              offsetX, offsetY, usefulWidth, usefulHeight);
          double imageRatio = iRatio;
          double imageW = imageBounds.width;
          double imageH = imageBounds.height;
          if (Math.signum(1 - pageRatio) != Math.signum(1 - imageRatio)) {
            System.out.println("Rotating image??");
            transform.translate(usefulWidth, 0);
            transform.rotate(Math.PI / 2);
            imageRatio = 1.0 / imageRatio;
            double temp = imageW;
            imageW = imageH;
            imageH = temp;
          }

          double scale = 1.0;
          if (imageRatio > pageRatio) { // scale "width"
            scale = usefulWidth / imageW;
          } else { // scale height
            scale = usefulHeight / imageH;
          }
          transform.scale(scale, scale);
          imagePanel.paintComponent((Graphics2D) g2D, transform);
          return Printable.PAGE_EXISTS;
        } else if (pageIndex == 1) {
          g2D.transform(transform);
          // statistics
          int vOff = 0;
          Font f = new Font("Sans", Font.PLAIN, FONT_SIZE);
          int groupNumber = 0;
          for (PointCollection pc : pointCollections) {
            String message = "Shot group " + (++groupNumber);
            g2D.drawString(message, 0, vOff);
            vOff += FONT_SIZE + 2;
            message = "Range ";
            g2D.drawString(message, 0, vOff);
            vOff += FONT_SIZE + 2;
            message = "Standard Deviation (MOA) " + pc.getSD();
            g2D.drawString(message, 0, vOff);
            vOff += FONT_SIZE + 2;
            message = " --------------------------------";
            g2D.drawString(message, 0, vOff);
            vOff += FONT_SIZE + 2;
          }
          return Printable.PAGE_EXISTS;
        } else {
          return Printable.NO_SUCH_PAGE;
        }
      }
    }, pf);
    boolean ok = job.printDialog(aset);
    if (ok) {
      try {
        job.print();
      } catch (PrinterException e1) {
        e1.printStackTrace();
      }
    }
  }
}
