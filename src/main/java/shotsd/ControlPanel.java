package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;

public final class ControlPanel extends JPanel {
  private ShotUI ui;
  private final double SCALE_INCREMENT = 0.02;
  
  private final JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
  
  public ControlPanel(ShotUI ui) {
    this.ui = ui;
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 1; cons.gridy = 1; cons.fill = GridBagConstraints.BOTH;
    cons.gridwidth = GridBagConstraints.REMAINDER;
    add(scaleSlider, cons);
    scaleSlider.addChangeListener(e -> ui.setScale(getScale()));
  }
  
  public double getScale() {
    double scale = SCALE_INCREMENT * scaleSlider.getValue();
    System.out.println("calculated scale is " + scale);
    return scale;
  }
}
