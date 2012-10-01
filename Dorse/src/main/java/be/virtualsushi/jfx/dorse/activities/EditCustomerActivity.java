package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_CUSTOMERS;

import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.dialogs.NewSectorDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSectorEvent;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditCustomerActivity extends AbstractEditActivity<VBox, Customer> {

	private class SaveSectorTaskCreator implements TaskCreator<DorseBackgroundTask<Sector>> {

		private final Sector sector;

		public SaveSectorTaskCreator(Sector sectorToSave) {
			this.sector = sectorToSave;
		}

		@Override
		public DorseBackgroundTask<Sector> createTask() {
			return new DorseBackgroundTask<Sector>(this, sector) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected Sector call() throws Exception {
					getRestApiAccessor().save((Sector) getParameters()[0]);
					doCustomBackgroundInitialization(getEntity());
					return (Sector) getParameters()[0];
				}

				@Override
				protected void onSuccess(Sector value) {
					mapSector(getEntity());
					hideLoadingMask();
				}
			};
		}

	}

	@FXML
	protected TextField nameField, vatField, ibanField;

	@FXML
	protected TextAreaField remarkField;

	@FXML
	protected Label idField, title;

	@FXML
	protected EditableList<Sector> sectorField;

	@FXML
	private ValidationErrorPanel validationPanel;

	private List<Sector> acceptableSectors;

	@Override
	public void initialize() {
		super.initialize();
		sectorField.setEditHandler(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showDialog(getResources().getString("new.sector.dialog.title"), NewSectorDialog.class, acceptableSectors);
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
		mapSector(editingEntity);
	}

	private void mapSector(Customer editingEntity) {
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
		Sector sector = sectorField.getValue();
		if (sector != null) {
			editedCustomer.setSubsector(sector.getId());
			editedCustomer.setSector(sector.getParent());
		}
		return editedCustomer;
	}

	@Override
	protected void doCustomBackgroundInitialization(Customer editingEntity) {
		acceptableSectors = getRestApiAccessor().getList(Sector.class, false);
	}

	@Subscribe
	public void onSaveSector(SaveSectorEvent event) {
		doInBackground(new SaveSectorTaskCreator(event.getEntity()));
	}

	@Override
	protected AppActivitiesNames getListActivityName() {
		return LIST_CUSTOMERS;
	}

	@Override
	protected ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	protected void fillFieldsMap(HashMap<String, HasValidation> fieldsMap) {
		fieldsMap.put("name", nameField);
		fieldsMap.put("vat", vatField);
		fieldsMap.put("iban", ibanField);
		fieldsMap.put("reamrk", remarkField);
		fieldsMap.put("sector", sectorField);
	}

}
