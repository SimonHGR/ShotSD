package shotsd;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

public final class ControlPanel extends JPanel {
  private final UIMediator mediator;
  private final double SCALE_INCREMENT = 0.02;
  private final JTextField messageBox = new JTextField();
  private final JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
  private final JButton newGroupButton = new JButton("New Group");
  
  private final JPanel groupPanel = new JPanel();
  private int groupCount = 0;
  private final JScrollPane groupScroller = new JScrollPane(groupPanel);
  
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
    
    cons.gridy++; cons.gridwidth = 3;
    add(newGroupButton, cons);

    cons.gridy++; cons.weighty = 1.0;
    add(groupScroller, cons);
    groupPanel.setLayout(new GridBagLayout());
  }
  
  public double getScale() {
    double scale = SCALE_INCREMENT * scaleSlider.getValue();
//    System.out.println("calculated scale is " + scale);
    return scale;
  }

  public void setMessage(String message) {
    messageBox.setText(message);
  }
  
  public void addNewGroupButtonHandler(ActionListener l) {
    newGroupButton.setEnabled(true);
    newGroupButton.addActionListener(l);
  }
  
  public void addNewGroup(GroupUI groupUI) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = groupCount++;
    groupPanel.add(groupUI, gbc);
    revalidate();
    repaint();
  }
}
