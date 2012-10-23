package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.IntegerNumberField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveStockEvent;
import be.virtualsushi.jfx.dorse.model.Stock;

@Component
public class ModifyStockDialog extends AbstractDialog {

	@FXML
	private Label oldValueField;

	@FXML
	private IntegerNumberField newValueField;

	private ValidationErrorPanel validationPanel;

	@FXML
	protected void handleSave(ActionEvent event) {
		Stock stock = new Stock();
		stock.setQuantity(newValueField.getValue());
    stock.setId(idField.getValue());
		getEventBus().post(new SaveStockEvent(stock));
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();
		validationPanel = new ValidationErrorPanel(false);
		validationPanel.addMessage(getResources().getString("not.null.value"));
	}

	@Override
	public void onShow(Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters) && parameters[0] != null) {
			oldValueField.setText(String.valueOf(((Stock) parameters[0]).getQuantity()));
			idField.setValue(((Stock) parameters[0]).getId());
		}
	}

}
