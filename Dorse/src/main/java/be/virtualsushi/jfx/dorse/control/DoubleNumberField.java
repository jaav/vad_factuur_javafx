package be.virtualsushi.jfx.dorse.control;

public class DoubleNumberField extends NumberField<Double> {

	@Override
	protected boolean isDecimal() {
		return true;
	}

	@Override
	protected Double parseValueFromString(String string) {
		return Double.parseDouble(string);
	}

}
