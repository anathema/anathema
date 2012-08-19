package net.sf.anathema.character.presenter.magic;

import com.google.common.base.Predicate;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;

public interface SpecialCharmList {
  void add(ISpecialCharm charm);

  void hideAllViews();

  void showViews();

  void setVisibilityPredicate(Predicate<String> predicate);
}
