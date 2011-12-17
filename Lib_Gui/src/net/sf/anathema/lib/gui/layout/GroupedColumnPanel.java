package net.sf.anathema.lib.gui.layout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class GroupedColumnPanel {

  private final JPanel[] columns;
  private int columnIndex = -1;

  public GroupedColumnPanel(JComponent parent, int columnCount) {
    columns = new JPanel[columnCount];
    for (int i = 0; i < columns.length; i++) {
      columns[i] = new JPanel(new GridDialogLayout(2, false));
    }
    addOverallView(parent);
  }

  public void startNewGroup(String groupLabel) {
    increaseColumnIndex();
    if (groupLabel != null) {
      new SingleOverallComponent(new JLabel(groupLabel)).fillInto(getCurrentColumn(), 2);
    }
  }

  private void increaseColumnIndex() {
    columnIndex++;
    if (columnIndex >= columns.length) {
      columnIndex = 0;
    }
  }

  public JPanel getCurrentColumn() {
    return columns[columnIndex];
  }

  private void addOverallView(JComponent container) {
    GridDialogLayout layout = new GridDialogLayout(columns.length, false);
    layout.setHorizontalSpacing(15);
    container.setLayout(layout);
    GridDialogLayoutData data = GridDialogLayoutDataFactory.createTopData();
    for (JPanel column : columns) {
      container.add(column, data);
    }
  }
}