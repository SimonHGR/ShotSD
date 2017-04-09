package shotsd;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ShotUI extends JPanel {

  private JScrollPane scroller = new JScrollPane();
  private ControlPanel controlPanel;

  public ShotUI(JComponent imagePanel, ControlPanel controlPanel) {
    this.setLayout(new BorderLayout());
    scroller.setViewportView(imagePanel);
    this.add(scroller, BorderLayout.CENTER);
    this.add(controlPanel, BorderLayout.EAST);
  }
}
