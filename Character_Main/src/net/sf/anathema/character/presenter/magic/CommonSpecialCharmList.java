package net.sf.anathema.character.presenter.magic;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.charmtree.presenter.view.ISpecialCharmViewContainer;
import net.sf.anathema.platform.svgtree.presenter.view.ISpecialNodeView;

import java.util.ArrayList;
import java.util.List;

public class CommonSpecialCharmList implements SpecialCharmList {
  private final List<ISpecialNodeView> specialCharmViews = new ArrayList<ISpecialNodeView>();
  private final ISpecialCharmViewContainer container;
  private final SpecialCharmViewBuilder builder;
  private Predicate<String> visibilityPredicate = Predicates.alwaysFalse();

  public CommonSpecialCharmList(ISpecialCharmViewContainer container, SpecialCharmViewBuilder specialViewBuilder) {
    this.container = container;
    this.builder = specialViewBuilder;
  }

  @Override
  public void add(ISpecialCharm charm) {
    builder.reset();
    builder.buildFor(charm);
    if (builder.hasResult()) {
      ISpecialNodeView nodeView = builder.getResult();
      specialCharmViews.add(nodeView);
    }
  }

  @Override
  public void hideAllViews() {
    container.hideSpecialCharmViews();
  }

  @Override
  public void showViews() {
    for (ISpecialNodeView charmView : specialCharmViews) {
      boolean isVisible = isVisible(charmView);
      container.setSpecialCharmViewVisible(charmView, isVisible);
    }
  }

  @Override
  public void setVisibilityPredicate(Predicate<String> predicate) {
    this.visibilityPredicate = predicate;
  }

  private boolean isVisible(ISpecialNodeView charmView) {
    String nodeId = charmView.getNodeId();
    return visibilityPredicate.apply(nodeId);
  }
}