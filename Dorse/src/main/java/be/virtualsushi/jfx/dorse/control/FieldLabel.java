package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class FieldLabel extends Label {

	public FieldLabel() {
		super();
		setTextAlignment(TextAlignment.RIGHT);
		setAlignment(Pos.CENTER_RIGHT);
	}

	public String getValue() {
		return valueProperty().get();
	}

	public void setValue(String value) {
		valueProperty().set(value + ":");
	}

	public StringProperty valueProperty() {
		return textProperty();
	}

}
