package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.VIEW_INVOICE;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.AddressesListToggle;
import be.virtualsushi.jfx.dorse.control.ComboBoxField;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.dialogs.NewAddressDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditInvoiceActivity extends AbstractEditActivity<VBox, Invoice> {

	private class GetCustomerTaskCreator implements TaskCreator<DorseBackgroundTask<Customer>> {

		private final Long customerId;

		public GetCustomerTaskCreator(Long customerId) {
			this.customerId = customerId;
		}

		@Override
		public DorseBackgroundTask<Customer> createTask() {
			return new DorseBackgroundTask<Customer>(this, customerId) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected Customer call() throws Exception {
					return getRestApiAccessor().get((Long) getParameters()[0], Customer.class);
				}

				@Override
				protected void onSuccess(Customer value) {
					updateAddressesPanel(value);
					hideLoadingMask();
				}

			};
		}

	}

	private class SaveAddressTaskCreator implements TaskCreator<DorseBackgroundTask<Customer>> {

		private final Address address;

		public SaveAddressTaskCreator(Address addressToSave) {
			this.address = addressToSave;
		}

		@Override
		public DorseBackgroundTask<Customer> createTask() {
			return new DorseBackgroundTask<Customer>(this, address) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected Customer call() throws Exception {
					getRestApiAccessor().save((Address) getParameters()[0]);
					return getRestApiAccessor().get(((Address) getParameters()[0]).getCustomer(), Customer.class);
				}

				@Override
				protected void onSuccess(Customer value) {
					updateAddressesPanel(value);
					hideLoadingMask();
				}
			};
		}

	}

	@FXML
	private Label idField;

	@FXML
	private TextField numberField;

	@FXML
	private TextAreaField remarkField;

	@FXML
	private ComboBoxField<Customer> customerField;

	@FXML
	private VBox deliveryAddressBox, invoiceAddressBox;

	private List<Customer> acceptableCustomers;

	private ToggleGroup deliveryAddressToggleGroup;
	private ToggleGroup invoiceAddressToggleGroup;

	@Override
	public void initialize() {
		super.initialize();
		customerField.valueProperty().addListener(new ChangeListener<Customer>() {

			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
				doInBackground(new GetCustomerTaskCreator(newValue.getId()));
			}
		});
		deliveryAddressToggleGroup = new ToggleGroup();
		invoiceAddressToggleGroup = new ToggleGroup();
	}

	@FXML
	protected void handleNewDeliveryAddress(ActionEvent event) {
		showDialog(getResources().getString("new.delivery.address.dialog.title"), NewAddressDialog.class, 0);
	}

	@FXML
	protected void handleNewInvoiceAddress(ActionEvent event) {
		showDialog(getResources().getString("new.invoice.address.dialog.title"), NewAddressDialog.class, 1);
	}

	private void updateAddressesPanel(Customer newValue) {
		invoiceAddressBox.getChildren().clear();
		deliveryAddressBox.getChildren().clear();
		for (Address address : newValue.getAddress()) {
			AddressesListToggle deliveryToggle = new AddressesListToggle();
			deliveryToggle.setToggleGroup(deliveryAddressToggleGroup);
			deliveryToggle.setValue(address);
			deliveryAddressBox.getChildren().add(deliveryToggle);
			if (getEntity().getDeliveryAddress() != null && getEntity().getDeliveryAddress() == address.getId()) {
				deliveryToggle.setSelected(true);
			}

			AddressesListToggle invoiceToggle = new AddressesListToggle();
			invoiceToggle.setToggleGroup(invoiceAddressToggleGroup);
			invoiceToggle.setValue(address);
			invoiceAddressBox.getChildren().add(invoiceToggle);
			if (getEntity().getInvoiceAddress() != null && getEntity().getInvoiceAddress() == address.getId()) {
				invoiceToggle.setSelected(true);
			}
		}
	}

	@Override
	protected String getEditTitleKey() {
		return "edit.invoice";
	}

	@Override
	protected String getNewTitleKey() {
		return "new.invoice";
	}

	@Override
	protected Invoice newEntityInstance() {
		return new Invoice();
	}

	@Override
	protected void mapFields(Invoice editingInvoice) {
		idField.setText(String.valueOf(editingInvoice.getId()));
		numberField.setValue(editingInvoice.getCode());
		remarkField.setValue(editingInvoice.getRemark());
		customerField.setAcceptableValues(acceptableCustomers);
		if (editingInvoice.getCustomer() != null) {
			mapCustomer(editingInvoice);
		}
	}

	private void mapCustomer(Invoice editingInvoice) {
		for (Customer customer : customerField.getAcceptableValues()) {
			if (editingInvoice.getCustomer() == customer.getId()) {
				customerField.setValue(customer);
				break;
			}
		}
	}

	@Override
	protected Invoice getEditedEntity() {
		Invoice result = getEntity();
		result.setCode(numberField.getValue());
		result.setRemark(remarkField.getValue());
		result.setCustomer(customerField.getValue().getId());
		result.setInvoiceAddress(((AddressesListToggle) invoiceAddressToggleGroup.getSelectedToggle().getUserData()).getValue().getId());
		result.setDeliveryAddress(((AddressesListToggle) deliveryAddressToggleGroup.getSelectedToggle().getUserData()).getValue().getId());
		return result;
	}

	@Override
	protected void doCustomBackgroundInitialization(Invoice editingEntity) {
		acceptableCustomers = getRestApiAccessor().getList(Customer.class, Customer[].class, false);
	}

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return VIEW_INVOICE;
	}

	@Subscribe
	protected void onSaveAddress(SaveAddressEvent event) {
		Address addressToSave = event.getEntity();
		addressToSave.setCustomer(customerField.getValue().getId());
		doInBackground(new SaveAddressTaskCreator(addressToSave));
	}

}
