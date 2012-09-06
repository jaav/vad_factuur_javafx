package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.Address;

@Component
public class NewAddressDialog extends AbstractDialog {

	@FXML
	private TextField addressLine1Field, addressLine2Field, locationField, phoneField, faxField, emailField;

	@FXML
	protected void handleSave(ActionEvent event) {
		Address address = new Address();
		address.setAddress1(addressLine1Field.getValue());
		address.setAddress2(addressLine2Field.getValue());
		address.setLocation(locationField.getValue());
		address.setPhone(phoneField.getValue());
		address.setFax(faxField.getValue());
		address.setEmail(emailField.getValue());
		getEventBus().post(new SaveEntityEvent<Address>(address));
	}

}
