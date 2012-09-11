package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Unit;

public class SaveUnitEvent extends SaveEntityEvent<Unit> {

	public SaveUnitEvent(Unit entity) {
		super(entity);
	}

}
