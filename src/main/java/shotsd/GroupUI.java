package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

public final class GroupUI extends JPanel {

  private final PointCollection pointCollection;
  private final double pixelScale;
  
  private final JTextField rangeInput = new JTextField("25");
  private final JComboBox<UnitItem> rangeUnits = new UnitsComboBox();
  private final JTextField count = new JTextField();
  private final JTextField standardDeviation = new JTextField();

  public GroupUI(PointCollection pointCollection, double pixelScale) {
    this.pointCollection = pointCollection;
    this.pixelScale = pixelScale;
    
    setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0; cons.gridy = 0; 
    cons.fill = GridBagConstraints.HORIZONTAL;

    // range input
    add(new JLabel("Range"), cons);
    ++cons.gridx; cons.weightx = 1.0;
    add(rangeInput, cons);
    ++cons.gridx; cons.weightx = 0;
    add(rangeUnits, cons);
    
    // count output
    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Count"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(count, cons);

    // spread output
    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1; cons.gridwidth = 1;
    add(new JLabel("Spread (SD)"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(standardDeviation, cons);
    
    pointCollection.addPropertyChangeListener(e -> update());
  }
  
  public void update() {
    count.setText("" + pointCollection.getPointCount());
    double sdPixels = pointCollection.getSD();
    double range = Double.parseDouble(rangeInput.getText()) 
        * ((UnitItem)(rangeUnits.getSelectedItem())).multiplier;
    System.out.println("range is " + range);
    double sdMoa = ((60*360/2/Math.PI)*Math.atan((sdPixels / pixelScale)/range));
    standardDeviation.setText(String.format("%7.2f", sdMoa));
  }
}
