package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;

public class ClearFilterEvent {

	private final Class<? extends AbstractFilterDialog> filterDialogClass;

	public ClearFilterEvent(Class<? extends AbstractFilterDialog> filterDialogClass) {
		this.filterDialogClass = filterDialogClass;
	}

	public Class<? extends AbstractFilterDialog> getDialogClass() {
		return filterDialogClass;
	}

}