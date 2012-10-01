package be.virtualsushi.jfx.dorse.dialogs;

import java.util.Map;

import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;

public interface HasValidationDialog {

	ValidationErrorPanel getValidationPanel();

	Map<String, HasValidation> getFieldsMap();

	void showValidationPanel();

	void hideValidationPanel();

}
