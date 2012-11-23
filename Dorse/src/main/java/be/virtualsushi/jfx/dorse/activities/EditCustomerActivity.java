package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_CUSTOMERS;

import java.util.*;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.model.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
	private EditableList<Sector> sectorField;

  @FXML
 	protected EditableList<Sector> subSectorField;

	@FXML
	private ValidationErrorPanel validationPanel;

	private List<Sector> acceptableSectors;
  private List<Sector> acceptableSubSectors;

	@Override
	public void initialize() {
		super.initialize();
		sectorField.setEditHandler(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        showDialog(getResources().getString("new.sector.dialog.title"), NewSectorDialog.class, acceptableSectors);
      }
    });
    sectorField.setChangeHandler(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        System.out.println("There was a change => " + actionEvent);
        ComboBox sectorCombo = (ComboBox)actionEvent.getSource();
        Sector selectedSector = (Sector)sectorCombo.getValue();
        Long id = selectedSector.getId();
        if(id!=null && id>=0)
          showSubSectors(id);
        else
          hideSubsectors();
        subSectorField.getSelectionModel().clearSelection();
      }
    });
    if(subSectorField.getAcceptableValues().size()==0)
      subSectorField.getAcceptableValues().add(new Sector(getResources().getString("select_sector_first")));
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
    List l = subSectorField.getAcceptableValues();
    if(editingEntity.getSubsector()!=null) subSectorField.setValue(getSubSector(editingEntity.getSubsector()));
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
    if (sector != null) editedCustomer.setSector(sector.getId());
    Sector subSector = (Sector)subSectorField.getValue();
    if(subSector != null) editedCustomer.setSubsector(subSector.getId());
		return editedCustomer;
	}

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Customer editingEntity) {
    if(editingEntity.getId()!=null){
      idField.setVisible(true);
    }
    else{
      idField.setVisible(false);
    }
		acceptableSectors = (List<Sector>)getRestApiAccessor().getResponse(Sector.class, null, null, null, null, "parent is null", false, false, true).getData();
    acceptableSectors.add(0, Sector.getEmptySector());
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

  @SuppressWarnings("unchecked")
  private void showSubSectors(Long sector_id){
    acceptableSubSectors = (List<Sector>)getRestApiAccessor().getResponse(Sector.class, null, null, null, "name", "parent="+sector_id, true, false, true).getData();
    acceptableSubSectors.add(0, Sector.getEmptySector());
    subSectorField.setAcceptableValues(acceptableSubSectors);
  }

  private void hideSubsectors(){
    subSectorField.getAcceptableValues().clear();
  }

  private Sector getSubSector(Long id){
    for (Sector acceptableSubSector : acceptableSubSectors) {
      if(acceptableSubSector.getId().equals(id)) return acceptableSubSector;
    }
    return null;
  }

  @Override
  protected void clearForm() {
    nameField.setValue("");
    remarkField.setValue("");
    ibanField.setValue("");
    vatField.setValue("");
    sectorField.setValue(Sector.getEmptySector());
  }

}
