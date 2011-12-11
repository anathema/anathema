package net.sf.anathema.test.character;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.dummy.DummyCharacterModelContext;
import net.sf.anathema.character.generic.dummy.DummyGenericCharacter;
import net.sf.anathema.character.generic.dummy.DummyGenericTrait;
import net.sf.anathema.character.generic.dummy.template.DummyCharacterTemplate;
import net.sf.anathema.character.generic.framework.CharacterModuleContainerInitializer;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ITraitValueStrategy;
import net.sf.anathema.character.generic.framework.module.CharacterModuleContainer;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;
import net.sf.anathema.framework.resources.AnathemaResources;
import net.sf.anathema.initialization.InitializationException;
import net.sf.anathema.lib.testing.BasicTestCase;

public abstract class BasicCharacterTestCase extends BasicTestCase {

  public final IGenericCharacter createCharacterAbstraction() {
    DummyCharacterTemplate template = new DummyCharacterTemplate();
    return new DummyGenericCharacter(template);
  }

  protected final ICharacterGenerics createCharacterGenerics() throws InitializationException {
    CharacterModuleContainerInitializer initializer = new CharacterModuleContainerInitializer();
    CharacterModuleContainer container = initializer.initContainer(new AnathemaResources(), new DemoDataFileProvider());
    return container.getCharacterGenerics();
  }

  protected final DummyCharacterModelContext createModelContextWithEssence2(final ITraitValueStrategy valueStrategy) {
    DummyCharacterModelContext modelContext = new DummyCharacterModelContext(valueStrategy);
    modelContext.getCharacter().addTrait(new DummyGenericTrait(OtherTraitType.Essence, 2));
    return modelContext;
  }
}