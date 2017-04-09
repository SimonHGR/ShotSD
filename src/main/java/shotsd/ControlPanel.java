package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public final class ControlPanel extends JPanel {
  private ShotUI ui;
  private final double SCALE_INCREMENT = 0.02;
  private final JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
  private final JTextField rangeInput = new JTextField(25);
  private final JComboBox rangeUnits = new UnitsComboBox();
  
  public ControlPanel(ShotUI ui) {
    this.ui = ui;
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 1; cons.gridy = 1; cons.fill = GridBagConstraints.HORIZONTAL;
    cons.gridwidth = GridBagConstraints.REMAINDER;
    add(scaleSlider, cons);
    scaleSlider.addChangeListener(e -> ui.setScale(getScale()));
    
    // range input
    rangeInput.setText("25");
    cons.gridx = 1; ++cons.gridy; cons.gridwidth = 1;
    add(new JLabel("Range"));
    ++cons.gridx;
    add(rangeInput);
    ++cons.gridx;
    add(rangeUnits);
    
    
  }
  
  public double getScale() {
    double scale = SCALE_INCREMENT * scaleSlider.getValue();
//    System.out.println("calculated scale is " + scale);
    return scale;
  }
}
