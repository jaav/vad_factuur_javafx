package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class EditableList<V> extends HBox implements HasValue<V>, HasValidation, HasMask {

	private ComboBox<V> comboBox;
	private AddButton addButton;
	private Label validationMessage;
	private boolean valid;

	public EditableList() {
		setSpacing(3d);
		comboBox = new ComboBox<V>();
		addButton = new AddButton();
		validationMessage = new Label();
		validationMessage.getStyleClass().add("validation-message");
		getChildren().add(comboBox);
		getChildren().add(addButton);
		getChildren().add(validationMessage);
	}

	@Override
	public V getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(V value) {
		comboBox.setValue(value);
	}

	public void setCellFactory(Callback<ListView<V>, ListCell<V>> value) {
		comboBox.setCellFactory(value);
	}

	public void setAcceptableValues(List<V> values) {
		comboBox.getItems().clear();
		comboBox.getItems().addAll(values);
	}

	public void setEditHandler(EventHandler<ActionEvent> value) {
		addButton.setOnAction(value);
	}

	public DoubleProperty fieldWidthProperty() {
		return comboBox.prefWidthProperty();
	}

	public void setFieldWidth(double width) {
		fieldWidthProperty().set(width);
	}

	public double getFieldWidth() {
		return fieldWidthProperty().get();
	}

	@Override
	public void setInvalid(String message) {
		valid = false;
		validationMessage.setVisible(true);
		validationMessage.setText(message);
	}

	@Override
	public void clearInvalid() {
		valid = true;
		validationMessage.setVisible(false);
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void mask(String maskText) {
		setClip(new Text(20, 20, maskText));
		setDisable(true);
	}

	@Override
	public void unmask() {
		setClip(null);
		setDisable(false);
	}

}