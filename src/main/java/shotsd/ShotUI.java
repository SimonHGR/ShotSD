package shotsd;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class ShotUI extends JSplitPane {

  private JScrollPane scroller = new JScrollPane();
  private ControlPanel controlPanel;

  public ShotUI(JComponent imagePanel, ControlPanel controlPanel) {
    super(JSplitPane.HORIZONTAL_SPLIT, true);
    scroller.setViewportView(imagePanel);
    this.add(scroller);
    this.add(controlPanel);
    this.setResizeWeight(0.7);
  }
}
