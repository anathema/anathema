package net.sf.anathema.hero.charms.persistence.special.effect;

import net.sf.anathema.character.main.magic.model.charm.special.CharmSpecialsModel;
import net.sf.anathema.character.main.magic.model.charm.special.MultipleEffectCharmSpecials;
import net.sf.anathema.character.main.magic.model.charm.special.SubEffect;
import net.sf.anathema.hero.charms.persistence.special.SpecialCharmPersister;
import net.sf.anathema.hero.charms.persistence.special.SpecialCharmPto;

public class MultipleEffectCharmPersister implements SpecialCharmPersister {

  @Override
  public void saveCharmSpecials(CharmSpecialsModel charmSpecials, SpecialCharmPto charmPto) {
    MultipleEffectCharmSpecials multipleEffects = (MultipleEffectCharmSpecials) charmSpecials;
    SubEffectListPto subEffectsList = createPto(multipleEffects);
    charmPto.subEffects = subEffectsList;
  }

  private SubEffectListPto createPto(MultipleEffectCharmSpecials multipleEffects) {
    SubEffectListPto subEffectsList = new SubEffectListPto();
    for (SubEffect effect : multipleEffects.getEffects()) {
      SubEffectPto pto = createSubEffectPto(effect);
      subEffectsList.subEffects.add(pto);
    }
    return subEffectsList;
  }

  private SubEffectPto createSubEffectPto(SubEffect effect) {
    SubEffectPto pto = new SubEffectPto();
    pto.id = effect.getId();
    pto.creationLearned = effect.isCreationLearned();
    pto.experienceLearned = effect.isCreationLearned() || effect.isLearned();
    return pto;
  }
}