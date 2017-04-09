package shotsd;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ImageLoader {
  public static void main(String[] args) throws Throwable {
    UIMediator mediator = new UIMediator();
    Deque<PointCollection> pointCollections = new ArrayDeque<>();
    
    BufferedImage image = ImageIO.read(new File("image.jpg"));
    System.out.println("Image loaded: " + image);
    
    ControlPanel controlPanel = new ControlPanel(mediator);
    ImagePanel imagePanel = new ImagePanel(image, 1.0, pointCollections);
    ShotUI ui = new ShotUI(imagePanel, controlPanel);
    mediator.addParticipants(pointCollections, ui, imagePanel, controlPanel);
    JFrame frame = new JFrame("ShotSD");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(ui, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }
}
