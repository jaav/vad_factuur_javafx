package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;

public class CancelStatsEvent {

	private final Class statsFilterDialogClass;

	public CancelStatsEvent(Class statsFilterDialogClass) {
		this.statsFilterDialogClass = statsFilterDialogClass;
	}

	public Class getDialogClass() {
		return statsFilterDialogClass;
	}

}
