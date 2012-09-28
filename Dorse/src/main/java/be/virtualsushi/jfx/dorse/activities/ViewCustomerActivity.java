package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
public class ViewCustomerActivity extends AbstractViewEntityActivity<VBox, Customer> {

	@FXML
	protected Label idField, vatField, ibanField, remarkField;

	@FXML
	protected TabPane addressesList;

	@Override
	protected void mapFields(Customer viewingEntity) {
		super.mapFields(viewingEntity);
		title.setText(viewingEntity.getName());
		idField.setText(String.valueOf(viewingEntity.getId()));
		vatField.setText(viewingEntity.getVat());
		ibanField.setText(viewingEntity.getIban());
		remarkField.setText(viewingEntity.getRemark());
		int addressIndex = 1;
		for (Address address : viewingEntity.getAddress()) {
			Tab tab = new Tab(getResources().getString("address") + " " + String.valueOf(addressIndex));
			ViewAddressControl addressView = new ViewAddressControl();
			addressView.setPadding(new Insets(10));
			addressView.setResources(getResources());
			addressView.setValue(address);
			tab.setContent(addressView);
			addressesList.getTabs().add(tab);
			addressIndex++;
		}
	}

	@FXML
	protected void handleAddAddress(ActionEvent event) {
		showDialog(String.format(getResources().getString("new.customer.address.dialog.title"), getEntity().getName()), NewAddressDialog.class, getEntity().getId());
	}

	@Override
	protected void doCustomBackgroundInitialization(Customer entity) {

	}

}
