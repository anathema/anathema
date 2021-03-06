package net.sf.anathema.namegenerator;

import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.environment.Environment;
import net.sf.anathema.framework.environment.fx.UiEnvironment;
import net.sf.anathema.framework.view.perspective.Container;
import net.sf.anathema.framework.view.perspective.Perspective;
import net.sf.anathema.framework.view.perspective.PerspectiveAutoCollector;
import net.sf.anathema.framework.view.perspective.PerspectiveToggle;
import net.sf.anathema.framework.environment.dependencies.Weight;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.namegenerator.exalted.ExaltedNameGeneratorModel;
import net.sf.anathema.namegenerator.presenter.NameGeneratorPresenter;
import net.sf.anathema.namegenerator.presenter.model.INameGeneratorModel;
import net.sf.anathema.namegenerator.presenter.view.NameGeneratorView;
import net.sf.anathema.namegenerator.view.FxNameGeneratorView;

@PerspectiveAutoCollector
@Weight(weight = 7000)
public class NameGeneratorPerspective implements Perspective {

  @Override
  public void configureToggle(PerspectiveToggle toggle) {
    toggle.setTooltip("ItemType.NameGenerator.PrintName");
    toggle.setIcon(new RelativePath("icons/NameGeneratorPerspective.png"));
  }

  @Override
  public void initContent(Container container, IApplicationModel applicationModel, Environment environment, UiEnvironment uiEnvironment) {
    NameGeneratorView view = new FxNameGeneratorView();
    INameGeneratorModel generatorModel = new ExaltedNameGeneratorModel();
    new NameGeneratorPresenter(environment, view, generatorModel).initPresentation();
    container.setContent(view.getNode());
  }
}
