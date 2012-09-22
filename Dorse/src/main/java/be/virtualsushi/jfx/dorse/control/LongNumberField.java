package be.virtualsushi.jfx.dorse.control;

public class LongNumberField extends NumberField<Long> {

	@Override
	protected boolean isDecimal() {
		return false;
	}

	@Override
	protected Long parseValueFromString(String string) {
		return Long.parseLong(string);
	}

}
