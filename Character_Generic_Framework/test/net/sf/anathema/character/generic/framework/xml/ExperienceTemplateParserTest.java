package net.sf.anathema.character.generic.framework.xml;

import net.sf.anathema.character.generic.dummy.template.DummyXmlTemplateRegistry;
import net.sf.anathema.character.generic.framework.xml.experience.ExperienceTemplateParser;
import net.sf.anathema.character.generic.framework.xml.experience.GenericExperiencePointCosts;
import net.sf.anathema.character.generic.impl.template.points.MultiplyRatingCosts;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.charms.MartialArtsLevel;
import net.sf.anathema.character.generic.template.experience.ICostAnalyzer;
import net.sf.anathema.dummy.character.magic.DummyCharm;
import net.sf.anathema.lib.exception.AnathemaException;
import net.sf.anathema.lib.xml.DocumentUtilities;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExperienceTemplateParserTest {

  String xml = "<dummyExperienceTemplate>" //$NON-NLS-1$
    + "<attributes>" //$NON-NLS-1$
    + "<generalCosts>" //$NON-NLS-1$
    + "<currentRating multiplier=\"4\" />" //$NON-NLS-1$
    + "</generalCosts>" //$NON-NLS-1$
    + "<favoredCosts>" //$NON-NLS-1$
    + "<currentRating multiplier=\"3\" />" //$NON-NLS-1$
    + "</favoredCosts>" //$NON-NLS-1$
    + "</attributes>" //$NON-NLS-1$
    + "<abilities>" //$NON-NLS-1$
    + "  <generalCosts>" //$NON-NLS-1$
    + "    <currentRating multiplier=\"2\" initialCosts=\"3\"/>" //$NON-NLS-1$
    + "  </generalCosts>" //$NON-NLS-1$
    + "  <favoredCosts>" //$NON-NLS-1$
    + "    <currentRating multiplier=\"2\" summand=\"-1\" initialCosts=\"3\"/>" //$NON-NLS-1$
    + "  </favoredCosts>" //$NON-NLS-1$
    + "  <specialties>" //$NON-NLS-1$
    + "    <fixedCost cost=\"3\" />" //$NON-NLS-1$
    + "  </specialties>" //$NON-NLS-1$
    + "</abilities>" //$NON-NLS-1$
    + "<advantages>" //$NON-NLS-1$
    + "<willpower>" //$NON-NLS-1$
    + "<currentRating multiplier=\"2\" />" //$NON-NLS-1$
    + "</willpower>" //$NON-NLS-1$
    + "<virtues>" //$NON-NLS-1$
    + "<currentRating multiplier=\"3\" />" //$NON-NLS-1$
    + "</virtues>" //$NON-NLS-1$
    + "<essence>" //$NON-NLS-1$
    + "<currentRating multiplier=\"8\" />" //$NON-NLS-1$
    + "</essence>" //$NON-NLS-1$
    + "</advantages>" //$NON-NLS-1$
    + "<magic>" //$NON-NLS-1$
    + " <charms favored=\"8\" general=\"10\">" //$NON-NLS-1$
    + "<highLevelMartialArts favored=\"13\" general=\"15\"/>" //$NON-NLS-1$
    + "</charms>" //$NON-NLS-1$
    + "</magic>" //$NON-NLS-1$
    + "</dummyExperienceTemplate>"; //$NON-NLS-1$
  private DummyXmlTemplateRegistry<GenericExperiencePointCosts> templateRegistry;
  private ExperienceTemplateParser parser;

  @Before
  public void setUp() throws Exception {
    templateRegistry = new DummyXmlTemplateRegistry<GenericExperiencePointCosts>();
    parser = new ExperienceTemplateParser(templateRegistry, MartialArtsLevel.Celestial);
  }

  private Element createElement() throws AnathemaException {
    return DocumentUtilities.read(xml).getRootElement();
  }

  @Test
  public void testAttributeCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(4), costs.getAttributeCosts(false));
  }

  @Test
  public void testGeneralAbilityCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(2, 3), costs.getAbilityCosts(false));
  }

  @Test
  public void testFavoredAbilityCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(2, 3, -1), costs.getAbilityCosts(true));
  }

  @Test
  public void testSpecialtyCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(3, costs.getSpecialtyCosts(false));
  }

  @Test
  public void testWillpowerCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(2), costs.getWillpowerCosts());
  }

  @Test
  public void testVirtueCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(3), costs.getVirtueCosts());
  }

  @Test
  public void testEssenceCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(new MultiplyRatingCosts(8), costs.getEssenceCosts());
  }

  @Test
  public void testGeneralMagicCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(10, costs.getCharmCosts(new DummyCharm("Charm"), new ICostAnalyzer() { //$NON-NLS-1$
      public boolean isOccultFavored() {
        return false;
      }

      public MartialArtsLevel getMartialArtsLevel(ICharm charm) {
        return null;
      }

      public boolean isMagicFavored(IMagic magic) {
        return false;
      }
    }));
  }

  @Test
  public void testFavoredMagicCosts() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(8, costs.getCharmCosts(new DummyCharm("Charm"), new ICostAnalyzer() { //$NON-NLS-1$
      public boolean isOccultFavored() {
        return true;
      }

      public MartialArtsLevel getMartialArtsLevel(ICharm charm) {
        return null;
      }

      public boolean isMagicFavored(IMagic magic) {
        return true;
      }
    }));
  }

  @Test
  public void testCelestialMartialArtsCost() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(10, costs.getCharmCosts(new DummyCharm("Charm"), new ICostAnalyzer() { //$NON-NLS-1$
      public boolean isOccultFavored() {
        return true;
      }

      public MartialArtsLevel getMartialArtsLevel(ICharm charm) {
        return MartialArtsLevel.Celestial;
      }

      public boolean isMagicFavored(IMagic magic) {
        return false;
      }
    }));
  }

  @Test
  public void testUnfavoredHighLevelMartialArtsCost() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(15, costs.getCharmCosts(new DummyCharm("Charm"), new ICostAnalyzer() { //$NON-NLS-1$
      public boolean isOccultFavored() {
        return true;
      }

      public MartialArtsLevel getMartialArtsLevel(ICharm charm) {
        return MartialArtsLevel.Sidereal;
      }

      public boolean isMagicFavored(IMagic magic) {
        return false;
      }
    }));
  }

  @Test
  public void testFavoredHighLevelMartialArtsCost() throws Exception {
    Element element = createElement();
    GenericExperiencePointCosts costs = parser.parseTemplate(element);
    assertEquals(13, costs.getCharmCosts(new DummyCharm("Charm"), new ICostAnalyzer() { //$NON-NLS-1$
      public boolean isOccultFavored() {
        return true;
      }

      public MartialArtsLevel getMartialArtsLevel(ICharm charm) {
        return MartialArtsLevel.Sidereal;
      }

      public boolean isMagicFavored(IMagic magic) {
        return true;
      }
    }));
  }
}