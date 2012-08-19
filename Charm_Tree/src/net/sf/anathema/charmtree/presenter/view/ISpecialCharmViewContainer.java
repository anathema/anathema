package net.sf.anathema.charmtree.presenter.view;

import net.sf.anathema.platform.svgtree.presenter.view.ISpecialNodeView;

public interface ISpecialCharmViewContainer {

  void setSpecialCharmViewVisible(ISpecialNodeView charmView, boolean visible);

  void hideSpecialCharmViews();
}