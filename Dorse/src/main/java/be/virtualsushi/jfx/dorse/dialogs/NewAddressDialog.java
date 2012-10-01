package be.virtualsushi.jfx.dorse.dialogs;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.model.Address;

@Component
public class NewAddressDialog extends AbstractDialog implements HasValidationDialog {

	@FXML
	private VBox container;

	@FXML
	private TextField addressField, cityField, zipcodeField, phoneField, faxField, emailField;

	private ValidationErrorPanel validationPanel;

	private Integer addressType;

	private Long customer;

	private Map<String, HasValidation> fieldsMap;

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
		address.setCustomer(customer);
		postSaveEvent(new SaveAddressEvent(address));
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();

		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();
		fieldsMap.put("address", addressField);
		fieldsMap.put("city", cityField);
		fieldsMap.put("zipcode", zipcodeField);
		fieldsMap.put("phone", phoneField);
		fieldsMap.put("fax", faxField);
		fieldsMap.put("email", emailField);
	}

	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);

		if (ArrayUtils.isNotEmpty(parameters)) {
			customer = (Long) parameters[0];
			if (parameters.length > 1) {
				addressType = (Integer) parameters[1];
			} else {
				addressType = 0;
			}
		}
	}

	@Override
	public ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	public Map<String, HasValidation> getFieldsMap() {
		return fieldsMap;
	}

	@Override
	public void showValidationPanel() {
		container.getChildren().add(0, validationPanel);
	}

	@Override
	public void hideValidationPanel() {
		container.getChildren().remove(validationPanel);
	}

}
