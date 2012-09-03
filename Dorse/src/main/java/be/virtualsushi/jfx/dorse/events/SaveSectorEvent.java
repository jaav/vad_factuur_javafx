package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.model.Sector;

public class SaveSectorEvent {

	private final Sector sector;

	public SaveSectorEvent(Sector sector) {
		this.sector = sector;
	}

	public Sector getSector() {
		return sector;
	}

}
