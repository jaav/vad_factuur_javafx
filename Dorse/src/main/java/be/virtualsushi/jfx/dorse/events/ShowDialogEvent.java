package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;

public class ShowDialogEvent {

	private final Class<? extends AbstractDialog> dialogControllerClass;
	private final String dialogTitle;

	public ShowDialogEvent(String title, Class<? extends AbstractDialog> dialogControllerClass) {
		this.dialogControllerClass = dialogControllerClass;
		this.dialogTitle = title;
	}

	public Class<? extends AbstractDialog> getDialogControllerClass() {
		return dialogControllerClass;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

}
