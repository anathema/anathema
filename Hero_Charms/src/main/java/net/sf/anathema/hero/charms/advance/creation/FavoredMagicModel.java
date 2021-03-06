package net.sf.anathema.hero.charms.advance.creation;

import net.sf.anathema.hero.template.creation.ICreationPoints;
import net.sf.anathema.hero.advance.overview.model.AbstractSpendingModel;

public class FavoredMagicModel extends AbstractSpendingModel {
  private final MagicCreationCostCalculator magicCalculator;
  private final ICreationPoints creationPoints;

  public FavoredMagicModel(MagicCreationCostCalculator magicCalculator, ICreationPoints creationPoints) {
    super("Charms", "Favored");
    this.magicCalculator = magicCalculator;
    this.creationPoints = creationPoints;
  }

  @Override
  public int getSpentBonusPoints() {
    return 0;
  }

  @Override
  public Integer getValue() {
    return magicCalculator.getFavoredCharmPicksSpent();
  }

  @Override
  public int getAllotment() {
    return creationPoints.getFavoredCreationMagicCount();
  }
}