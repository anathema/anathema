package net.sf.anathema.character.main.magic.model.charm.special;

public abstract class AbstractMultiLearnableCharm implements IMultiLearnableCharm {

  private final String charmId;

  public AbstractMultiLearnableCharm(String charmId) {
    this.charmId = charmId;
  }

  @Override
  public final String getCharmId() {
    return charmId;
  }

  @Override
  public void accept(ISpecialCharmVisitor visitor) {
    visitor.visitMultiLearnableCharm(this);
  }

  @Override
  public int getMinimumLearnCount(LearnRangeContext learnRangeContext) {
    return 1;
  }
}