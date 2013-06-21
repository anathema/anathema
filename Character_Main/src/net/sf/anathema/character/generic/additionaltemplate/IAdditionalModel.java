package net.sf.anathema.character.generic.additionaltemplate;

import net.sf.anathema.hero.display.HeroModelGroup;
import net.sf.anathema.lib.control.IChangeListener;

public interface IAdditionalModel {

  String getTemplateId();

  HeroModelGroup getAdditionalModelType();

  HeroModelBonusPointCalculator getBonusPointCalculator();

  void addChangeListener(IChangeListener listener);

  HeroModelExperienceCalculator getExperienceCalculator();
}