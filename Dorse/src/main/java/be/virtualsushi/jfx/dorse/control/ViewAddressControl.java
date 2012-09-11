package be.virtualsushi.jfx.dorse.control;

import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import be.virtualsushi.jfx.dorse.fxml.IUiComponent;
import be.virtualsushi.jfx.dorse.model.Address;

public class ViewAddressControl extends GridPane implements IUiComponent, HasValue<Address> {

	private ObjectProperty<ResourceBundle> resourcesProperty;
	private ObjectProperty<Address> valueProperty;

	private FieldLabel addressLabel = new FieldLabel();
	private FieldLabel cityLabel = new FieldLabel();
	private FieldLabel phoneLabel = new FieldLabel();

	private Label addressField = new Label();
	private Label cityField = new Label();
	private Label phoneField = new Label();

	public ViewAddressControl() {
		super();
		resourcesProperty = new SimpleObjectProperty<ResourceBundle>();
		resourcesProperty.addListener(new ChangeListener<ResourceBundle>() {

			@Override
			public void changed(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
				if (newValue != null) {
					bindLabels(newValue);
				}
			}

		});
		valueProperty = new SimpleObjectProperty<Address>();
		valueProperty.addListener(new ChangeListener<Address>() {

			@Override
			public void changed(ObservableValue<? extends Address> observable, Address oldValue, Address newValue) {
				if (newValue != null) {
					addressField.setText(newValue.getAddress());
					cityField.setText(newValue.getZipcode() + " " + newValue.getCity());
					phoneField.setText(newValue.getPhone());
				}
			}
		});
		bindUi();
	}

	private void bindLabels(ResourceBundle resrouces) {
		addressLabel.setValue(resrouces.getString("address"));
		cityLabel.setValue(resrouces.getString("city"));
		phoneLabel.setValue(resrouces.getString("phone"));
	}

	public ObjectProperty<ResourceBundle> resourcesProperty() {
		return resourcesProperty;
	}

	public void setResources(ResourceBundle value) {
		resourcesProperty.set(value);
	}

	public ResourceBundle getResources() {
		return resourcesProperty.get();
	}

	@Override
	public void bindUi() {
		setHgap(10d);
		setVgap(5d);
		add(addressLabel, 0, 0);
		add(addressField, 1, 0);
		add(cityLabel, 0, 1);
		add(cityField, 1, 1);
		add(phoneLabel, 0, 2);
		add(phoneField, 1, 2);
	}

	@Override
	public Node asNode() {
		return this;
	}

	public ObjectProperty<Address> valueProperty() {
		return valueProperty;
	}

	@Override
	public Address getValue() {
		return valueProperty().get();
	}

	@Override
	public void setValue(Address value) {
		valueProperty().set(value);
	}

}
