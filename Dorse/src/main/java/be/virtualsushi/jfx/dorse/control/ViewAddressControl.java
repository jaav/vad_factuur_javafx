package be.virtualsushi.jfx.dorse.control;

import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import be.virtualsushi.jfx.dorse.fxml.IUiComponent;

public class ViewAddressControl extends GridPane implements IUiComponent {

	private ObjectProperty<ResourceBundle> resourcesProperty;

	private FieldLabel addressLine1Label = new FieldLabel();
	private FieldLabel addressLine2Label = new FieldLabel();
	private FieldLabel locationLabel = new FieldLabel();
	private FieldLabel phoneLabel = new FieldLabel();
	private FieldLabel faxLabel = new FieldLabel();
	private FieldLabel emailLabel = new FieldLabel();

	private Label addressLine1Field = new Label();
	private Label addressLine2Field = new Label();
	private Label locationField = new Label();
	private Label phoneField = new Label();
	private Label faxField = new Label();
	private Label emailField = new Label();

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
		bindUi();
	}

	private void bindLabels(ResourceBundle resrouces) {
		addressLine1Label.setValue(resrouces.getString("address.line.1"));
		addressLine2Label.setValue(resrouces.getString("address.line.2"));
		locationLabel.setValue(resrouces.getString("location.label"));
		phoneLabel.setValue(resrouces.getString("phone.label"));
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
		add(addressLine1Label, 0, 0);
		add(addressLine1Field, 1, 0);
		add(addressLine2Label, 0, 1);
		add(addressLine2Field, 1, 1);
		add(locationLabel, 0, 2);
		add(locationField, 1, 2);
		add(phoneLabel, 0, 3);
		add(phoneField, 1, 3);
		// TODO do we need this lines?
		// add(faxLabel, 0, 4);
		// add(faxField, 1, 4);
		// add(emailLabel, 0, 5);
		// add(emailField, 1, 5);
	}

	@Override
	public Node asNode() {
		return this;
	}

	public StringProperty addressLine1Property() {
		return addressLine1Field.textProperty();
	}

	public void setAddressLine1(String value) {
		addressLine1Property().set(value);
	}

	public String getAddressLine1() {
		return addressLine1Property().get();
	}

	public StringProperty addressLine2Property() {
		return addressLine2Field.textProperty();
	}

	public void setAddressLine2(String value) {
		addressLine1Property().set(value);
	}

	public String getAddressLine2() {
		return addressLine1Property().get();
	}

	public StringProperty locationProperty() {
		return locationField.textProperty();
	}

	public void setLocation(String value) {
		locationProperty().set(value);
	}

	public String getLocation() {
		return locationProperty().get();
	}

	public StringProperty phoneProperty() {
		return phoneField.textProperty();
	}

	public void setPhone(String value) {
		phoneProperty().set(value);
	}

	public String getPhone() {
		return phoneProperty().get();
	}

}
