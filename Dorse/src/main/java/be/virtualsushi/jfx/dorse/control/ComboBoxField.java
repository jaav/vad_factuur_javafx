package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import be.virtualsushi.jfx.dorse.model.Listable;

public class ComboBoxField<V extends Listable> extends FieldWithValidation<DorseComboBox<V>, V> {

	@Override
	public V getValue() {
		return valueProperty().get();
	}

	@Override
	public void setValue(V value) {
		valueProperty().set(value);
	}

	@Override
	protected DorseComboBox<V> initializeField() {
		return new DorseComboBox<V>();
	}

	public DoubleProperty fieldWidthProperty() {
		return getField().prefWidthProperty();
	}

	public void setFieldWidth(double width) {
		fieldWidthProperty().set(width);
	}

	public double getFieldWidth() {
		return fieldWidthProperty().get();
	}

	public void setAcceptableValues(List<V> acceptableValues) {
		getField().setAcceptableValues(acceptableValues);
	}

	public List<V> getAcceptableValues() {
		return getField().getAcceptableValues();
	}

	public ObjectProperty<V> valueProperty() {
		return getField().valueProperty();
	}

}
