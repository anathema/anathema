package net.sf.anathema.framework.view.toolbar;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.lib.gui.IView;

public class AnathemaToolBar implements IAnathemaToolbar, IView {

  private JToolBar toolBar;

  public AnathemaToolBar() {
    this.toolBar = new JToolBar();
    this.toolBar.setFloatable(false);
    this.toolBar.setRollover(true);
  }

  public JToolBar getComponent() {
    return toolBar;
  }

  public void addSeparator() {
    toolBar.addSeparator();
  }

  public void addTools(Action... toolBarActions) {
    for (Action action : toolBarActions) {
      addComponent(new ToolBarButton(), action);
    }
  }

  private void addComponent(JButton button, Action action) {
    button.setAction(action);
    toolBar.add(button);
    setButtonSize(button);
  }

  private void setButtonSize(JButton button) {
    Dimension dimension = button.getPreferredSize();
    button.setMinimumSize(dimension);
    button.setSize(dimension);
  }

  public void addMenu(Icon buttonIcon, Action[] menuActions, String toolTip) {
    final JPopupMenu menu = new JPopupMenu();
    for (Action action : menuActions) {
      menu.add(action);
    }
    final ToolBarButton button = new ToolBarButton();
    SmartAction action = new SmartAction(buttonIcon) {
      private static final long serialVersionUID = 5982837727535553981L;

	  @Override
      protected void execute(Component parentComponent) {
        menu.show(button, 0, button.getHeight());
      }
    };
    action.setToolTipText(toolTip);
    addComponent(button, action);
  }
}