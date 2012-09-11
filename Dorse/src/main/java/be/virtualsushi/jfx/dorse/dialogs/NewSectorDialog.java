package be.virtualsushi.jfx.dorse.dialogs;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Sector;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

@Component
public class NewSectorDialog extends AbstractDialog {

	@Autowired
	private RestApiAccessor restApiAccessor;

	@FXML
	protected TextField nameField;

	@FXML
	protected ComboBox<Sector> parentSectorField;

	@FXML
	protected void handleSave(ActionEvent event) {
		getEventBus().post(new SaveEntityEvent<Sector>(null));
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
