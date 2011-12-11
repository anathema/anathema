package net.sf.anathema.character.generic.framework.magic.stringbuilder.type;

import net.sf.anathema.character.generic.framework.magic.stringbuilder.ICharmTypeStringBuilder;
import net.sf.anathema.character.generic.impl.magic.charm.type.CharmTypeModel;
import net.sf.anathema.character.generic.impl.magic.charm.type.ReflexiveSpecialsModel;
import net.sf.anathema.character.generic.impl.magic.charm.type.SimpleSpecialsModel;
import net.sf.anathema.character.generic.magic.charms.type.CharmType;
import net.sf.anathema.character.generic.magic.charms.type.TurnType;
import net.sf.anathema.lib.dummy.DummyResources;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortCharmTypeStringBuilderTest extends AbstractCharmTypeStringBuilderTest {

  private ShortCharmTypeStringBuilder builder;

  @Before
  public void setUp() throws Exception {
    DummyResources resources = new DummyResources();
    resources.putString("CharmTreeView.ToolTip.Type", "Type"); //$NON-NLS-1$ //$NON-NLS-2$
    resources.putString("ExtraAction", "Extra Action"); //$NON-NLS-1$ //$NON-NLS-2$
    resources.putString("Simple", "Simple"); //$NON-NLS-1$ //$NON-NLS-2$     
    resources.putString("Supplemental", "Supplemental"); //$NON-NLS-1$ //$NON-NLS-2$     
    resources.putString("Reflexive", "Reflexive"); //$NON-NLS-1$ //$NON-NLS-2$     
    resources.putString("CharmTreeView.ToolTip.Type.LongTick.Short", "LT"); //$NON-NLS-1$ //$NON-NLS-2$
    resources.putString("CharmTreeView.ToolTip.Type.DramaticAction.Short", "DA"); //$NON-NLS-1$ //$NON-NLS-2$
    resources.putString("CharmTreeView.ToolTip.Type.SingleStep.Short", "{0, number, integer}"); //$NON-NLS-1$ //$NON-NLS-2$
    resources.putString("CharmTreeView.ToolTip.Type.DualStep.Short", "{0, number, integer}/{1, number, integer}"); //$NON-NLS-1$ //$NON-NLS-2$
    builder = new ShortCharmTypeStringBuilder(resources);
  }

  @Override
  protected ICharmTypeStringBuilder getBuilder() {
    return builder;
  }

  @Test
  public void testReflexiveModelShortDoubleStep() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Reflexive);
    charmTypeModel.setSpecialModel(new ReflexiveSpecialsModel(1, 2));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Reflexive (1/2)", string); //$NON-NLS-1$
  }

  @Test
  public void testReflexiveModelShortSingleStep() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Reflexive);
    charmTypeModel.setSpecialModel(new ReflexiveSpecialsModel(4, null));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Reflexive (4)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelTickDefaultDefense() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(4, TurnType.Tick, -1));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (4, -1)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelTickDefaultSpeed() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(6, TurnType.Tick, -0));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (6, -0)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelTick() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(3, TurnType.Tick, -2));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (3, -2)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelLongTick() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(3, TurnType.LongTick, -1));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (3 LT, -1)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelDefaultLongTick() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(6, TurnType.LongTick, -1));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (6 LT, -1)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelDramaticActionDefaultDefense() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(3, TurnType.DramaticAction, -1));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (DA, -1)", string); //$NON-NLS-1$
  }

  @Test
  public void testSimpleModelDramaticAction() throws Exception {
    final CharmTypeModel charmTypeModel = new CharmTypeModel();
    charmTypeModel.setCharmType(CharmType.Simple);
    charmTypeModel.setSpecialModel(new SimpleSpecialsModel(3, TurnType.DramaticAction, -6));
    String string = builder.createTypeString(charmTypeModel);
    assertEquals("Simple (DA, -6)", string); //$NON-NLS-1$
  }
}