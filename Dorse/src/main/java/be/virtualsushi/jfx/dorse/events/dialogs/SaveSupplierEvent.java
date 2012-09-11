package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Supplier;

public class SaveSupplierEvent extends SaveEntityEvent<Supplier> {

	public SaveSupplierEvent(Supplier entity) {
		super(entity);
	}

}
