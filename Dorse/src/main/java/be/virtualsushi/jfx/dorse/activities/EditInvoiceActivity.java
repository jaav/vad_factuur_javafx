package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_INVOICES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.VIEW_INVOICE;

import java.util.*;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.model.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
	private DorseComboBox<Customer> customerField;

  @FXML
 	private ComboBoxField<Status> statusField;

	@FXML
	private VBox deliveryAddressBox, invoiceAddressBox;

	@FXML
	private ValidationErrorPanel validationPanel;

	private List<Customer> acceptableCustomers;

  private List<Customer> acceptableStatuses;

	private ToggleGroup deliveryAddressToggleGroup;
	private ToggleGroup invoiceAddressToggleGroup;

	@Override
	public void initialize() {
		super.initialize();
		customerField.valueProperty().addListener(new ChangeListener<Customer>() {

			@Override
			public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
        if(newValue!=null && newValue.getId()>=0)
				  doInBackground(new GetCustomerTaskCreator(newValue.getId()));
			}
		});
		deliveryAddressToggleGroup = new ToggleGroup();
		invoiceAddressToggleGroup = new ToggleGroup();
	}

	@FXML
	protected void handleNewDeliveryAddress(ActionEvent event) {
		if (customerField.getValue() == null) {
			return;
		}
		showDialog(getResources().getString("new.delivery.address.dialog.title"), NewAddressDialog.class, customerField.getValue().getId(), 0);
	}

	@FXML
	protected void handleNewInvoiceAddress(ActionEvent event) {
		if (customerField.getValue() == null) {
			return;
		}
		showDialog(getResources().getString("new.invoice.address.dialog.title"), NewAddressDialog.class, customerField.getValue().getId(), 1);
	}

	private void updateAddressesPanel(Customer newValue) {
		invoiceAddressBox.getChildren().clear();
		deliveryAddressBox.getChildren().clear();
    int counter = 0;
		for (Address address : newValue.getAddress()) {
			AddressesListToggle deliveryToggle = new AddressesListToggle(newValue.getName());
			deliveryToggle.setToggleGroup(deliveryAddressToggleGroup);
			deliveryToggle.setValue(address);
			deliveryAddressBox.getChildren().add(deliveryToggle);
      if(counter == 0){
        deliveryToggle.setSelected(true);
      }
      else{
        if (getEntity().getDeliveryAddress() != null && getEntity().getDeliveryAddress().equals(address.getId())) {
          deliveryToggle.setSelected(true);
        }
      }

			AddressesListToggle invoiceToggle = new AddressesListToggle(newValue.getName());
			invoiceToggle.setToggleGroup(invoiceAddressToggleGroup);
			invoiceToggle.setValue(address);
			invoiceAddressBox.getChildren().add(invoiceToggle);
      if(counter == 0){
        invoiceToggle.setSelected(true);
      }
      else{
        if (getEntity().getInvoiceAddress() != null && getEntity().getInvoiceAddress().equals(address.getId())) {
          invoiceToggle.setSelected(true);
        }
      }
      counter++;
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
		Invoice invoice = new Invoice();
		invoice.setCreationDate(new Date());
		return invoice;
	}

	@Override
	protected void mapFields(Invoice editingInvoice) {
		idField.setText(String.valueOf(editingInvoice.getId()));
		numberField.setValue(editingInvoice.getCode());
		remarkField.setValue(editingInvoice.getRemark());
		customerField.setAcceptableValues(acceptableCustomers);
    statusField.setAcceptableValues(getStatuses());
		if (editingInvoice.getCustomer() != null) {
			mapCustomer(editingInvoice);
		}
	}

	private void mapCustomer(Invoice editingInvoice) {
		for (Customer customer : customerField.getAcceptableValues()) {
			if (editingInvoice.getCustomer().equals(customer)) {
				customerField.setValue(customer);
        mapAddresses(editingInvoice, customer);
				break;
			}
		}
	}

  private void mapAddresses(Invoice editingInvoice, Customer customer){
//    if(editingInvoice.getDeliveryAddress()==null)
//    customer.
  }

	@Override
	protected Invoice getEditedEntity() {
		Invoice result = getEntity();
		result.setCode(numberField.getValue());
		result.setRemark(remarkField.getValue());
		result.setCustomer(customerField.getValue());
		if (invoiceAddressToggleGroup.getSelectedToggle() != null) {
			result.setInvoiceAddress(((AddressesListToggle) invoiceAddressToggleGroup.getSelectedToggle().getUserData()).getValue().getId());
		}
		if (deliveryAddressToggleGroup.getSelectedToggle() != null) {
			result.setDeliveryAddress(((AddressesListToggle) deliveryAddressToggleGroup.getSelectedToggle().getUserData()).getValue().getId());
		}
		if (result.isNew()) {
			result.setCreationDate(new Date());
		}
		result.setDeliveryDate(new Date());
		result.setPaidDate(new Date());
    result.setStatus(statusField.getValue().getId());
		return result;
	}

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Invoice editingEntity) {
    if(acceptableCustomers==null){
      acceptableCustomers = (List<Customer>)getRestApiAccessor().getResponse(Customer.class, Customer.DEFAULT_COLUMN, false, Customer.DEFAULT_ASC).getData();
      acceptableCustomers.add(0, Customer.getEmptyCustomer());
    }
    if(editingEntity.getId()==null){
      List<Invoice> previousInvoice = (List<Invoice>)getRestApiAccessor().getResponse(Invoice.class, null, 0, 1, "id", null, false, false, false).getData();
      if(previousInvoice!=null && !previousInvoice.isEmpty()) editingEntity.setCode(getNextInvoiceCode(previousInvoice.get(0).getCode()));
    }
	}

	@Override
	protected AppActivitiesNames getListActivityName() {
		return LIST_INVOICES;
	}

	@Subscribe
	protected void onSaveAddress(SaveAddressEvent event) {
		Address addressToSave = event.getEntity();
		addressToSave.setCustomer(customerField.getValue().getId());
		doInBackground(new SaveAddressTaskCreator(addressToSave));
	}

	@Override
	protected ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	protected void fillFieldsMap(HashMap<String, HasValidation> fieldsMap) {
		fieldsMap.put("code", numberField);
		fieldsMap.put("remark", remarkField);
	}

  @Override
  protected void clearForm() {
    numberField.setValue("");
    remarkField.setValue("");
    customerField.setValue(Customer.getEmptyCustomer());
    statusField.setValue(Status.getEmptyStatus());
    invoiceAddressBox.getChildren().clear();
    deliveryAddressBox.getChildren().clear();
  }

  private String getNextInvoiceCode(String previousCode){
    Calendar c = new GregorianCalendar();
    String s_thisMonth = null;
    String s_thisYear = ""+c.get(Calendar.YEAR);
    int i_thisMonth = c.get(Calendar.MONTH)+1;
    s_thisMonth = i_thisMonth<10 ? "0"+i_thisMonth : ""+i_thisMonth;
    if(previousCode.indexOf(s_thisMonth)>=0 && previousCode.indexOf(s_thisYear)>=0){
      try{
        int previousCounter = Integer.parseInt(previousCode.substring(6));
        return s_thisYear+s_thisMonth+(previousCounter+1);
      }
      catch (Exception e){
      }
    }
    return s_thisYear+s_thisMonth+"***";
  }
}
