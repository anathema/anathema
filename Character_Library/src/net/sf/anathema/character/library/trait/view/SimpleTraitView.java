package net.sf.anathema.character.library.trait.view;

import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IGridDialogLayoutData;
import net.sf.anathema.character.library.trait.IModifiableCapTrait;
import net.sf.anathema.framework.value.IntegerViewFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

public class SimpleTraitView extends AbstractTraitView implements ITraitView<SimpleTraitView> {

  private final JLabel label;
  private final Component displayComponent;
  private final GridAlignment dotAlignment;
  private final IGridDialogLayoutData labelAlignment;
  private JPanel traitViewPanel;

  public SimpleTraitView(IntegerViewFactory factory, String labelText, int value, int maxValue) {
    this(factory, labelText, value, maxValue, null, GridAlignment.END);
  }

  public SimpleTraitView(IntegerViewFactory factory, String labelText, int value, int maxValue,
                         IModifiableCapTrait trait) {
    this(factory, labelText, value, maxValue, trait, GridAlignment.END);
  }

  public SimpleTraitView(IntegerViewFactory factory, String labelText, int value, int maxValue,
                         GridAlignment dotAlignment) {
    this(factory, labelText, value, maxValue, null, dotAlignment, GridDialogLayoutData.FILL_HORIZONTAL);
  }

  public SimpleTraitView(IntegerViewFactory factory, String labelText, int value, int maxValue,
                         IModifiableCapTrait trait, GridAlignment dotAlignment) {
    this(factory, labelText, value, maxValue, trait, dotAlignment, GridDialogLayoutData.FILL_HORIZONTAL);
  }

  public SimpleTraitView(IntegerViewFactory factory, String labelText, int value, int maxValue,
                         IModifiableCapTrait trait, GridAlignment dotAlignment, IGridDialogLayoutData labelAlignment) {
    super(factory, labelText, value, maxValue, trait);
    this.label = getLabelText() != null ? new JLabel(getLabelText()) : null;
    this.displayComponent = maxValue > 0 ? getValueDisplay().getComponent() : null;
    this.dotAlignment = dotAlignment;
    this.labelAlignment = labelAlignment;
  }

  @Override
  public void addComponents(JPanel panel) {
    this.traitViewPanel = panel;
    if (label != null) {
      panel.add(label);
      if (labelAlignment != null) panel.add(label, labelAlignment);
      else panel.add(label);
    }
    GridDialogLayoutData data = new GridDialogLayoutData();
    data.setHorizontalAlignment(dotAlignment);
    if (displayComponent != null) {
      panel.add(displayComponent, data);
    }
  }

  @Override
  public void delete() {
    traitViewPanel.remove(label);
    traitViewPanel.remove(displayComponent);
    traitViewPanel.revalidate();
  }

  @Override
  public SimpleTraitView getInnerView() {
    return this;
  }
}