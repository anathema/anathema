package net.sf.anathema.character.presenter.magic;

import net.sf.anathema.character.library.trait.presenter.TraitPresenter;
import net.sf.anathema.character.model.charm.OxBodyCategory;
import net.sf.anathema.character.model.charm.special.IOxBodyTechniqueConfiguration;
import net.sf.anathema.framework.value.IIntValueView;
import net.sf.anathema.lib.gui.Presenter;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.platform.svgtree.presenter.view.CategorizedSpecialNodeView;

public class OxBodyTechniquePresenter implements Presenter {

  private final CategorizedSpecialNodeView view;
  private final IOxBodyTechniqueConfiguration model;
  private final IResources resources;

  public OxBodyTechniquePresenter(IResources resources, CategorizedSpecialNodeView view,
                                  IOxBodyTechniqueConfiguration model) {
    this.resources = resources;
    this.view = view;
    this.model = model;
  }

  @Override
  public void initPresentation() {
    for (OxBodyCategory category : model.getCategories()) {
      String label = resources.getString("OxBodyTechnique." + category.getId()); //$NON-NLS-1$
      IIntValueView display = view.addCategory(label, category.getMaximalValue(), category.getCurrentValue());
      new TraitPresenter(category, display).initPresentation();
    }
  }
}
