package net.sf.anathema.hero.spiritual.advance.creation;

import net.sf.anathema.hero.points.HeroBonusPointCalculator;
import net.sf.anathema.hero.spiritual.SpiritualTraitModel;
import net.sf.anathema.hero.spiritual.model.traits.TraitCollectionUtilities;
import net.sf.anathema.hero.template.experience.CurrentRatingCosts;
import net.sf.anathema.hero.traits.advance.TraitRatingCostCalculator;
import net.sf.anathema.hero.traits.model.Trait;
import net.sf.anathema.hero.traits.model.types.OtherTraitType;

import static net.sf.anathema.hero.traits.model.types.OtherTraitType.Essence;

public class SpiritualBonusPointsCalculator implements HeroBonusPointCalculator {

  private final Trait willpower;
  private final Trait essence;
  private int essenceBonusPoints;
  private int willpowerBonusPoints;
  private SpiritualCreationData creationData;

  public SpiritualBonusPointsCalculator(SpiritualTraitModel spiritualTraits, SpiritualCreationData creationData) {
    this.creationData = creationData;
    this.willpower = TraitCollectionUtilities.getWillpower(spiritualTraits);
    this.essence = TraitCollectionUtilities.getEssence(spiritualTraits);
  }

  @Override
  public void recalculate() {
    willpowerBonusPoints = calculateWillpowerPoints();
    essenceBonusPoints = calculateEssencePoints();
  }

  @Override
  public int getBonusPointCost() {
    return willpowerBonusPoints + essenceBonusPoints;
  }

  @Override
  public int getBonusPointsGranted() {
    return 0;
  }

  private int calculateEssencePoints() {
    CurrentRatingCosts essenceCost = creationData.getEssenceCost();
    int calculationBase = creationData.getCalculationBase(Essence);
    return TraitRatingCostCalculator.getTraitRatingCosts(calculationBase, essence.getCreationValue(), essenceCost);
  }

  private int calculateWillpowerPoints() {
    int calculationBase = creationData.getCalculationBase(OtherTraitType.Willpower);
    return (willpower.getCreationValue() - calculationBase) * creationData.getWillpowerCost();
  }
}