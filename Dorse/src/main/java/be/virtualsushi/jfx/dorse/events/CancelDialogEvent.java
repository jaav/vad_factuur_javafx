package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;

public class CancelDialogEvent {

	private final Class<? extends AbstractDialog> dialogClass;

	public CancelDialogEvent(Class<? extends AbstractDialog> dialogClass) {
		this.dialogClass = dialogClass;
	}

	public Class<? extends AbstractDialog> getDialogClass() {
		return dialogClass;
	}

}
