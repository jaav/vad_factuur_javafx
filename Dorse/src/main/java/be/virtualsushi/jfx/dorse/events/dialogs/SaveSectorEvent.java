package be.virtualsushi.jfx.dorse.events.dialogs;

import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Sector;

public class SaveSectorEvent extends SaveEntityEvent<Sector> {

	public SaveSectorEvent(Sector entity) {
		super(entity);
	}

}
