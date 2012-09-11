package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ViewAddressControl;
import be.virtualsushi.jfx.dorse.dialogs.NewAddressDialog;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Customer;

@Component
@Scope("prototype")
public class ViewCustomerActivity extends AbstractManageEntityActivity<VBox, Customer> {

	@FXML
	protected Label idField, vatField, ibanField, remarkField, title;

	@FXML
	protected TabPane addressesList;

	@Override
	protected void mapFields(Customer viewingEntity) {
		title.setText(viewingEntity.getName());
		idField.setText(String.valueOf(viewingEntity.getId()));
		vatField.setText(viewingEntity.getVat());
		ibanField.setText(viewingEntity.getIban());
		remarkField.setText(viewingEntity.getRemark());
		int addressIndex = 1;
		for (Address address : viewingEntity.getAddress()) {
			Tab tab = new Tab(getResources().getString("address") + " " + String.valueOf(addressIndex));
			ViewAddressControl addressView = new ViewAddressControl();
			addressView.setResources(getResources());
			addressView.setAddressLine1(address.getAddress1());
			addressView.setAddressLine2(address.getAddress2());
			addressView.setLocation(address.getLocation());
			addressView.setPhone(address.getPhone());
			tab.setContent(addressView);
			addressesList.getTabs().add(tab);
		}
	}

	@FXML
	protected void handleAddAddress(ActionEvent event) {
		showDialog(String.format(getResources().getString("new.customer.address.dialog.title"), getEntity().getName()), NewAddressDialog.class);
	}

	@Override
	protected void doCustomBackgroundInitialization(Customer entity) {

	}

}
