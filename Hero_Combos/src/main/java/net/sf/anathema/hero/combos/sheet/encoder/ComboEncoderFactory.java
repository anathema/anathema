package net.sf.anathema.hero.combos.sheet.encoder;

import net.sf.anathema.framework.environment.Resources;
import net.sf.anathema.hero.sheet.pdf.content.BasicContent;
import net.sf.anathema.hero.sheet.pdf.encoder.EncoderIds;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.AbstractEncoderFactory;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.ContentEncoder;

@SuppressWarnings("UnusedDeclaration")
public class ComboEncoderFactory extends AbstractEncoderFactory {

  public ComboEncoderFactory() {
    super(EncoderIds.COMBOS);
    setPreferredHeight(new PreferredComboHeight());
  }

  @Override
  public ContentEncoder create(Resources resources, BasicContent content) {
    return new ComboEncoder();
  }

  @Override
  public boolean supports(BasicContent content) {
    return content.isEssenceUser();
  }
}
