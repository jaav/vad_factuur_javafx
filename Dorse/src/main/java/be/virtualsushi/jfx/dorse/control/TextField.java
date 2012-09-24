package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.Property;

public class TextField extends FieldWithValidation<javafx.scene.control.TextField, String> {

	@Override
	protected javafx.scene.control.TextField initializeDisplayField() {
		return new javafx.scene.control.TextField();
	}

	@Override
	public Property<String> valueProperty() {
		return getField().textProperty();
	}

}
