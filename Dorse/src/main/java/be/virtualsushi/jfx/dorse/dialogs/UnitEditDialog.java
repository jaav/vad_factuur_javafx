package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Unit;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class UnitEditDialog extends IdNamePairEditDialog<Unit> {

}
