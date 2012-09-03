package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TextArea;

public class TextAreaField extends FieldWithValidation<TextArea, String> {

	@Override
	public String getValue() {
		return getField().getText();
	}

	@Override
	public void setValue(String value) {
		getField().setText(value);
	}

	@Override
	protected TextArea initializeField() {
		TextArea field = new TextArea();
		field.setEditable(true);
		return field;
	}

	public DoubleProperty fieldHeightProperty() {
		return getField().prefHeightProperty();
	}

	public void setFieldHeight(double value) {
		fieldHeightProperty().set(value);
	}

	public double getFieldHeight() {
		return fieldHeightProperty().get();
	}

}
