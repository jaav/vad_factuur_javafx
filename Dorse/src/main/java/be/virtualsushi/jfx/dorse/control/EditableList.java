package be.virtualsushi.jfx.dorse.control;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.Listable;

public class EditableList<V extends Listable> extends HBox implements HasValue<V>, HasValidation, HasMask {

	private DorseComboBox<V> comboBox;
	private AddButton addButton;
	private boolean valid;

	public EditableList() {
		setSpacing(3d);
		comboBox = new DorseComboBox<V>();
		comboBox.getStyleClass().add("field");
		addButton = new AddButton();
		getChildren().add(comboBox);
		getChildren().add(addButton);
    /*comboBox.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        doComboAction(actionEvent);
      }
    });*/
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

	public List<V> getAcceptableValues() {
		return comboBox.getItems();
	}

	public void setEditHandler(EventHandler<ActionEvent> value) {
		addButton.setOnAction(value);
	}

  public void setChangeHandler(EventHandler<ActionEvent> value) {
 		comboBox.setOnAction(value);
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

  /*protected void doComboAction(ActionEvent actionEvent){
    //override me
    System.out.println("actionEvent = " + actionEvent);
  }*/

	@Override
	public void setInvalid() {
		valid = false;
		comboBox.getStyleClass().add("invalid");
	}

	@Override
	public void clearInvalid() {
		valid = true;
		comboBox.getStyleClass().remove("invalid");
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
