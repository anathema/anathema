package net.sf.anathema.framework.repository.tree;

import javax.swing.Icon;

import net.disy.commons.swing.ui.IObjectUi;
import net.sf.anathema.framework.item.IItemType;
import net.sf.anathema.framework.presenter.action.ItemTypeUi;
import net.sf.anathema.framework.presenter.item.ItemTypeCreationViewPropertiesExtensionPoint;
import net.sf.anathema.framework.view.PrintNameFile;
import net.sf.anathema.lib.resources.IResources;

public class ItemTypeTreeUi implements IObjectUi<Object> {

  private final ItemTypeCreationViewPropertiesExtensionPoint extension;
  private final IObjectUi<Object> itemtypeUi;

  public ItemTypeTreeUi(IResources resources, ItemTypeCreationViewPropertiesExtensionPoint extension) {
    this.extension = extension;
    this.itemtypeUi = new ItemTypeUi(resources, extension);
  }

  public Icon getIcon(Object value) {
    if (value instanceof IItemType) {
      return itemtypeUi.getIcon(value);
    }
    if (value instanceof PrintNameFile) {
      return extension.get(((PrintNameFile) value).getItemType()).getItemTypeUI().getIcon(value);
    }
    return null;
  }

  public String getLabel(Object value) {
    if (value instanceof IItemType) {
      return itemtypeUi.getLabel(value);
    }
    if (value instanceof PrintNameFile) {
      return extension.get(((PrintNameFile) value).getItemType()).getItemTypeUI().getLabel(value);
    }
    return value.toString();
  }

  @Override
  public String getToolTipText(Object value) {
    return null;
  }
}