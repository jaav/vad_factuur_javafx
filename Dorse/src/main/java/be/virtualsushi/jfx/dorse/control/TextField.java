package be.virtualsushi.jfx.dorse.control;

public class TextField extends FieldWithValidation<javafx.scene.control.TextField, String> {

	@Override
	public String getValue() {
		return getField().getText();
	}

	@Override
	public void setValue(String value) {
		getField().setText(value);
	}

	@Override
	protected javafx.scene.control.TextField initializeField() {
		return new javafx.scene.control.TextField();
	}

}
