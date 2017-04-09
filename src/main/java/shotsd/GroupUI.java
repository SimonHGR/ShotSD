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
  
  private final JTextField rangeInput = new JTextField("25");
  private final JComboBox<UnitItem> rangeUnits = new UnitsComboBox();
  private final JTextField count = new JTextField();
  private final JTextField standardDeviation = new JTextField();

  public GroupUI(PointCollection pointCollection) {
    this.pointCollection = pointCollection;
    setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
    setLayout(new GridBagLayout());
    GridBagConstraints cons = new GridBagConstraints();
    cons.gridx = 0; cons.gridy = 0; 
    cons.fill = GridBagConstraints.HORIZONTAL;

    // range input
    add(new JLabel("Range"));
    ++cons.gridx;
    add(rangeInput);
    ++cons.gridx;
    add(rangeUnits);
    
    // count output
    ++cons.gridy; cons.gridx = 0;
    add(new JLabel("Count"));
    ++cons.gridx;
    add(count);

    // spread output
    ++cons.gridy; cons.gridx = 0;
    add(new JLabel("Spread (σ)"));
    ++cons.gridx;
    add(standardDeviation);
  }
}
