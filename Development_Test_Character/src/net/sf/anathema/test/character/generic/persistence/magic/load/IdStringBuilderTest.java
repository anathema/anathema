package net.sf.anathema.test.character.generic.persistence.magic.load;

import static org.junit.Assert.assertEquals;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.IIdStringBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.IdStringBuilder;
import net.sf.anathema.character.generic.magic.charms.CharmException;
import net.sf.anathema.lib.xml.DocumentUtilities;

import org.dom4j.Element;
import org.junit.Test;

public class IdStringBuilderTest {

  private IIdStringBuilder builder = new IdStringBuilder();

  @Test
  public void testIdPresent() throws Exception {
    String xml = "<charm id=\"test\"/>"; //$NON-NLS-1$
    Element rootElement = DocumentUtilities.read(xml).getRootElement();
    String id = builder.build(rootElement);
    assertEquals("test", id); //$NON-NLS-1$
  }

  @Test
  public void testIdVariable() throws Exception {
    String xml = "<charm id=\"otherTest\"/>"; //$NON-NLS-1$
    Element rootElement = DocumentUtilities.read(xml).getRootElement();
    String id = builder.build(rootElement);
    assertEquals("otherTest", id); //$NON-NLS-1$
  }

  @Test(expected=CharmException.class)
  public void testIdMissing() throws Exception {
        String xml = "<charm />"; //$NON-NLS-1$
        Element rootElement = DocumentUtilities.read(xml).getRootElement();
        builder.build(rootElement);
  }

  @Test(expected=CharmException.class)
  public void testBadId() throws Exception {
        String xml = "<charm id=\"\"/>"; //$NON-NLS-1$
        Element rootElement = DocumentUtilities.read(xml).getRootElement();
        builder.build(rootElement);
  }
}