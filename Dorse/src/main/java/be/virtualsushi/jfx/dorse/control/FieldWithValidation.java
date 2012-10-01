package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;

public abstract class FieldWithValidation<F extends Control, V> extends HBox implements HasValidation, HasValue<V> {

	private F field;

	private BooleanProperty valid;

	public FieldWithValidation() {
		field = initializeDisplayField();
		field.getStyleClass().add("field");
		getChildren().add(field);
		valid = new SimpleBooleanProperty();
		valid.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue != null && !newValue) {
					field.getStyleClass().add("invalid");
				} else {
					field.getStyleClass().remove("invalid");
				}
			}
		});
		clearInvalid();
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
	public void setInvalid() {
		valid.set(false);
	}

	@Override
	public void clearInvalid() {
		valid.set(true);
	}

	@Override
	public boolean isValid() {
		return valid.get();
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
