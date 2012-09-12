package be.virtualsushi.jfx.dorse.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.model.Address;

@Component
public class NewAddressDialog extends AbstractDialog {

	@FXML
	private TextField addressField, cityField, zipcodeField, phoneField, faxField, emailField;

	private Integer addressType;

	@FXML
	protected void handleSave(ActionEvent event) {
		Address address = new Address();
		address.setAddress(addressField.getValue());
		address.setZipcode(addressField.getValue());
		address.setCity(cityField.getValue());
		address.setPhone(phoneField.getValue());
		address.setFax(faxField.getValue());
		address.setEmail(emailField.getValue());
		address.setAddressType(addressType);
		getEventBus().post(new SaveAddressEvent(address));
	}

	@Override
	public void onShow(Object... parameters) {
		if (ArrayUtils.isNotEmpty(parameters)) {
			addressType = (Integer) parameters[0];
		}
	}

}
