package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public abstract class FieldWithValidation<F extends Control, V> extends HBox implements HasValidation, HasValue<V> {

	private Label validationMessage;
	private F field;

	private boolean valid;

	public FieldWithValidation() {
		validationMessage = new Label();
		validationMessage.getStyleClass().add("validation-message");
		field = initializeField();
		field.getStyleClass().add("field");
		clearInvalid();
		getChildren().add(field);
		getChildren().add(validationMessage);
	}

	@Override
	public void setInvalid(String message) {
		valid = false;
		validationMessage.setVisible(true);
		validationMessage.setText(message);
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

	protected abstract F initializeField();

	public DoubleProperty fieldWidthProperty() {
		return field.prefWidthProperty();
	}

	public void setFieldWidth(double width) {
		fieldWidthProperty().set(width);
	}

	public double getFieldWidth() {
		return fieldWidthProperty().get();
	}

	protected F getField() {
		return field;
	}

}
