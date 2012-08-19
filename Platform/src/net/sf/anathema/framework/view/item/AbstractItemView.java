package net.sf.anathema.framework.view.item;

import com.google.common.base.Objects;
import net.sf.anathema.framework.view.IItemView;
import net.sf.anathema.lib.control.ObjectValueListener;
import org.jmock.example.announcer.Announcer;

import javax.swing.Icon;

public abstract class AbstractItemView implements IItemView {

  private String name;
  private final Announcer<ObjectValueListener> control = Announcer.to(ObjectValueListener.class);
  private final Icon icon;

  protected AbstractItemView(String name, Icon icon) {
    this.name = name;
    this.icon = icon;
  }
  
  @Override
  public final void setName(String name) {
    if (Objects.equal(name, this.name)) {
      return;
    }
    this.name = name;
    fireNameChangedEvent(name);
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final Icon getIcon() {
    return icon;
  }

  @Override
  public void addNameChangedListener(ObjectValueListener<String> nameListener) {
    control.addListener(nameListener);
  }

  @Override
  public void removeNameChangedListener(ObjectValueListener<String> nameListener) {
    control.removeListener(nameListener);
  }

  public void fireNameChangedEvent(String newName) {
    control.announce().valueChanged(newName);
  }
}