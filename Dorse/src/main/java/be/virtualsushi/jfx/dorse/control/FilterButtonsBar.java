package be.virtualsushi.jfx.dorse.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class FilterButtonsBar extends HBox {

	private Button saveButton;
	private Button cancelButton;
  private Button clearButton;

	public FilterButtonsBar() {
		super();
		setSpacing(10);
		saveButton = new Button();
		cancelButton = new Button();
    clearButton = new Button();
    getChildren().add(clearButton);
		getChildren().add(cancelButton);
    getChildren().add(saveButton);
	}

	public ObjectProperty<EventHandler<ActionEvent>> onSaveActionProperty() {
		return saveButton.onActionProperty();
	}

	public ObjectProperty<EventHandler<ActionEvent>> onCancelActionProperty() {
		return cancelButton.onActionProperty();
	}   

  public ObjectProperty<EventHandler<ActionEvent>> onClearActionProperty() {
 		return clearButton.onActionProperty();
 	}

	public EventHandler<ActionEvent> getOnSaveAction() {
		return onSaveActionProperty().get();
	}

	public void setOnSaveAction(EventHandler<ActionEvent> value) {
		onSaveActionProperty().set(value);
	}

	public EventHandler<ActionEvent> getOnCancelAction() {
		return onCancelActionProperty().get();
	}

	public void setOnCancelAction(EventHandler<ActionEvent> value) {
		onCancelActionProperty().set(value);
	}   

  public EventHandler<ActionEvent> getOnClearAction() {
 		return onClearActionProperty().get();
 	}
 
 	public void setOnClearAction(EventHandler<ActionEvent> value) {
 		onClearActionProperty().set(value);
 	}

	public StringProperty saveButtonTitleProperty() {
		return saveButton.textProperty();
	}

	public StringProperty cancelButtonTitleProperty() {
		return cancelButton.textProperty();
	}

  public StringProperty clearButtonTitleProperty() {
 		return clearButton.textProperty();
 	}

	public String getSaveButtonTitle() {
		return saveButtonTitleProperty().get();
	}

	public void setSaveButtonTitle(String value) {
		saveButtonTitleProperty().set(value);
	}

	public String getCancelButtonTitle() {
		return cancelButtonTitleProperty().get();
	}

	public void setCancelButtonTitle(String value) {
		cancelButtonTitleProperty().set(value);
	}

  public String getClearButtonTitle() {
 		return clearButtonTitleProperty().get();
 	}
 
 	public void setClearButtonTitle(String value) {
 		clearButtonTitleProperty().set(value);
 	}

}
