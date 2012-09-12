package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Address;

public class SaveAddressEvent extends SaveEntityEvent<Address> {

	public SaveAddressEvent(Address entity) {
		super(entity);
	}

}
