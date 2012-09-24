package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import be.virtualsushi.jfx.dorse.model.Listable;

public class ComboBoxField<V extends Listable> extends FieldWithValidation<DorseComboBox<V>, V> {

	@Override
	protected DorseComboBox<V> initializeDisplayField() {
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

	@Override
	public ObjectProperty<V> valueProperty() {
		return getField().valueProperty();
	}

}
