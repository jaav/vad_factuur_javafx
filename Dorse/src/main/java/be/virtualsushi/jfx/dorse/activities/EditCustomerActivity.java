package be.virtualsushi.jfx.dorse.activities;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

	private List<Sector> acceptableSectors;

	@Override
	public void initialize() {
		super.initialize();
		sectorField.setEditHandler(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showDialog(getResources().getString("new.sector.dialog.title"), NewSectorDialog.class);
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
	protected void mapFields(Customer editingEntity) {
		idField.setText(String.valueOf(editingEntity.getId()));
		nameField.setValue(editingEntity.getName());
		ibanField.setValue(editingEntity.getIban());
		vatField.setValue(editingEntity.getVat());
		remarkField.setValue(editingEntity.getRemark());
		sectorField.setAcceptableValues(acceptableSectors);
		for (Sector sector : sectorField.getAcceptableValues()) {
			if (editingEntity.getSector() == sector.getId()) {
				sectorField.setValue(sector);
				break;
			}
		}
	}

	@Override
	protected Customer getEditedEntity() {
		Customer editedCustomer = getEntity();
		editedCustomer.setName(nameField.getValue());
		editedCustomer.setIban(ibanField.getValue());
		editedCustomer.setVat(vatField.getValue());
		editedCustomer.setRemark(remarkField.getValue());
		editedCustomer.setSector(sectorField.getValue().getId());
		return editedCustomer;
	}

	@Override
	protected void doCustomBackgroundInitialization(Customer editingEntity) {
		acceptableSectors = getRestApiAccessor().getList(Sector.class, Sector[].class, false);
	}

}
