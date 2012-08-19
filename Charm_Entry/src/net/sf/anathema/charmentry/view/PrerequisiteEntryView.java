package net.sf.anathema.charmentry.view;

import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.sf.anathema.character.generic.framework.intvalue.ISelectableIntValueView;
import net.sf.anathema.character.generic.framework.intvalue.SelectableIntValueView;
import net.sf.anathema.charmentry.presenter.view.IPrerequisitesEntryView;
import net.sf.anathema.framework.value.IIntValueDisplay;
import net.sf.anathema.framework.value.IIntValueView;
import net.sf.anathema.framework.value.IntegerViewFactory;
import net.sf.anathema.framework.value.NullUpperBounds;
import net.sf.anathema.lib.util.Identified;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrerequisiteEntryView implements IPrerequisitesEntryView {

  private final IntegerViewFactory factory;
  private final JPanel content = new JPanel(new GridDialogLayout(3, false));

  public PrerequisiteEntryView(IntegerViewFactory factory) {
    this.factory = factory;
  }

  @Override
  public ISelectableIntValueView<Identified> addSelectablePrerequisiteView(String string, Identified[] traits, int initial, int max) {
    content.add(new JLabel(string));
    ISelectableIntValueView<Identified> view = new SelectableIntValueView<Identified>(factory, initial, max);
    view.setSelectableValues(traits);
    view.addTo(content);
    return view;
  }

  @Override
  public IIntValueView addEssencePrerequisiteView(String label, int minimum, int maximum) {
    content.add(new JLabel(label));
    content.add(new JLabel());
    IIntValueDisplay display = factory.createIntValueDisplay(maximum, minimum, new NullUpperBounds());
    content.add(display.getComponent());
    return display;
  }

  @Override
  public JComponent getContent() {
    return content;
  }

  @Override
  public void requestFocus() {
    // Nothing to do
  }
}