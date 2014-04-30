package net.sf.anathema.character.equipment.creation.view.swing;

import net.sf.anathema.character.equipment.creation.presenter.EquipmentStatsView;
import net.sf.anathema.character.equipment.creation.presenter.IIntegerSpinner;
import net.sf.anathema.framework.view.SwingApplicationFrame;
import net.sf.anathema.interaction.ToggleTool;
import net.sf.anathema.lib.control.ChangeListener;
import net.sf.anathema.lib.gui.AgnosticUIConfiguration;
import net.sf.anathema.lib.gui.dialog.userdialog.OperationResultHandler;
import net.sf.anathema.lib.gui.dialog.userdialog.UserDialog;
import net.sf.anathema.lib.gui.dialog.userdialog.page.AbstractDialogPage;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;
import net.sf.anathema.lib.message.IBasicMessage;
import net.sf.anathema.lib.workflow.booleanvalue.IBooleanValueView;
import net.sf.anathema.lib.workflow.textualdescription.ITextView;
import org.jmock.example.announcer.Announcer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class SwingEditStatsView extends AbstractDialogPage implements EquipmentStatsView {

  private ExtensibleEquipmentStatsView view = new ExtensibleEquipmentStatsView();
  private final Announcer<ChangeListener> announcer = Announcer.to(ChangeListener.class);
  private boolean canCurrentlyFinish;
  private IBasicMessage message;
  private String title;
  private String description;

  public SwingEditStatsView() {
    super("");
  }

  @Override
  public IBasicMessage createCurrentMessage() {
    return message;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public final String getDescription() {
    return description;
  }

  @Override
  public boolean canFinish() {
    return canCurrentlyFinish;
  }

  @Override
  public final JComponent createContent() {
    return view.getComponent();
  }

  @Override
  public void setInputValidListener(ChangeListener inputValidListener) {
    announcer.addListener(inputValidListener);
    refreshFinishingState();
  }

  public ITextView addLineTextView(String nameLabel) {
    return view.addLineTextView(nameLabel);
  }

  public void setCanFinish() {
    this.canCurrentlyFinish = true;
    refreshFinishingState();
  }

  public void setCannotFinish() {
    this.canCurrentlyFinish = false;
    refreshFinishingState();
  }

  @Override
  public void setMessage(IBasicMessage message) {
    this.message = message;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void show(OperationResultHandler handler) {
    SwingUtilities.invokeLater(() -> {
      UserDialog dialog = new UserDialog(SwingApplicationFrame.getParentComponent(), this);
      dialog.show(handler);
    });
  }

  @Override
  public void addHorizontalSeparator() {
    view.addHorizontalSeparator();
  }

  @Override
  public ToggleTool addToggleTool() {
    return view.addToggleTool();
  }

  @Override
  public <T> ObjectSelectionView<T> addObjectSelection(AgnosticUIConfiguration<T> agnosticUIConfiguration) {
    return view.addObjectSelection(agnosticUIConfiguration);
  }

  @Override
  public IIntegerSpinner addIntegerSpinner(String label, int initialValue) {
    return view.addIntegerSpinner(label, initialValue);
  }

  @Override
  public IBooleanValueView addBooleanSelector(String label) {
    return view.addBooleanSelector(label);
  }

  private void refreshFinishingState() {
    announcer.announce().changeOccurred();
  }
}