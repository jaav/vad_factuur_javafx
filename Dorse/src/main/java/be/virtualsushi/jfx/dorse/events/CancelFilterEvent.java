package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;

public class CancelFilterEvent {

	private final Class<? extends AbstractFilterDialog> filterDialogClass;

	public CancelFilterEvent(Class<? extends AbstractFilterDialog> filterDialogClass) {
		this.filterDialogClass = filterDialogClass;
	}

	public Class<? extends AbstractFilterDialog> getDialogClass() {
		return filterDialogClass;
	}

}
