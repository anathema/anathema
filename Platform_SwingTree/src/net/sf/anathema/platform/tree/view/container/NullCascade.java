package net.sf.anathema.platform.tree.view.container;

import net.sf.anathema.platform.svgtree.presenter.view.NodeProperties;
import net.sf.anathema.platform.tree.view.PolygonPanel;
import net.sf.anathema.platform.tree.view.interaction.SpecialControl;

import java.awt.Color;

public class NullCascade implements Cascade {
  @Override
  public void colorNode(String nodeId, Color fillColor) {
    //nothing to do
  }

  @Override
  public void setNodeAlpha(String nodeId, int alpha) {
    //nothing to do
  }

  @Override
  public void addTo(PolygonPanel panel) {
    //nothing to do
  }

  @Override
  public void addToggleListener(NodeToggleListener listener) {
    //nothing to do
  }

  @Override
  public void removeToggleListener(NodeToggleListener listener) {
    //nothing to do
  }

  @Override
  public void initNodeNames(NodeProperties properties) {
    //nothing to do
  }

  @Override
  public void determinePositionFor(String nodeId, SpecialControl control) {
    //Nothing to do
  }
}