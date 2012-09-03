package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.dialogs.NewSectorDialog;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;

@Component
@Scope("prototype")
public class EditCustomerActivity extends AbstractEditActivity<VBox, Customer> {

	public static final String CUSTOMER_ID_PARAMETER = "customer_id";

	@FXML
	protected TextField nameField, vatField, ibanField;

	@FXML
	protected TextAreaField remarkField;

	@FXML
	protected Label idField, title;

	@FXML
	protected EditableList<Sector> sectorField;

	@Override
	public void initialize() {
		super.initialize();
		sectorField.setEditHandler(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showDialog(getResources().getString("new.sector.dialog.title"), NewSectorDialog.class);
			}
		});
		sectorField.setCellFactory(new Callback<ListView<Sector>, ListCell<Sector>>() {

			@Override
			public ListCell<Sector> call(ListView<Sector> param) {
				return new ListCell<Sector>() {

					@Override
					protected void updateItem(Sector item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
						}
					}

				};
			}
		});
	}

	@Override
	protected String getEditTitleKey() {
		return "edit.customer";
	}

	@Override
	protected String getNewTitleKey() {
		return "new.customer";
	}

	@Override
	protected Customer newEntityInstance() {
		return new Customer();
	}

	@Override
	protected void mapFields() {
		idField.setText(String.valueOf(getEditingEntity().getId()));
		nameField.setValue(getEditingEntity().getName());
		ibanField.setValue(getEditingEntity().getIban());
		vatField.setValue(getEditingEntity().getVat());
		remarkField.setValue(getEditingEntity().getRemark());
	}

	@Override
	protected Customer getEditedEntity() {
		Customer editedCustomer = getEditingEntity();
		editedCustomer.setName(nameField.getValue());
		editedCustomer.setIban(ibanField.getValue());
		editedCustomer.setVat(vatField.getValue());
		editedCustomer.setRemark(remarkField.getValue());
		editedCustomer.setSector(sectorField.getValue().getId());
		return editedCustomer;
	}

}
