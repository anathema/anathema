package net.sf.anathema.character.equipment.modification;

import net.sf.anathema.character.equipment.impl.character.model.stats.modification.WeaponStatsType;
import net.sf.anathema.character.generic.impl.rules.ExaltedRuleSet;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;

import org.junit.Test;

public class SecondEditionSoulsteelModificationTest extends AbstractSoulsteelModificationTest {

  @Test
  public final void accuracyIncreasedBy2() throws Exception {
    assertAccuracyModification(3, 1, WeaponStatsType.Melee);
    assertAccuracyModification(3, 1, WeaponStatsType.Bow);
    assertAccuracyModification(3, 1, WeaponStatsType.Thrown);
    assertAccuracyModification(3, 1, WeaponStatsType.Thrown_BowBonuses);
    assertAccuracyModification(3, 1, WeaponStatsType.Flame);
  }

  @Test
  public void hardnessIncreasedBy1() {
    assertHardnessModification(2, 1);
  }

  @Override
  protected IExaltedRuleSet getRuleSet() {
    return ExaltedRuleSet.SecondEdition;
  }
}