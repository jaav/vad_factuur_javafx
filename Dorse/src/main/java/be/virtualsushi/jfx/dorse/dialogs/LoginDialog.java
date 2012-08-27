package be.virtualsushi.jfx.dorse.dialogs;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginDialog extends AbstractDialog {

	@FXML
	private TextField usernameField, passwordField;

	public LoginDialog(ResourceBundle resources) {
		setTitle(resources.getString("login.dialog.title"));
	}

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		hide();
	}

}
