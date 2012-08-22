package be.virtualsushi.jfx.dorse.control;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public abstract class FormField<F extends Control, V> extends HBox implements HasValidation, HasValue<V> {

	private Label label;
	private Label validationMessage;
	private F field;
	private boolean valid = true;

	public FormField() {
		label = new Label();
		validationMessage = new Label();
		validationMessage.setVisible(false);
		field = initializeField();
		getChildren().add(label);
		getChildren().add(field);
		getChildren().add(validationMessage);
	}

	protected F getField() {
		return field;
	}

	@Override
	public void clearInvalid() {
		valid = true;
		validationMessage.setVisible(false);
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void setInvalid(String message) {
		valid = false;
		validationMessage.setText(message);
		validationMessage.setVisible(true);
	}

	protected abstract F initializeField();

}
