package be.virtualsushi.jfx.dorse.dialogs;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSectorEvent;
import be.virtualsushi.jfx.dorse.model.Sector;

@Component
public class NewSectorDialog extends AbstractDialog {

	@FXML
	protected TextField nameField;

	@FXML
	protected ComboBox<Sector> parentSectorField;

	@FXML
	protected void handleSave(ActionEvent event) {
		Sector sector = new Sector();
		sector.setName(nameField.getValue());
		if (parentSectorField.getValue() != null) {
			sector.setParent(parentSectorField.getValue().getId());
		}
		getEventBus().post(new SaveSectorEvent(sector));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onShow(Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters)) {
			mapParentField((List<Sector>) parameters[0]);
		}
	}

	private void mapParentField(List<Sector> list) {
		parentSectorField.getItems().clear();
		parentSectorField.getItems().addAll(list);
	}

}
