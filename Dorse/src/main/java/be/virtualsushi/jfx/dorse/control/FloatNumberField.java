package be.virtualsushi.jfx.dorse.control;

public class FloatNumberField extends NumberField<Float> {

	@Override
	protected boolean isDecimal() {
		return true;
	}

	@Override
	protected Float parseValueFromString(String string) {
		return Float.parseFloat(string);
	}

}
