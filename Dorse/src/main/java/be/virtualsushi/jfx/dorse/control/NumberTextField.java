package be.virtualsushi.jfx.dorse.control;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * a {@link TextField} with only digital input allowed. JavaFX's
 * {@link TextField} does not have such feature by default yet.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class NumberTextField extends TextField {

	private boolean decimalInput;

	public NumberTextField(boolean decimalInput) {
		super();
		this.decimalInput = decimalInput;
		setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (!inputAllowed(event.getCode().ordinal())) {
					String text = getText();
					if (text.length() > 0) {
						setText(text.substring(0, text.length() - 1));
						positionCaret(text.length() - 1);
					}
				}
			}

		});
	}

	/**
	 * Checks if pressed symbol is allowed to be processed.
	 * 
	 * @param keyCodeOrd
	 *            - code of the pressed key
	 * @return
	 */
	private boolean inputAllowed(int keyCodeOrd) {
		if (keyCodeOrd >= KeyCode.DIGIT0.ordinal() && keyCodeOrd <= KeyCode.DIGIT9.ordinal()) {
			return true;
		}
		if (keyCodeOrd >= KeyCode.NUMPAD0.ordinal() && keyCodeOrd <= KeyCode.NUMPAD9.ordinal()) {
			return true;
		}
		if (keyCodeOrd == KeyCode.BACK_SPACE.ordinal() || keyCodeOrd == KeyCode.DELETE.ordinal()) {
			return true;
		}
		if (keyCodeOrd == KeyCode.PERIOD.ordinal() && getText().lastIndexOf(".") == getText().indexOf(".") && decimalInput) {
			return true;
		}
		if (keyCodeOrd == KeyCode.LEFT.ordinal() || keyCodeOrd == KeyCode.RIGHT.ordinal() || keyCodeOrd == KeyCode.UP.ordinal() || keyCodeOrd == KeyCode.DOWN.ordinal()) {
			return true;
		}
		return false;
	}

}
