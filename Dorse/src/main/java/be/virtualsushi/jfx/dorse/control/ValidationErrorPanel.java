package be.virtualsushi.jfx.dorse.control;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Simple vertical container to display error and validation messages. Contains
 * one title {@link Label} and one or more message {@link Label}s. Styles
 * defined in style.css
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */
public class ValidationErrorPanel extends VBox {

	private Label title;
	private List<Label> messages;

	public ValidationErrorPanel(boolean hasTitle) {
		getStyleClass().add("validation-panel");
		title = new Label();
		title.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		title.getStyleClass().add("validation-title");
		if (hasTitle) {
			getChildren().add(title);
		}
		messages = new ArrayList<Label>();
	}

	public ValidationErrorPanel() {
		this(true);
	}

	public void clearMessages() {
		for (Label message : messages) {
			getChildren().remove(message);
		}
		messages.clear();
	}

	public void setTitleText(String text) {
		title.setText(text);
	}

	public void addMessage(String text) {
		Label message = new Label();
		message.getStyleClass().add("validation-message");
		message.setText(text);
		messages.add(message);
		getChildren().add(message);
	}

	public void setMessageText(String text) {
		clearMessages();
		addMessage(text);
	}
}
