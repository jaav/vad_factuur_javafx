package be.virtualsushi.jfx.dorse.activities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ComboBoxField;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;

import com.zenjava.jfxflow.worker.BackgroundTask;

@Component
@Scope("prototype")
public class EditInvoiceActivity extends AbstractEditActivity<VBox, Invoice> {

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

	@Override
	protected void started() {
		super.started();

		showLoadingMask();
		executeTask(new BackgroundTask<Void>() {

			@Override
			protected Void call() throws Exception {
				Thread.sleep(1000);
				return null;
			}

			@Override
			protected void onSuccess(Void value) {
				hideLoadingMask();
			}
		});
	}

	@FXML
	protected void handleSave() {

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
	protected void mapFields(Invoice editingActivity) {

	}

	@Override
	protected Invoice getEditedEntity() {
		return null;
	}

	@Override
	protected void doCustomBackgroundInitialization(Invoice editingEntity) {
		// TODO Auto-generated method stub
		
	}

}
