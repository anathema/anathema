package net.sf.anathema.demo.character.equipment.character;

import net.sf.anathema.character.equipment.MaterialComposition;
import net.sf.anathema.character.equipment.character.EquipmentAdditionalPresenter;
import net.sf.anathema.character.equipment.dummy.DemoEquipmentAdditionalModel;
import net.sf.anathema.character.equipment.impl.character.model.EquipmentTemplate;
import net.sf.anathema.character.equipment.impl.character.model.natural.secondedition.Kick;
import net.sf.anathema.character.equipment.impl.character.model.natural.secondedition.Punch;
import net.sf.anathema.character.equipment.impl.character.view.EquipmentAdditionalView;
import net.sf.anathema.character.generic.impl.rules.ExaltedRuleSet;
import net.sf.anathema.demo.character.equipment.DemoCollectionFactory;
import net.sf.anathema.framework.resources.AnathemaResources;
import de.jdemo.extensions.SwingDemoCase;

public class EquipmentAdditionalViewDemo extends SwingDemoCase {

  public void demo() {
    AnathemaResources resources = new AnathemaResources();
    DemoEquipmentAdditionalModel equipmentModel = new DemoEquipmentAdditionalModel();
    EquipmentTemplate template = new EquipmentTemplate("Character Data", //$NON-NLS-1$
        "The characters natural weapons and armour.", MaterialComposition.None, null, new DemoCollectionFactory()); //$NON-NLS-1$
    template.addStats(ExaltedRuleSet.SecondEdition, new Punch());
    template.addStats(ExaltedRuleSet.SecondEdition, new Kick());
    equipmentModel.addAvailableTemplates(template);
    EquipmentAdditionalView view = new EquipmentAdditionalView();
    new EquipmentAdditionalPresenter(resources, equipmentModel, view).initPresentation();
    show(view.getComponent());
  }
}