package net.sf.anathema.graph.layering;

import net.sf.anathema.graph.nodes.IRegularNode;
import net.sf.anathema.graph.nodes.ISimpleNode;
import net.sf.anathema.graph.nodes.IdentifiedRegularNode;
import net.sf.anathema.graph.nodes.NodeFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LongestPathLayererTest {

  private LongestPathLayerer layerer;

  @Before
  public void setUp() throws Exception {
    layerer = new LongestPathLayerer();
  }

  @Test
  public void testLayerSingleChild() throws Exception {
    IRegularNode leaf = NodeFactory.createChildlessNode("leaf"); //$NON-NLS-1$
    IRegularNode root = new IdentifiedRegularNode("root", leaf); //$NON-NLS-1$
    IRegularNode[] acyclicGraph = new IRegularNode[]{leaf, root};
    int deepestLayer = layerer.layerGraph(acyclicGraph);
    assertEquals(2, deepestLayer);
    assertEquals(root.getLayer(), 1);
    assertEquals(leaf.getLayer(), 2);
  }

  @Test
  public void testLayerDualChild() throws Exception {
    IRegularNode leaf1 = NodeFactory.createChildlessNode("leaf1"); //$NON-NLS-1$
    IRegularNode leaf2 = NodeFactory.createChildlessNode("leaf2"); //$NON-NLS-1$
    IRegularNode root = new IdentifiedRegularNode("root", leaf1, leaf2); //$NON-NLS-1$
    IRegularNode[] acyclicGraph = new IRegularNode[]{leaf1, leaf2, root};
    int deepestLayer = layerer.layerGraph(acyclicGraph);
    assertEquals(2, deepestLayer);
    assertEquals(root.getLayer(), 1);
    assertEquals(leaf1.getLayer(), 2);
    assertEquals(leaf2.getLayer(), 2);
  }

  @Test
  @Ignore(value = "Apparently this optimization leads to questionable results.")
  public void testLayerDualRootDualChild() throws Exception {
    IRegularNode leaf1 = NodeFactory.createChildlessNode("leaf1"); //$NON-NLS-1$
    IRegularNode leaf2 = NodeFactory.createChildlessNode("leaf2"); //$NON-NLS-1$
    IRegularNode root1 = new IdentifiedRegularNode("root1", leaf1, leaf2); //$NON-NLS-1$
    IRegularNode root2 = new IdentifiedRegularNode("root2", leaf1, leaf2); //$NON-NLS-1$
    IRegularNode[] acyclicGraph = new IRegularNode[]{leaf1, leaf2, root1, root2};
    connect(acyclicGraph);
    int deepestLayer = layerer.layerGraph(acyclicGraph);
    assertEquals(3, deepestLayer);
    assertEquals(root1.getLayer(), 1);
    assertEquals(root2.getLayer(), 1);
    assertTrue(leaf1.getLayer() == 2 || leaf1.getLayer() == 3);
    assertTrue(leaf2.getLayer() == 2 || leaf2.getLayer() == 3);
    assertTrue(leaf1.getLayer() != leaf2.getLayer());
  }

  private void connect(IRegularNode[] acyclicGraph) {
    for (IRegularNode node : acyclicGraph) {
      ISimpleNode[] children = node.getChildren();
      for (ISimpleNode child : children) {
        child.addParent(node);
      }
    }
  }

  @Test
  public void testLayerDualRootTripleChild() throws Exception {
    IRegularNode leaf1 = NodeFactory.createChildlessNode("leaf1"); //$NON-NLS-1$
    IRegularNode leaf2 = NodeFactory.createChildlessNode("leaf2"); //$NON-NLS-1$
    IRegularNode leaf3 = NodeFactory.createChildlessNode("leaf3"); //$NON-NLS-1$
    IRegularNode root1 = new IdentifiedRegularNode("root1", leaf1, leaf2, leaf3); //$NON-NLS-1$
    IRegularNode root2 = new IdentifiedRegularNode("root2", leaf1, leaf2, leaf3); //$NON-NLS-1$
    IRegularNode[] acyclicGraph = new IRegularNode[]{leaf1, leaf2, leaf3, root1, root2};
    connect(acyclicGraph);
    int deepestLayer = layerer.layerGraph(acyclicGraph);
    assertEquals(2, deepestLayer);
    assertEquals(root1.getLayer(), 1);
    assertEquals(root2.getLayer(), 1);
    assertTrue(leaf1.getLayer() == 2);
    assertTrue(leaf2.getLayer() == 2);
    assertTrue(leaf3.getLayer() == 2);
  }
}