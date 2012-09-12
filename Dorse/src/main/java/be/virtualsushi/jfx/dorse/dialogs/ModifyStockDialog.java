package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.dialogs.SetStockValueEvent;

@Component
public class ModifyStockDialog extends AbstractDialog {

	@FXML
	private Label oldValueField;

	@FXML
	private TextField newValueField;

	@FXML
	protected void handleSave(ActionEvent event) {
		getEventBus().post(new SetStockValueEvent(Integer.parseInt(newValueField.getValue())));
	}

	@Override
	public void onShow(Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters)) {
			oldValueField.setText(String.valueOf(parameters[0]));
		}
	}

}
