package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveSectorEvent;
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
		getEventBus().post(new SaveSectorEvent(null));
	}

	@Override
	public void onShow() {
		// TODO load sectors list.
	}

}
