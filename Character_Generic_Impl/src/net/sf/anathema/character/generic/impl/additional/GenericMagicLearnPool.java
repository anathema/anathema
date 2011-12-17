package net.sf.anathema.character.generic.impl.additional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.disy.commons.core.util.ArrayUtilities;
import net.sf.anathema.character.generic.additionalrules.IAdditionalMagicLearnPool;
import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.character.IGenericTraitCollection;
import net.sf.anathema.character.generic.impl.util.DefaultPointModification;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.magic.spells.CircleType;
import net.sf.anathema.character.generic.traits.IGenericTrait;
import net.sf.anathema.character.generic.util.IPointModification;
import net.sf.anathema.lib.collection.DefaultValueHashMap;

public class GenericMagicLearnPool implements IAdditionalMagicLearnPool {

  private final IBackgroundTemplate template;
  private final boolean defaultResponse;
  private final List<String> exceptionIds = new ArrayList<String>();
  private final Map<CircleType, Integer> typesByMinimumValue = new DefaultValueHashMap<CircleType, Integer>(0);
  private CircleType maximumCircle = CircleType.Terrestrial;
  private IPointModification pointModification = new DefaultPointModification();

  public GenericMagicLearnPool(IBackgroundTemplate template, boolean defaultResponse) {
    this.template = template;
    this.defaultResponse = defaultResponse;
  }

  public int getAdditionalMagicCount(IGenericTraitCollection traitCollection) {
    int value = getBackgroundValue(traitCollection);
    return value + pointModification.getAdditionalPoints(value);
  }

  private int getBackgroundValue(IGenericTraitCollection traitCollection) {
    IGenericTrait background = traitCollection.getTrait(template);
    if (background == null) {
      return 0;
    }
    return background.getCurrentValue();
  }

  public boolean isAllowedFor(final IGenericTraitCollection traitCollection, IMagic magic) {
    final boolean[] isAllowed = new boolean[1];
    magic.accept(new IMagicVisitor() {
      public void visitSpell(ISpell spell) {
        CircleType type = spell.getCircleType();
        if (isSpellCircleGreaterThanMaximumCircle(type)
            || isBackgroundValueLessThanMinimumValueForCircle(traitCollection, type)) {
          isAllowed[0] = false;
          return;
        }
        if (exceptionIds.contains(spell.getId())) {
          isAllowed[0] = !defaultResponse;
        }
        else {
          isAllowed[0] = defaultResponse;
        }
      }

      public void visitCharm(ICharm charm) {
        isAllowed[0] = false;
      }
    });
    return isAllowed[0];
  }

  public void addIdException(String attributeValue) {
    exceptionIds.add(attributeValue);
  }

  public void setMaximumCircle(CircleType type) {
    this.maximumCircle = type;
  }

  public void attachCondition(CircleType minimumType, int minimumBackgroundValue) {
    for (CircleType type : CircleType.values()) {
      if (type.compareTo(minimumType) >= 0) {
        typesByMinimumValue.put(type, minimumBackgroundValue);
      }
    }
  }

  public void setCostModification(IPointModification pointModification) {
    this.pointModification = pointModification;
  }

  private boolean isBackgroundValueLessThanMinimumValueForCircle(
      final IGenericTraitCollection traitCollection,
      CircleType type) {
    return typesByMinimumValue.get(type) > getBackgroundValue(traitCollection);
  }

  private boolean isSpellCircleGreaterThanMaximumCircle(CircleType type) {
    return !ArrayUtilities.containsValue(type.getComparableCircles(), maximumCircle) || (maximumCircle.compareTo(type) < 0);
  }
}