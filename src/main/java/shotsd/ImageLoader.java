package shotsd;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ImageLoader {

  public static void main(String[] args) throws Throwable {
    JFrame frame = new JFrame("ShotSD");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    UIMediator mediator = new UIMediator();
    Deque<PointCollection> pointCollections = new ArrayDeque<>();

    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showOpenDialog(frame);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      BufferedImage image = ImageIO.read(file);
      System.out.println("Image loaded: " + image);

      ControlPanel controlPanel = new ControlPanel(mediator);
      ImagePanel imagePanel = new ImagePanel(image, 1.0, pointCollections);
      ShotUI ui = new ShotUI(imagePanel, controlPanel);
      mediator.addParticipants(pointCollections, ui, imagePanel, controlPanel);

      frame.add(ui, BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
    }
  }
}
