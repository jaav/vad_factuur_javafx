package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.DoubleProperty;
import be.virtualsushi.jfx.dorse.model.Listable;

public class ComboBoxField<V extends Listable> extends FieldWithValidation<DorseComboBox<V>, V> {

	@Override
	public V getValue() {
		return getField().getValue();
	}

	@Override
	public void setValue(V value) {
		getField().setValue(value);
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

}
