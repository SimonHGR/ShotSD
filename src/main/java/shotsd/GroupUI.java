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
  
  private final JTextField nameField = new JTextField();
  private final JTextField rangeInput = new JTextField("25");
  private final JComboBox<UnitItem> rangeUnits = new UnitsComboBox();
  private final JTextField count = new JTextField();
//  private final JTextField standardDeviationPix = new JTextField();
  private final JTextField standardDeviation = new JTextField();

  public GroupUI(PointCollection pointCollection, String name) {
    this.pointCollection = pointCollection;
    pointCollection.setGroupName(name);
    updateRangeInfo();
    nameField.setText(name);
    nameField.addActionListener(e->pointCollection.setGroupName(nameField.getText()));
    
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
    rangeInput.addActionListener(e->{updateRangeInfo(); update();});
    
    // count output
    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Count"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(count, cons);

    // spread output
    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Spread (SD MOA)"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(standardDeviation, cons);
//
//    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
//    add(new JLabel("Spread (SD Pix)"), cons);
//    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
//    add(standardDeviationPix, cons);
    
    pointCollection.addPropertyChangeListener(e -> update());
  }
  
  public void updateRangeInfo() {
    UnitItem unitItem = ((UnitItem)(rangeUnits.getSelectedItem()));
    double range = Double.parseDouble(rangeInput.getText());
    pointCollection.setRangeInUnits(range);
    pointCollection.setRangeUnitName(unitItem.toString());
    pointCollection.setRangeMultiplier(unitItem.multiplier);
  }
  
  public void update() {
    count.setText("" + pointCollection.getPointCount());
    double sdPixels = pointCollection.getSD();
    standardDeviation.setText(String.format("%7.2f", pointCollection.getMoaSD()));
//    standardDeviationPix.setText(String.format("%5d", (int)pointCollection.getSD()));
  }
}
