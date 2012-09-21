package be.virtualsushi.jfx.dorse.control;

import java.lang.reflect.ParameterizedType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;

public class NumberField<N extends Number> extends FieldWithValidation<NumberTextField, N> {

	private ObjectProperty<N> valueProperty;
	
	public NumberField() {
		super();
		valueProperty = new SimpleObjectProperty<N>();
		getField().textProperty().bindBidirectional(valueProperty, new StringConverter<N>() {

			@SuppressWarnings("unchecked")
			@Override
			public N fromString(String string) {
				if (isValueClassAssignableFrom(Long.class)) {
					return (N) new Long(string);
				}
				if (isValueClassAssignableFrom(Integer.class)) {
					return (N) new Integer(string);
				}
				if (isValueClassAssignableFrom(Float.class)) {
					return (N) new Float(string);
				}
				return (N) new Double(string);
			}

			@Override
			public String toString(N object) {
				if (object == null) {
					return "0";
				}
				return String.valueOf(object);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private boolean isValueClassAssignableFrom(Class<? extends Number> checkClass) {
		return ((Class<N>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]).isAssignableFrom(checkClass);
	}

	@Override
	protected NumberTextField initializeDisplayField() {
		return new NumberTextField(isValueClassAssignableFrom(Double.class) || isValueClassAssignableFrom(Float.class));
	}

	@Override
	public Property<N> valueProperty() {
		return valueProperty;
	}

}
