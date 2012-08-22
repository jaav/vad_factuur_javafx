package be.virtualsushi.jfx.dorse.dialogs;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import be.virtualsushi.jfx.dorse.DorseFxmlLoader;

import com.zenjava.jfxflow.dialog.Dialog;

public class LoginDialog extends Dialog {

	@FXML
	private TextField usernameField, passwordField;

	public LoginDialog(DorseFxmlLoader fxmlLoader, ResourceBundle resources) {
		setTitle("Log in to VAD Factuur");
		setContent(fxmlLoader.loadView(LoginDialog.class));
	}

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		hide();
	}

}
