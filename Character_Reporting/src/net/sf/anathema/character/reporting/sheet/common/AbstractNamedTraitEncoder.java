package net.sf.anathema.character.reporting.sheet.common;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.framework.resources.TraitInternationalizer;
import net.sf.anathema.character.generic.traits.INamedGenericTrait;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.reporting.sheet.util.AbstractPdfEncoder;
import net.sf.anathema.character.reporting.sheet.util.PdfTraitEncoder;
import net.sf.anathema.character.reporting.util.Position;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public abstract class AbstractNamedTraitEncoder extends AbstractPdfEncoder {
  private static final int SUBSECTION_FONT_SIZE = 8;
  private final IResources resources;
  private final PdfTraitEncoder traitEncoder;
  private final BaseFont baseFont;

  public AbstractNamedTraitEncoder(IResources resources, BaseFont baseFont, PdfTraitEncoder traitEncoder) {
    this.resources = resources;
    this.baseFont = baseFont;
    this.traitEncoder = traitEncoder;
  }

  protected IResources getResources() {
    return resources;
  }

  @Override
  protected BaseFont getBaseFont() {
    return baseFont;
  }
  
  protected int getLineCount(String title, float height) {
    if (title != null) {
      height -= SUBSECTION_FONT_SIZE * 1.5f;
    }
    return (int)(height / traitEncoder.getTraitHeight());
  }

  protected float drawNamedTraitSection(PdfContentByte directContent,
                                        String title,
                                        IValuedTraitReference[] traits,
                                        Position position, float width,
                                        float height, int dotCount) {
    return _drawNamedTraitSection(directContent, title, traits, position,
                                  width, getLineCount(title, height), dotCount);
  }

  protected float _drawNamedTraitSection(PdfContentByte directContent,
                                        String title,
                                        IValuedTraitReference[] traits,
                                        Position position, float width,
                                        int lineCount, int dotCount) {
    float height = 0;
    if (title != null) {
      height = drawSubsectionHeader(directContent, title, position, width);
    }
    TraitInternationalizer internationalizer = new TraitInternationalizer(getResources());
    for (int index = 0; index < lineCount && index < traits.length; index++) {
      IValuedTraitReference trait = traits[index];
      String name = internationalizer.getSheetName(trait);
      Position traitPosition = new Position(position.x, position.y - height);
      int value = trait.getValue();
      traitEncoder.encodeWithText(directContent, name, traitPosition, width, value, dotCount);
      height += traitEncoder.getTraitHeight();
    }
    for (int index = traits.length; index < lineCount; index++) {
      Position traitPosition = new Position(position.x, position.y - height);
      traitEncoder.encodeWithLine(directContent, traitPosition, width, 0, dotCount);
      height += traitEncoder.getTraitHeight();
    }
    return height;
  }

  private final float drawSubsectionHeader(PdfContentByte directContent, String text, Position position, float width) {
    setSubsectionFont(directContent);
    drawText(directContent, text, new Position(position.x + width / 2, position.y), PdfContentByte.ALIGN_CENTER);
    return SUBSECTION_FONT_SIZE * 1.5f;
  }

  protected final void setSubsectionFont(PdfContentByte directContent) {
    directContent.setFontAndSize(getBaseFont(), SUBSECTION_FONT_SIZE);
  }

  protected final IValuedTraitReference[] getTraitReferences(INamedGenericTrait[] traits, ITraitType type) {
    List<IValuedTraitReference> references = new ArrayList<IValuedTraitReference>();
    for (INamedGenericTrait trait : traits) {
      references.add(new NamedGenericTraitReference(trait, type));
    }
    return references.toArray(new IValuedTraitReference[references.size()]);
  }
}