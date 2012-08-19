package net.sf.anathema.platform.tree.view.interaction;

import net.sf.anathema.platform.svgtree.presenter.view.ToolTipProperties;
import net.sf.anathema.platform.tree.view.PolygonPanel;
import net.sf.anathema.platform.tree.view.container.Cascade;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ToolTipListener extends MouseMotionAdapter {
  private final PolygonPanel polygonPanel;
  private final SetToolTipWithDetails setToolTip;

  public ToolTipListener(ToolTipProperties properties, PolygonPanel polygonPanel, Cascade cascade) {
    this.polygonPanel = polygonPanel;
    this.setToolTip = new SetToolTipWithDetails(cascade, properties, polygonPanel);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    polygonPanel.onElementAtPoint(e.getPoint()).perform(setToolTip).orFallBackTo(new NoTooltip());
  }

  private class NoTooltip implements Runnable {
    @Override
    public void run() {
      polygonPanel.setToolTipText(null);
    }
  }
}
