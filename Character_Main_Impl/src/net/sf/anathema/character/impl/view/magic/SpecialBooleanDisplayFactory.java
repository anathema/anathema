package net.sf.anathema.character.impl.view.magic;

import net.sf.anathema.lib.workflow.booleanvalue.IBooleanValueView;
import net.sf.anathema.platform.svgtree.presenter.view.ContentFactory;

public class SpecialBooleanDisplayFactory implements ContentFactory {

  @Override
  public IBooleanValueView create(Object... parameters) {
    String label = (String) parameters[0];
    return new CheckMenuItemView(label);
  }
}
