package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;

public class ShowDialogEvent {

	private final Class<? extends AbstractDialog> dialogControllerClass;
	private final String dialogTitle;
	private final Object[] parameters;

	public ShowDialogEvent(String title, Class<? extends AbstractDialog> dialogControllerClass, Object... parameters) {
		this.dialogControllerClass = dialogControllerClass;
		this.dialogTitle = title;
		this.parameters = parameters;
	}

	public Class<? extends AbstractDialog> getDialogControllerClass() {
		return dialogControllerClass;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public Object[] getParameters() {
		return parameters;
	}

}
