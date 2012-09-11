package be.virtualsushi.jfx.dorse.dialogs;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSupplierEvent;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Supplier;

@Component
@FxmlFile("IdNamePairEditDialog.fxml")
public class SupplierEditDialog extends IdNamePairEditDialog<Supplier> {

	@Override
	protected Supplier createNewEntityInstance() {
		return new Supplier();
	}

	@Override
	protected SaveEntityEvent<Supplier> createSaveEvent(Supplier entity) {
		return new SaveSupplierEvent(entity);
	}

}
