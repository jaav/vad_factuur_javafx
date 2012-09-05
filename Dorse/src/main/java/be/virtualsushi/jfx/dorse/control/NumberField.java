package be.virtualsushi.jfx.dorse.control;

public abstract class NumberField<N extends Number> extends FieldWithValidation<NumberTextField, N> {

	@Override
	public N getValue() {
		return parseValue(getField().getText());
	}

	@Override
	public void setValue(N value) {
		getField().setText(String.valueOf(value));
	}

	@Override
	protected NumberTextField initializeField() {
		return new NumberTextField();
	}

	protected abstract N parseValue(String value);

}
