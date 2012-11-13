package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;

public class ShowFilterDialogEvent {

	private final Class<? extends AbstractFilterDialog> dialogControllerClass;
	private final String dialogTitle;

	public ShowFilterDialogEvent(String title, Class<? extends AbstractFilterDialog> dialogControllerClass) {
		this.dialogControllerClass = dialogControllerClass;
		this.dialogTitle = title;
	}

	public Class<? extends AbstractFilterDialog> getFilterDialogControllerClass() {
		return dialogControllerClass;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

}
