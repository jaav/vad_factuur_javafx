package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveUnitEvent;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Unit;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class UnitEditDialog extends IdNamePairEditDialog<Unit> {

	@Override
	protected Unit createNewEntityInstance() {
		return new Unit();
	}

	@Override
	protected SaveEntityEvent<Unit> createSaveEvent(Unit entity) {
		return new SaveUnitEvent(entity);
	}

}
