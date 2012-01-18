package net.sf.anathema.character.impl.model.charm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharmLearnStrategy;
import net.sf.anathema.character.generic.impl.magic.charm.CharmGroup;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IExtendedCharmData;
import net.sf.anathema.character.generic.magic.charms.ICharmGroup;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharmConfiguration;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.ICharmLearnListener;
import net.sf.anathema.character.model.charm.IExtendedCharmLearnableArbitrator;
import net.sf.anathema.character.model.charm.ILearningCharmGroup;
import net.sf.anathema.character.model.charm.special.IMultiLearnableCharmConfiguration;
import net.sf.anathema.character.model.charm.special.IMultipleEffectCharmConfiguration;
import net.sf.anathema.lib.control.GenericControl;
import net.sf.anathema.lib.control.IClosure;

public class LearningCharmGroup extends CharmGroup implements ILearningCharmGroup {

  private final Set<ICharm> charmsLearnedOnCreation = new HashSet<ICharm>();
  private final Set<ICharm> charmsLearnedWithExperience = new HashSet<ICharm>();
  private final GenericControl<ICharmLearnListener> control = new GenericControl<ICharmLearnListener>();
  private final IExtendedCharmLearnableArbitrator learnArbitrator;
  private final ICharmLearnStrategy learnStrategy;
  private final ILearningCharmGroupContainer charmGroupContainer;
  private final ICharmConfiguration charmConfig;
  
  public LearningCharmGroup(
	      ICharmLearnStrategy learnStrategy,
	      ICharmGroup simpleCharmGroup,
	      IExtendedCharmLearnableArbitrator arbitrator,
	      ILearningCharmGroupContainer charmGroupContainer)
  {
	  this(learnStrategy, simpleCharmGroup, arbitrator, charmGroupContainer, null);
  }

  public LearningCharmGroup(
      ICharmLearnStrategy learnStrategy,
      ICharmGroup simpleCharmGroup,
      IExtendedCharmLearnableArbitrator arbitrator,
      ILearningCharmGroupContainer charmGroupContainer,
      ICharmConfiguration charmConfig) {
    super(
        simpleCharmGroup.getCharacterType(),
        simpleCharmGroup.getId(),
        simpleCharmGroup.getAllCharms(),
        simpleCharmGroup.isMartialArtsGroup());
    this.learnStrategy = learnStrategy;
    this.learnArbitrator = arbitrator;
    this.charmGroupContainer = charmGroupContainer;
    this.charmConfig = charmConfig;
  }

  public void toggleLearned(ICharm charm) {
    learnStrategy.toggleLearned(this, charm);
  }

  public void toggleLearnedOnCreation(ICharm charm) {
    if (charmsLearnedOnCreation.contains(charm)) {
      forgetCharm(charm, false);
      return;
    }
    if (!learnArbitrator.isLearnable(charm)) {
      boolean mergedLearned = false;
    	for (ICharm merged : charm.getMergedCharms())
    		mergedLearned = mergedLearned || learnArbitrator.isLearned(merged);
      if (!mergedLearned)
    	  fireNotLearnableEvent(charm);
      return;
    }
    learnCharm(charm, false);
  }

  public void toggleExperienceLearnedCharm(ICharm charm) {
    if (charmsLearnedOnCreation.contains(charm)) {
      fireNotUnlearnableEvent(charm);
      return;
    }
    if (charmsLearnedWithExperience.contains(charm)) {
      forgetCharm(charm, true);
      return;
    }
    if (!learnArbitrator.isLearnable(charm)) {
      fireNotLearnableEvent(charm);
      return;
    }
    learnCharm(charm, true);
  }

  public void forgetCharm(ICharm charm, boolean experienced) {
    if (isUnlearnable(charm)) {
      if (experienced) {
        charmsLearnedWithExperience.remove(charm);
      }
      else {
        charmsLearnedOnCreation.remove(charm);
      }
      fireCharmForgotten(charm);
      forgetChildren(charm, experienced);
    }
  }

  public void learnCharm(ICharm charm, boolean experienced) {
	learnParents(charm, experienced);
    learnCharmNoParents(charm, experienced, true);
  }

  public void learnCharmNoParents(ICharm charm, boolean experienced, boolean announce) {
    if (experienced) {
      charmsLearnedWithExperience.add(charm);
    }
    else {
      charmsLearnedOnCreation.add(charm);
    }
    if (announce)
    	fireCharmLearned(charm);
  }

  private void forgetChildren(ICharm charm, boolean experienced) {
    for (ICharm child : charm.getLearnFollowUpCharms(learnArbitrator)) {
      ILearningCharmGroup childGroup = charmGroupContainer.getLearningCharmGroup(child);
      childGroup.forgetCharm(child, experienced);
    }
  }

  private void learnParents(ICharm charm, boolean experienced) {
    for (ICharm parent : charm.getLearnPrerequisitesCharms(learnArbitrator)) {
      ILearningCharmGroup parentGroup = charmGroupContainer.getLearningCharmGroup(parent);
      boolean subeffectHandled = false;
      for (String subeffectRequirement : charm.getParentSubeffects())
      {
    	  if (getSubeffectParent(subeffectRequirement).equals(parent.getId()))
    	  {
    		  ISpecialCharmConfiguration config = charmConfig.getSpecialCharmConfiguration(getSubeffectParent(subeffectRequirement));
    		  if (config instanceof IMultipleEffectCharmConfiguration)
    		  {
    			  subeffectHandled = true;
    		      IMultipleEffectCharmConfiguration mConfig = (IMultipleEffectCharmConfiguration)config;
    		      mConfig.getEffectById(getSubeffect(subeffectRequirement)).setLearned(true);
    		  }
    		  if (config instanceof IMultiLearnableCharmConfiguration)
    		  {
    			  subeffectHandled = true;
    		      IMultiLearnableCharmConfiguration mConfig = (IMultiLearnableCharmConfiguration)config;
    		      String effect = getSubeffect(subeffectRequirement);
    		      int requiredCount = Integer.parseInt(effect.replace("Repurchase", ""));
    		      if (mConfig.getCurrentLearnCount() < requiredCount)
    		    	  mConfig.setCurrentLearnCount(requiredCount);
    		  }
    	  }
      }
      if (!subeffectHandled && !parentGroup.isLearned(parent)) {
        parentGroup.learnCharm(parent, experienced);
      }
    }
  }
  
  private String getSubeffectParent(String subeffect)
  {
	  String[] split = subeffect.split("\\.");
	  return split[0] + "." + split[1] + (split.length == 5 ? "." + split[4] : "");
  }
  
  private String getSubeffect(String subeffect)
  {
	  return subeffect.split("\\.")[3];
  }

  private void fireCharmLearned(final ICharm charm) {
    control.forAllDo(new IClosure<ICharmLearnListener>() {
      public void execute(ICharmLearnListener input) {
        input.charmLearned(charm);
      }
    });
  }

  private void fireCharmForgotten(final ICharm charm) {
    control.forAllDo(new IClosure<ICharmLearnListener>() {
      public void execute(ICharmLearnListener input) {
        input.charmForgotten(charm);
      }
    });
  }

  private void fireNotLearnableEvent(final ICharm charm) {
    control.forAllDo(new IClosure<ICharmLearnListener>() {
      public void execute(ICharmLearnListener input) {
        input.charmNotLearnable(charm);
      }
    });
  }

  private void fireNotUnlearnableEvent(final ICharm charm) {
    control.forAllDo(new IClosure<ICharmLearnListener>() {
      public void execute(ICharmLearnListener input) {
        input.charmNotUnlearnable(charm);
      }
    });
  }

  public void fireRecalculateRequested() {
    control.forAllDo(new IClosure<ICharmLearnListener>() {
      public void execute(ICharmLearnListener input) {
        input.recalculateRequested();
      }
    });
  }

  public void addCharmLearnListener(ICharmLearnListener listener) {
    control.addListener(listener);
  }

  public ICharm[] getCreationLearnedCharms() {
    return charmsLearnedOnCreation.toArray(new ICharm[charmsLearnedOnCreation.size()]);
  }

  public ICharm[] getExperienceLearnedCharms() {
    return charmsLearnedWithExperience.toArray(new ICharm[charmsLearnedWithExperience.size()]);
  }

  public boolean isLearned(ICharm charm) {
    return learnStrategy.isLearned(this, charm);
  }

  /**
   * @param experienced true to learn whether the charm is learned on experience, false if interested in creation
   *         learning.
   */
  public boolean isLearned(ICharm charm, boolean experienced) {
    if (experienced) {
      return charmsLearnedWithExperience.contains(charm);
    }
    return charmsLearnedOnCreation.contains(charm);
  }

  public boolean isUnlearnable(ICharm charm) {
    return !learnArbitrator.isCompulsiveCharm(charm) && learnStrategy.isUnlearnable(this, charm);
  }

  public boolean isUnlearnableWithoutConsequences(ICharm charm) {
    if (!isUnlearnable(charm)) {
      return false;
    }
    for (ICharm child : charm.getLearnFollowUpCharms(learnArbitrator)) {
      ILearningCharmGroup childGroup = charmGroupContainer.getLearningCharmGroup(child);
      if (childGroup.isLearned(child)) {
        return false;
      }
    }
    return true;
  }

  public void forgetAll() {
    Set<ICharm> forgetCloneCharms = new HashSet<ICharm>(charmsLearnedWithExperience);
    for (ICharm charm : forgetCloneCharms) {
      forgetCharm(charm, true);
    }
    forgetCloneCharms = new HashSet<ICharm>(charmsLearnedOnCreation);
    for (ICharm charm : forgetCloneCharms) {
      forgetCharm(charm, false);
    }
  }

  @Override
  public boolean hasLearnedCharms() {
    return charmsLearnedOnCreation.size() + charmsLearnedWithExperience.size() > 0;
  }

  @Override
  public ICharm[] getCoreCharms() {
    ICharm[] allCharms = getAllCharms();
    List<ICharm> charms = new ArrayList<ICharm>();
    for (ICharm charm : allCharms) {
      if (!charm.hasAttribute(IExtendedCharmData.EXCLUSIVE_ATTRIBUTE)) {
        charms.add(charm);
      }
    }
    return charms.toArray(new ICharm[charms.size()]);
  }

  @Override
  public void unlearnExclusives() {
    List<ICharm> exclusiveCharms = new ArrayList<ICharm>();
    Collections.addAll(exclusiveCharms, getAllCharms());
    exclusiveCharms.removeAll(Arrays.asList(getCoreCharms()));
    for (ICharm charm : exclusiveCharms) {
      forgetCharm(charm, isLearned(charm, true));
    }
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}