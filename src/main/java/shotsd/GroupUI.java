package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

public final class GroupUI extends JPanel {

  private final PointCollection pointCollection;
  
  private final JLabel nameField = new JLabel();
  private final JTextArea descriptionField = new JTextArea(5, 30);
  private final JTextField rangeInput = new JTextField("25");
  private final JComboBox<UnitItem> rangeUnits = new UnitsComboBox();
  private final JTextField count = new JTextField();
  private final JTextField standardDeviation = new JTextField();
  private final JTextField groupSizeInches = new JTextField();

  public GroupUI(PointCollection pointCollection) {
    this.pointCollection = pointCollection;
    updateRangeInfo();
    
    setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0; cons.gridy = 0; 
    cons.fill = GridBagConstraints.BOTH;

    add(new JLabel("Group ID"), cons);
    nameField.setText(pointCollection.getGroupName());
    ++cons.gridx; cons.weightx = 1.0;
    add(nameField, cons);
    ++cons.gridx; cons.weightx = 0;

    // range input
    add(new JLabel("Range"), cons);
    ++cons.gridx; cons.weightx = 1.0;
    add(rangeInput, cons);
    ++cons.gridx; cons.weightx = 0;
    add(rangeUnits, cons);
    rangeInput.addActionListener(e->{updateRangeInfo(); update();});

    // description
    cons.gridx = 0; ++cons.gridy; 
    cons.gridwidth = GridBagConstraints.REMAINDER; cons.gridheight = 5;
    add(descriptionField, cons);
    descriptionField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        pointCollection.setDescription(descriptionField.getText());
      } 
    });
    cons.gridy += 5; cons.gridheight = 1;
    
    // count output
//    ++cons.gridy; 
    cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Count"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(count, cons);

    // spread output
    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Spread (SD MOA)"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(standardDeviation, cons);

    ++cons.gridy; cons.gridx = 0; cons.gridwidth = 1;
    add(new JLabel("Group radius (inch)"), cons);
    ++cons.gridx; cons.gridwidth = GridBagConstraints.REMAINDER;
    add(groupSizeInches, cons);
    
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
    groupSizeInches.setText(String.format("%7.2f", pointCollection.getGroupSizeInches()));
  }
}
