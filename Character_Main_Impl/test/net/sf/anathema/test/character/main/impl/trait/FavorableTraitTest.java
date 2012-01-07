package net.sf.anathema.test.character.main.impl.trait;

import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.dummy.DummyCasteType;
import net.sf.anathema.character.generic.dummy.DummyCharacterModelContext;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ITraitContext;
import net.sf.anathema.character.generic.impl.traits.SimpleTraitTemplate;
import net.sf.anathema.character.generic.traits.ITraitTemplate;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.character.impl.model.context.trait.CreationTraitValueStrategy;
import net.sf.anathema.character.impl.model.context.trait.ExperiencedTraitValueStrategy;
import net.sf.anathema.character.impl.model.context.trait.ProxyTraitValueStrategy;
import net.sf.anathema.character.library.trait.DefaultTrait;
import net.sf.anathema.character.library.trait.FriendlyValueChangeChecker;
import net.sf.anathema.character.library.trait.favorable.FavorableState;
import net.sf.anathema.character.library.trait.favorable.IFavorableStateChangedListener;
import net.sf.anathema.character.library.trait.favorable.IIncrementChecker;
import net.sf.anathema.character.library.trait.rules.FavorableTraitRules;
import net.sf.anathema.character.library.trait.specialties.DefaultTraitReference;
import net.sf.anathema.character.library.trait.specialties.SpecialtiesContainer;
import net.sf.anathema.character.library.trait.subtrait.ISubTrait;
import net.sf.anathema.character.library.trait.subtrait.ISubTraitContainer;
import net.sf.anathema.lib.control.intvalue.IIntValueChangedListener;
import net.sf.anathema.test.character.BasicCharacterTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FavorableTraitTest {

  private IIncrementChecker incrementChecker = Mockito.mock(IIncrementChecker.class);
  private IFavorableStateChangedListener abilityStateListener = Mockito.mock(IFavorableStateChangedListener.class);
  private ProxyTraitValueStrategy valueStrategy;
  private DefaultTrait trait;
  private DummyCharacterModelContext modelContext;

  @Before
  public void createTrait() throws Exception {
    this.valueStrategy = new ProxyTraitValueStrategy(new CreationTraitValueStrategy());
    this.modelContext = new BasicCharacterTestCase().createModelContextWithEssence2(valueStrategy);
    this.trait = createObjectUnderTest(modelContext);
  }

  @Test
  public void testSetAbilityToFavored() throws Exception {
    allowOneFavoredIncrement();
    trait.getFavorization().addFavorableStateChangedListener(abilityStateListener);
    assertEquals(0, trait.getCreationValue());
    trait.getFavorization().setFavorableState(FavorableState.Favored);
    verify(abilityStateListener).favorableStateChanged(FavorableState.Favored);
    assertEquals(1, trait.getCreationValue());
  }

  private void allowOneFavoredIncrement() {
    when(incrementChecker.isValidIncrement(1)).thenReturn(true);
  }

  @Test
  public void testSetAbiltyToFavoredUnallowed() throws Exception {
    when(incrementChecker.isValidIncrement(1)).thenReturn(false);
    trait.getFavorization().setFavorableState(FavorableState.Favored);
    assertSame(FavorableState.Default, trait.getFavorization().getFavorableState());
    assertEquals(0, trait.getCreationValue());
  }

  @Test
  public void testSetFavoredAbiltyCreationValueBelow1() throws Exception {
    allowOneFavoredIncrement();
    trait.getFavorization().setFavorableState(FavorableState.Favored);
    assertTrue(trait.getFavorization().isFavored());
    trait.setCurrentValue(0);
    assertEquals(1, trait.getCreationValue());
  }

  @Test
  public void testCasteAbilityNotSetToFavored() throws Exception {
    trait.getFavorization().setFavorableState(FavorableState.Caste);
    trait.getFavorization().addFavorableStateChangedListener(abilityStateListener);
    trait.getFavorization().setFavorableState(FavorableState.Favored);
    assertSame(FavorableState.Caste, trait.getFavorization().getFavorableState());
    verifyZeroInteractions(abilityStateListener);
  }

  @Test
  public void testExceedCreationValueMaximum() throws Exception {
    trait.setCurrentValue(6);
    assertEquals(5, trait.getCreationValue());
  }

  @Test
  public void testUnderrunCreationValueMinimum() throws Exception {
    trait.setCurrentValue(-1);
    assertEquals(0, trait.getCreationValue());
  }

  private DefaultTrait createObjectUnderTest(ICharacterModelContext context) {
    ITraitTemplate archeryTemplate = SimpleTraitTemplate.createEssenceLimitedTemplate(0);
    ITraitContext traitContext = context.getTraitContext();
    FavorableTraitRules rules = new FavorableTraitRules(
            AbilityType.Archery,
            archeryTemplate,
            traitContext.getLimitationContext());
    return new DefaultTrait(
            rules,
            new ICasteType[]{new DummyCasteType()},
            traitContext,
            context.getBasicCharacterContext(),
            context.getCharacterListening(),
            new FriendlyValueChangeChecker(),
            incrementChecker);
  }

  @Test
  public void creationValueIsLowerBoundForExperiencedValue() throws Exception {
    trait.setCurrentValue(2);
    valueStrategy.setStrategy(new ExperiencedTraitValueStrategy());
    trait.setCurrentValue(3);
    final int[] holder = new int[1];
    trait.addCurrentValueListener(new IIntValueChangedListener() {
      public void valueChanged(int newValue) {
        holder[0] = newValue;
      }
    });
    trait.setCurrentValue(0);
    assertEquals(2, holder[0]);
  }

  @Test
  public void testSetValueTo6OnExperiencedCharacterWithoutHighEssence() throws Exception {
    valueStrategy.setStrategy(new ExperiencedTraitValueStrategy());
    trait.setCurrentValue(6);
    assertEquals(5, trait.getCurrentValue());
  }

  @Test
  public void testExperienceSpecialtyCount() throws Exception {
    ISubTraitContainer container = new SpecialtiesContainer(
            new DefaultTraitReference(trait),
            modelContext.getTraitContext());
    ISubTrait specialty = container.addSubTrait("TestSpecialty"); //$NON-NLS-1$
    specialty.setCreationValue(1);
    valueStrategy.setStrategy(new ExperiencedTraitValueStrategy());
    specialty.setExperiencedValue(2);
    assertEquals(2, specialty.getCurrentValue());
    assertEquals(1, container.getCreationDotTotal());
    assertEquals(1, container.getExperienceDotTotal());
  }

  @Test
  public void testCreationSpecialtyDuringExperienced() throws Exception {
    ICharacterModelContext context = new BasicCharacterTestCase().createModelContextWithEssence2(new ExperiencedTraitValueStrategy());
    ISubTraitContainer container = new SpecialtiesContainer(new DefaultTraitReference(trait), context.getTraitContext());
    ISubTrait specialty = container.addSubTrait("TestSpecialty"); //$NON-NLS-1$
    specialty.setCreationValue(2);
    assertEquals(2, specialty.getCreationValue());
    assertEquals(-1, specialty.getExperiencedValue());
    assertEquals(2, specialty.getCurrentValue());
    assertEquals(2, container.getCreationDotTotal());
    assertEquals(0, container.getExperienceDotTotal());
  }
}