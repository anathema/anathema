package net.sf.anathema.lib.dummy;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import net.disy.commons.swing.icon.EmptyIcon;
import net.sf.anathema.lib.exception.NotYetImplementedException;
import net.sf.anathema.lib.resources.IResources;

public class DummyResources implements IResources {
  private final Map<String, String> stringMap = new HashMap<String, String>();

  public boolean supportsKey(String key) {
    return stringMap.containsKey(key);
  }

  public void putString(String key, String value) {
    stringMap.put(key, value);
  }

  public String getString(String key, Object... arguments) {
    if (arguments.length == 0) {
      return stringMap.get(key);
    }
    throw new NotYetImplementedException();
  }

  public Image getImage(Class< ? > requestor, String relativePath) {
    throw new NotYetImplementedException();
  }

  public Icon getImageIcon(Class< ? > requestor, String relativePath) {
    return new EmptyIcon();
  }

}