package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.authentication.LoginSuccessfulEvent;
import be.virtualsushi.jfx.dorse.restapi.RestApiAccessor;

@Component
public class LoginDialog extends AbstractDialog {

	@Autowired
	private RestApiAccessor restApiAccessor;

	@FXML
	private TextField usernameField, passwordField;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		String authToken = restApiAccessor.login(usernameField.getText(), passwordField.getText());
		if (StringUtils.isNotBlank(authToken)) {
			getEventBus().post(new LoginSuccessfulEvent(authToken));
		} else {
			// TODO display error message
		}
	}

}
