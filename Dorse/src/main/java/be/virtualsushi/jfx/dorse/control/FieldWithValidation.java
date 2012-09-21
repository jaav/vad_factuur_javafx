package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
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
		field = initializeDisplayField();
		field.getStyleClass().add("field");
		clearInvalid();
		getChildren().add(field);
		getChildren().add(validationMessage);
	}

	@Override
	public V getValue() {
		return valueProperty().getValue();
	}

	@Override
	public void setValue(V value) {
		valueProperty().setValue(value);
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

	protected abstract F initializeDisplayField();

	public abstract Property<V> valueProperty();

}
