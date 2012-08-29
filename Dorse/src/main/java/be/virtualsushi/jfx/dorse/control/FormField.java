package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class FormField<F extends Control, V> extends GridPane implements HasValidation, HasValue<V> {

	private Label label;
	private Label validationMessage;
	private F field;
	private boolean valid = true;

	public FormField() {
		setHgap(2d);
		label = new Label();
		validationMessage = new Label();
		validationMessage.setVisible(false);
		field = initializeField();
		add(label, 0, 0);
		add(field, 1, 0);
		add(validationMessage, 2, 0);
		setHalignment(label, HPos.RIGHT);
		setValignment(label, VPos.CENTER);
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

	public String getLabel() {
		return labelProperty().get();
	}

	public void setLabel(String label) {
		labelProperty().set(label + ":");
	}

	public StringProperty labelProperty() {
		return label.textProperty();
	}

	protected abstract F initializeField();

}
