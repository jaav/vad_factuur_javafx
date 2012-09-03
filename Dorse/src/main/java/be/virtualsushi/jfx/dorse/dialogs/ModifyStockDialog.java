package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveStockEvent;

@Component
public class ModifyStockDialog extends AbstractDialog {

	@FXML
	private Label oldValueField;

	@FXML
	private TextField newValueField;

	@FXML
	protected void handleSave(ActionEvent event) {
		getEventBus().post(new SaveStockEvent(Integer.parseInt(newValueField.getValue())));
	}

}
