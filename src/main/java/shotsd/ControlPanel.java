package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public final class ControlPanel extends JPanel {
  private final UIMediator mediator;
  private final double SCALE_INCREMENT = 0.02;
  private final JTextField messageBox = new JTextField();
  private final JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
  private final JTextField rangeInput = new JTextField();
  private final JComboBox rangeUnits = new UnitsComboBox();
  
  public ControlPanel(UIMediator mediator) {
    this.mediator = mediator;
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0; cons.gridy = 0;
    cons.gridwidth = GridBagConstraints.REMAINDER;
    add(messageBox, cons);
    
    cons.gridx = 0; ++cons.gridy; cons.fill = GridBagConstraints.HORIZONTAL;
    add(scaleSlider, cons);
    scaleSlider.addChangeListener(e -> mediator.setScale(getScale()));
    
    // range input
    rangeInput.setText("25");
    cons.gridx = 0; ++cons.gridy; cons.gridwidth = 1;
    add(new JLabel("Range"));
    ++cons.gridx; cons.weightx = 1.0;
    add(rangeInput);
    ++cons.gridx; cons.weightx = 0;
    add(rangeUnits);
  }
  
  public double getScale() {
    double scale = SCALE_INCREMENT * scaleSlider.getValue();
//    System.out.println("calculated scale is " + scale);
    return scale;
  }

  void setMessage(String message) {
    messageBox.setText(message);
  }
}
