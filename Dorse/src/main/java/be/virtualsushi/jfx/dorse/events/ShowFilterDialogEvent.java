package be.virtualsushi.jfx.dorse.events;

import be.virtualsushi.jfx.dorse.dialogs.AbstractDialog;
import be.virtualsushi.jfx.dorse.dialogs.AbstractFilterDialog;

public class ShowFilterDialogEvent {

	private final Class<? extends AbstractFilterDialog> dialogControllerClass;
	private final String dialogTitle;
  private final Object[] parameters;

	public ShowFilterDialogEvent(String title, Class<? extends AbstractFilterDialog> dialogControllerClass, Object... parameters) {
		this.dialogControllerClass = dialogControllerClass;
		this.dialogTitle = title;
    this.parameters = parameters;
	}

	public Class<? extends AbstractFilterDialog> getFilterDialogControllerClass() {
		return dialogControllerClass;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

  public Object[] getParameters() {
 		return parameters;
 	}

}
