package net.sf.anathema.character.equipment.impl.character.model.stats;

import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.ObjectUtilities;
import net.sf.anathema.character.equipment.MagicalMaterial;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.AccuracyModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.DamageModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.DefenseModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.IStatsModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.RangeModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.RateModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.SpeedModification;
import net.sf.anathema.character.equipment.impl.character.model.stats.modification.WeaponStatsType;
import net.sf.anathema.character.equipment.impl.creation.model.WeaponTag;
import net.sf.anathema.character.generic.equipment.weapon.IEquipmentStats;
import net.sf.anathema.character.generic.equipment.weapon.IWeaponStats;
import net.sf.anathema.character.generic.health.HealthType;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.util.IProxy;
import net.sf.anathema.lib.util.IIdentificate;

public class ProxyWeaponStats extends AbstractStats implements IWeaponStats, IProxy<IWeaponStats> {

  private final IWeaponStats delegate;
  private final MagicalMaterial material;
  private final IExaltedRuleSet ruleSet;

  public ProxyWeaponStats(IWeaponStats stats, MagicalMaterial material, IExaltedRuleSet ruleSet) {
    this.delegate = stats;
    this.material = material;
    this.ruleSet = ruleSet;
  }
  
  public IWeaponStats getUnderlying() {
    return this.delegate;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ProxyWeaponStats)) {
      return false;
    }
    ProxyWeaponStats other = (ProxyWeaponStats) obj;
    return ObjectUtilities.equals(delegate, other.delegate)
        && ObjectUtilities.equals(material, other.material)
        && ObjectUtilities.equals(ruleSet, other.ruleSet);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public int getAccuracy() {
    return getModifiedValue(new AccuracyModification(material, ruleSet), delegate.getAccuracy());
  }

  private Integer getModifiedValue(IStatsModification modification, Integer unmodifiedValue) {
    if (unmodifiedValue == null) {
      return null;
    }
    return !useAttunementModifiers() ? unmodifiedValue : modification.getModifiedValue(unmodifiedValue, getWeaponStatsType());
  }

  private WeaponStatsType getWeaponStatsType() {
    if (ArrayUtilities.containsValue(getTags(), WeaponTag.BowType)) {
      return WeaponStatsType.Bow;
    }
    if (ArrayUtilities.containsValue(getTags(), WeaponTag.FlameType)) {
      return WeaponStatsType.Flame;
    }
    if (ArrayUtilities.containsValue(getTags(), WeaponTag.Thrown)) {
      if (ArrayUtilities.containsValue(getTags(), WeaponTag.BowBonuses)) {
        return WeaponStatsType.Thrown_BowBonuses;
      } else {
        return WeaponStatsType.Thrown;  
      }
    }
    return WeaponStatsType.Melee;
  }

  public int getDamage() {
    return getModifiedValue(new DamageModification(material, ruleSet), delegate.getDamage());
  }

  public ITraitType getDamageTraitType() {
    return delegate.getDamageTraitType();
  }

  public HealthType getDamageType() {
    return delegate.getDamageType();
  }

  public Integer getDefence() {
    return getModifiedValue(new DefenseModification(material, ruleSet), delegate.getDefence());
  }

  public Integer getRange() {
    return getModifiedValue(new RangeModification(material, ruleSet), delegate.getRange());
  }

  public Integer getRate() {
    return getModifiedValue(new RateModification(material, ruleSet), delegate.getRate());
  }

  public int getSpeed() {
    return getModifiedValue(new SpeedModification(material, ruleSet), delegate.getSpeed());
  }

  public IIdentificate[] getTags() {
    return delegate.getTags();
  }

  public ITraitType getTraitType() {
    return delegate.getTraitType();
  }

  public boolean inflictsNoDamage() {
    return delegate.inflictsNoDamage();
  }

  public IIdentificate getName() {
    return delegate.getName();
  }

  public boolean isRangedCombat() {
    return delegate.isRangedCombat();
  }

  @Override
  public IEquipmentStats[] getViews() {
    return new IEquipmentStats[] { this };
  }

  @Override
  public String getId() {
    return getName().getId();
  }
  
  @Override
  public Object[] getApplicableMaterials()
  {
	  return delegate.getApplicableMaterials();
  }
}
