package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.EventHandler;
import be.virtualsushi.jfx.dorse.events.authentication.SessionExpiredEvent;

@Component
@Scope("prototype")
@EventHandler
public class EditCustomerActivity extends UiActivity<VBox> {

	@FXML
	protected void handleSave(ActionEvent event) {
		getEventBus().post(new SessionExpiredEvent());
	}

	@FXML
	protected void handleCancel(ActionEvent event) {

	}

}
