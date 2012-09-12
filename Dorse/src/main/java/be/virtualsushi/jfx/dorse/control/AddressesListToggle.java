package be.virtualsushi.jfx.dorse.control;

import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import be.virtualsushi.jfx.dorse.model.Address;

public class AddressesListToggle extends HBox implements Toggle, HasValue<Address> {

	private RadioButton button;
	private ViewAddressControl addressView;

	private ObjectProperty<ToggleGroup> toggleGroup;
	private BooleanProperty selected;

	public AddressesListToggle() {
		button = new RadioButton();
		addressView = new ViewAddressControl();
		toggleGroup = new SimpleObjectProperty<ToggleGroup>();
		toggleGroup.bindBidirectional(button.toggleGroupProperty());
		selected = new SimpleBooleanProperty();
		selected.bindBidirectional(button.selectedProperty());
		setSpacing(10d);
		getChildren().add(button);
		getChildren().add(addressView);
		button.setUserData(this);
	}

	@Override
	public ToggleGroup getToggleGroup() {
		return toggleGroupProperty().get();
	}

	@Override
	public boolean isSelected() {
		return selectedProperty().get();
	}

	@Override
	public BooleanProperty selectedProperty() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		selectedProperty().set(selected);
	}

	@Override
	public void setToggleGroup(ToggleGroup toggleGroup) {
		toggleGroupProperty().set(toggleGroup);
	}

	@Override
	public ObjectProperty<ToggleGroup> toggleGroupProperty() {
		return toggleGroup;
	}

	@Override
	public Address getValue() {
		return valueProperty().get();
	}

	@Override
	public void setValue(Address value) {
		valueProperty().set(value);
	}

	public ObjectProperty<Address> valueProperty() {
		return addressView.valueProperty();
	}

	public ObjectProperty<ResourceBundle> resourcesProperty() {
		return addressView.resourcesProperty();
	}

	public void setResources(ResourceBundle value) {
		resourcesProperty().set(value);
	}

	public ResourceBundle getResources() {
		return resourcesProperty().get();
	}
}
