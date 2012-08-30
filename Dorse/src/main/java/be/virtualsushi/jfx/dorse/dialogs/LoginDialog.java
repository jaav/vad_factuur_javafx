package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.EventHandler;
import be.virtualsushi.jfx.dorse.events.authentication.LoginEvent;
import be.virtualsushi.jfx.dorse.events.authentication.LoginFailedEvent;

import com.google.common.eventbus.Subscribe;

@Component
@EventHandler
public class LoginDialog extends AbstractDialog {

	@FXML
	private TextField usernameField, passwordField;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		getEventBus().post(new LoginEvent(usernameField.getText(), passwordField.getText()));
	}

	@Subscribe
	public void onLoginFailed(LoginFailedEvent event) {

	}

}
