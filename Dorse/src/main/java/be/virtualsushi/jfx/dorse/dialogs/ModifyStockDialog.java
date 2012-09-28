package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.IntegerNumberField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SetStockValueEvent;

@Component
public class ModifyStockDialog extends AbstractDialog {

	@FXML
	private VBox container;

	@FXML
	private Label oldValueField;

	@FXML
	private IntegerNumberField newValueField;

	private ValidationErrorPanel validationPanel;

	@FXML
	protected void handleSave(ActionEvent event) {
		container.getChildren().remove(validationPanel);
		if (newValueField.getValue() == null) {
			container.getChildren().add(0, validationPanel);
		} else {
			postSaveEvent(new SetStockValueEvent(newValueField.getValue()));
		}
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();
		validationPanel = new ValidationErrorPanel(false);
		validationPanel.addMessage(getResources().getString("not.null.value"));
	}

	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);

		if (ArrayUtils.isNotEmpty(parameters)) {
			oldValueField.setText(String.valueOf(parameters[0]));
		}
	}

}
