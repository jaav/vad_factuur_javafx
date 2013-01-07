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

	/*private FieldLabel addressLabel = new FieldLabel();
	private FieldLabel cityLabel = new FieldLabel();
	private FieldLabel phoneLabel = new FieldLabel();
	private FieldLabel emailLabel = new FieldLabel();*/

	private Label nameField = new Label();
  private Label addressField = new Label();
	private Label cityField = new Label();
	private Label phoneField = new Label();
	private Label emailField = new Label();
  private Label attField = new Label();

	public ViewAddressControl() {
		super();
    //this.setStyle("-fx-background-color:red;");
		resourcesProperty = new SimpleObjectProperty<ResourceBundle>();
		/*resourcesProperty.addListener(new ChangeListener<ResourceBundle>() {

			@Override
			public void changed(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
				if (newValue != null) {
					bindLabels(newValue);
				}
			}

		});*/
		valueProperty = new SimpleObjectProperty<Address>();
		valueProperty.addListener(new ChangeListener<Address>() {

			@Override
			public void changed(ObservableValue<? extends Address> observable, Address oldValue, Address newValue) {
				if (newValue != null) {
          addressField.setText(newValue.getAddress());
					cityField.setText(newValue.getZipcode() + " " + newValue.getCity());
					phoneField.setText(newValue.getPhone());
					emailField.setText(newValue.getEmail());
          attField.setText(newValue.getAtt());
				}
			}
		});
		bindUi();
	}

  public void setTitle(String title){
    nameField.setText(title);
  }

	private void bindLabels(ResourceBundle resrouces) {
		/*addressLabel.setValue(resrouces.getString("address"));
		cityLabel.setValue(resrouces.getString("city"));
		phoneLabel.setValue(resrouces.getString("phone"));
		emailLabel.setValue(resrouces.getString("email"));*/
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
		//add(addressLabel, 0, 0);
		add(nameField, 0, 0);
    add(attField, 0, 1);
    add(addressField, 0, 2);
		//add(cityLabel, 0, 1);
		add(cityField, 0, 3);
		//add(phoneLabel, 0, 2);
		add(phoneField, 0, 4);
		//add(emailLabel, 0, 3);
		add(emailField, 0, 5);
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
