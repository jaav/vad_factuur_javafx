package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;

import org.apache.commons.lang3.StringUtils;

public abstract class NumberField<N extends Number> extends FieldWithValidation<NumberTextField, N> {

	private ObjectProperty<N> valueProperty;

	public NumberField() {
		super();
		valueProperty = new SimpleObjectProperty<N>();
		getField().textProperty().bindBidirectional(valueProperty, new StringConverter<N>() {

			@Override
			public N fromString(String string) {
				if (StringUtils.isEmpty(string)) {
					return null;
				} else {
					return parseValueFromString(string);
				}
			}

			@Override
			public String toString(N object) {
				if (object == null) {
					return "";
				}
				return String.valueOf(object);
			}
		});
	}

	@Override
	protected NumberTextField initializeDisplayField() {
		return new NumberTextField(isDecimal());
	}

	@Override
	public Property<N> valueProperty() {
		return valueProperty;
	}

	protected abstract boolean isDecimal();

	protected abstract N parseValueFromString(String string);

}
