package net.sf.anathema.character.lunar.beastform.model.gift.weapons;

import net.sf.anathema.character.equipment.MagicalMaterial;
import net.sf.anathema.character.equipment.MaterialComposition;
import net.sf.anathema.character.equipment.template.IEquipmentTemplate;
import net.sf.anathema.character.generic.equipment.weapon.IEquipmentStats;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;

public class SavageMoonsilverTalonsTemplate implements IEquipmentTemplate {

  private static final String TALONS = "Lunar.SavageMoonsilverTalons"; //$NON-NLS-1$

  @Override
  public MaterialComposition getComposition() {
    return MaterialComposition.Fixed;
  }

  @Override
  public String getDescription() {
    return TALONS;
  }

  @Override
  public MagicalMaterial getMaterial() {
    return MagicalMaterial.Moonsilver;
  }

  @Override
  public String getName() {
    return TALONS;
  }

  @Override
  public IEquipmentStats[] getStats(IExaltedRuleSet ruleSet) {
    throw new UnsupportedOperationException("Second Edition Lunars not yet supported"); //$NON-NLS-1$
  }
}