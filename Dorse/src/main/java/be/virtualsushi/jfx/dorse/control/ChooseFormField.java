package be.virtualsushi.jfx.dorse.control;

import javafx.scene.control.ChoiceBox;

public class ChooseFormField extends FormField<ChoiceBox<String>, String> {

	@Override
	public String getValue() {
		return getField().getSelectionModel().getSelectedItem();
	}

	@Override
	public void setValue(String value) {
		getField().getSelectionModel().select(value);
	}

	@Override
	protected ChoiceBox<String> initializeField() {
		return new ChoiceBox<String>();
	}

}
