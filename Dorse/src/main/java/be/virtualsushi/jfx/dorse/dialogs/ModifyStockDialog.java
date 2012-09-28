package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.events.dialogs.SaveStockEvent;
import be.virtualsushi.jfx.dorse.model.Stock;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;

@Component
public class ModifyStockDialog extends AbstractDialog {

	@FXML
	private Label idField;

  @FXML
 	private Label oldValueField;

	@FXML
	private TextField newValueField;

	@FXML
	protected void handleSave(ActionEvent event) {
    Stock stock = new Stock();
    stock.setQuantity(Integer.parseInt(newValueField.getValue()));
    if(StringUtils.isNotBlank(idField.getText())) stock.setId(Long.parseLong(idField.getText()));
		getEventBus().post(new SaveStockEvent(stock));
	}

	@Override
	public void onShow(Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters) && parameters[0]!=null) {
			oldValueField.setText(String.valueOf(((Stock)parameters[0]).getQuantity()));
      idField.setText(String.valueOf(((Stock)parameters[0]).getId()));
		}
	}

}
