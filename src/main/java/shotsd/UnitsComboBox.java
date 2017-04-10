package shotsd;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

class UnitItem {

  String name;
  double multiplier;

  public UnitItem(String name, double multiplier) {
    this.name = name;
    this.multiplier = multiplier;
  }

  @Override
  public String toString() {
    return name;
  }
}

public class UnitsComboBox extends JComboBox<UnitItem> {

  private static final UnitItem[] ITEMS = {
    new UnitItem("Yards", 36),
    new UnitItem("Feet", 12),
    new UnitItem("Inches", 1),
    new UnitItem("Meters", 39.3701),};

  public UnitsComboBox() {
    super(new DefaultComboBoxModel<UnitItem>(ITEMS));
  }

  public double getMultiplier() {
    return ITEMS[this.getSelectedIndex()].multiplier;
  }
}
