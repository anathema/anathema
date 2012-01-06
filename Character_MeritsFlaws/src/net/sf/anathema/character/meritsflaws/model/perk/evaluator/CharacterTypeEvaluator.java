package net.sf.anathema.character.meritsflaws.model.perk.evaluator;

import java.util.Arrays;

import net.disy.commons.core.util.ArrayUtilities;
import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.type.ICharacterType;

public class CharacterTypeEvaluator implements ICharacterEvaluator {

  private final ICharacterType[] types;

  public CharacterTypeEvaluator(ICharacterType[] types) {
    this.types = types;
  }

  public boolean evaluateCharacter(IBasicCharacterData characterData) {
    return ArrayUtilities.containsValue(types, characterData.getCharacterType());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    return Arrays.equals(this.types, ((CharacterTypeEvaluator) obj).types);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(types) * 11;
  }
}