package net.sf.anathema.hero.traits.model.types;

import net.sf.anathema.hero.traits.model.TraitType;
import net.sf.anathema.hero.traits.model.ValuedTraitType;

public class SimpleValuedTraitType implements ValuedTraitType {

  public static final SimpleValuedTraitType NULL_TYPE = new SimpleValuedTraitType(null, 1);
  private final TraitType traitType;
  private final int value;

  @Override
  public TraitType getType() {
    return traitType;
  }

  @Override
  public int getCurrentValue() {
    return value;
  }

  @Override
  public boolean isCasteOrFavored() {
    return false;
  }

  public SimpleValuedTraitType(TraitType traitType, int value) {
    this.traitType = traitType;
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SimpleValuedTraitType)) {
      return false;
    }
    SimpleValuedTraitType otherType = (SimpleValuedTraitType) obj;
    if (otherType.getType() == null) {
      return false;
    }
    return otherType.getType().equals(traitType) && otherType.getCurrentValue() == value;
  }

  @Override
  public int hashCode() {
    return traitType.hashCode() + value * 17;
  }
}