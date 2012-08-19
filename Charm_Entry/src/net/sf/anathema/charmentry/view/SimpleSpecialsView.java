package net.sf.anathema.charmentry.view;

import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.sf.anathema.charmentry.presenter.view.ISimpleSpecialsView;
import net.sf.anathema.framework.presenter.view.ObjectSelectionIntValueView;
import net.sf.anathema.framework.value.IIntValueDisplay;
import net.sf.anathema.lib.gui.selection.IObjectSelectionView;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class SimpleSpecialsView implements ISimpleSpecialsView {
  private final JPanel content = new JPanel(new GridDialogLayout(1, false));

  @Override
  public JComponent getContent() {
    return content;
  }

  @Override
  public void requestFocus() {
    // Nothing to do
  }

  @Override
  public IIntValueDisplay addIntegerSelectionView(String typeLabel, int maximum) {
    ObjectSelectionIntValueView view = new ObjectSelectionIntValueView(
        typeLabel,
        new DefaultListCellRenderer(),
        maximum);
    content.add(view.getComponent());
    return view;
  }

  @Override
  public <V> IObjectSelectionView<V> addObjectSelectionView(String labelString, ListCellRenderer renderer, V[] objects) {
    ObjectSelectionView<V> view = new ObjectSelectionView<V>(labelString, renderer, objects);
    view.addTo(content, new GridDialogLayoutData());
    return view;
  }
}