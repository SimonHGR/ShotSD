package shotsd;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageLoader {
  public static void main(String[] args) throws Throwable {
    BufferedImage image = ImageIO.read(new File("image.jpg"));
    System.out.println("Image loaded: " + image);
    ShotUI ui = new ShotUI(image);
  }
}
