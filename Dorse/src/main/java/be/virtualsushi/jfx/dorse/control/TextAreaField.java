package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.scene.control.TextArea;

public class TextAreaField extends FieldWithValidation<TextArea, String> {

	@Override
	protected TextArea initializeDisplayField() {
		TextArea field = new TextArea();
		field.setEditable(true);
		field.setWrapText(true);
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

	@Override
	public Property<String> valueProperty() {
		return getField().textProperty();
	}

}
