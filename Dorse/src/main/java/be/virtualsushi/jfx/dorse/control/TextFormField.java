package be.virtualsushi.jfx.dorse.control;

import javafx.scene.control.TextField;

public class TextFormField extends FormField<TextField, String> {

	@Override
	protected TextField initializeField() {
		return new TextField();
	}

	@Override
	public String getValue() {
		return getField().getText();
	}

	@Override
	public void setValue(String value) {
		getField().setText(value);
	}

}
