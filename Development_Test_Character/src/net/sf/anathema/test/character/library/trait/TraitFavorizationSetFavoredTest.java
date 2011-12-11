package net.sf.anathema.test.character.library.trait;

import net.sf.anathema.character.dummy.trait.DummyDefaultTrait;
import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.dummy.DummyBasicCharacterData;
import net.sf.anathema.character.generic.dummy.DummyCasteType;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.character.library.trait.favorable.FavorableState;
import net.sf.anathema.character.library.trait.favorable.FriendlyIncrementChecker;
import net.sf.anathema.character.library.trait.favorable.IFavorableStateChangedListener;
import net.sf.anathema.character.library.trait.favorable.IIncrementChecker;
import net.sf.anathema.character.library.trait.favorable.TraitFavorization;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.lib.testing.BasicTestCase;

import org.easymock.EasyMock;

public class TraitFavorizationSetFavoredTest extends BasicTestCase {

  private IDefaultTrait archeryTrait;
  private IFavorableStateChangedListener listener;

  private TraitFavorization createFriendlyTraitFavorization() {
    return createTraitFavorization(new FriendlyIncrementChecker());
  }

  private TraitFavorization createTraitFavorization(IIncrementChecker incrementChecker) {
    IBasicCharacterData characterData = new DummyBasicCharacterData();
    return new TraitFavorization(characterData, new ICasteType[] { new DummyCasteType() },
    		incrementChecker, archeryTrait, false);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.archeryTrait = new DummyDefaultTrait(AbilityType.Archery);
    this.listener = EasyMock.createStrictMock(IFavorableStateChangedListener.class);
  }

  public void testSetFavoredOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    assertSame(FavorableState.Default, favorization.getFavorableState());
    favorization.setFavored(true);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
  }

  public void testSetFavoredOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.setFavored(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
  }

  public void testSetNonFavoredOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    assertSame(FavorableState.Default, favorization.getFavorableState());
    favorization.setFavored(false);
    assertSame(FavorableState.Default, favorization.getFavorableState());
  }

  public void testSetNonFavoredOnFavored() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setFavored(true);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
    favorization.setFavored(false);
    assertSame(FavorableState.Default, favorization.getFavorableState());
  }

  public void testSetNonFavoredOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.setFavored(false);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
  }

  public void testEventOnSetFavoredOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.addFavorableStateChangedListener(listener);
    listener.favorableStateChanged(FavorableState.Favored);
    EasyMock.replay();
    favorization.setFavored(true);
    EasyMock.verify();
  }

  public void testEventOnSetNotFavoredOnFavored() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setFavored(true);
    favorization.addFavorableStateChangedListener(listener);
    listener.favorableStateChanged(FavorableState.Default);
    EasyMock.replay();
    favorization.setFavored(false);
    EasyMock.verify();
  }

  public void testNoEventOnSetFavoredOnFavored() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setFavored(true);
    assertSame(FavorableState.Favored, favorization.getFavorableState());
    favorization.addFavorableStateChangedListener(listener);
    EasyMock.replay();
    favorization.setFavored(true);
    EasyMock.verify();
  }

  public void testNoEventOnSetFavoredOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.addFavorableStateChangedListener(listener);
    EasyMock.replay();
    favorization.setFavored(true);
    EasyMock.verify();
  }

  public void testNoEventOnSetNotFavoredOnDefault() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    assertSame(FavorableState.Default, favorization.getFavorableState());
    favorization.addFavorableStateChangedListener(listener);
    EasyMock.replay();
    favorization.setFavored(false);
    EasyMock.verify();
  }

  public void testNoEventOnSetNotFavoredOnCaste() throws Exception {
    TraitFavorization favorization = createFriendlyTraitFavorization();
    favorization.setCaste(true);
    assertSame(FavorableState.Caste, favorization.getFavorableState());
    favorization.addFavorableStateChangedListener(listener);
    EasyMock.replay();
    favorization.setFavored(false);
    EasyMock.verify();
  }
}