package be.virtualsushi.jfx.dorse.control;

public class IntegerNumberField extends NumberField<Integer> {

	@Override
	protected boolean isDecimal() {
		return false;
	}

	@Override
	protected Integer parseValueFromString(String string) {
		return Integer.parseInt(string);
	}

}
