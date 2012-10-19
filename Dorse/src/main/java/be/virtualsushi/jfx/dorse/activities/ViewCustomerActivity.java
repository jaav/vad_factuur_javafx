package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.dialogs.NewPersonDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SavePersonEvent;
import be.virtualsushi.jfx.dorse.model.Person;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import be.virtualsushi.jfx.dorse.utils.Utils;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ViewAddressControl;
import be.virtualsushi.jfx.dorse.dialogs.NewAddressDialog;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Customer;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ViewCustomerActivity extends AbstractViewEntityActivity<VBox, Customer> {
  

  private class SaveAddressTaskCreator implements TaskCreator<DorseBackgroundTask<List<Address>>> {
 
 		private final Address address;
 
 		public SaveAddressTaskCreator(Address address) {
 			this.address = address;
 		}
 
 		@Override
 		public DorseBackgroundTask<List<Address>> createTask() {
 			return new DorseBackgroundTask<List<Address>>(this, address) {
 
 				@Override
 				protected void onPreExecute() {
 					showLoadingMask();
 				}
 
 				@Override
 				protected List<Address> call() throws Exception {
 					getRestApiAccessor().save((Address) getParameters()[0]);
 					return getRestApiAccessor().getList(Address.class, null, null, "id", "order="+getEntity().getId(), true, false);
 				}
 
 				@Override
 				protected void onSuccess(List<Address> value) {
 					Address = value;
 					addressTable.setItems(FXCollections.observableArrayList(new ArrayList<Address>()));
 					addressTable.setItems(FXCollections.observableArrayList(address));
 					hideLoadingMask();
 				}
 
 			};
 		}
 
 	}

	@FXML
	protected Label idField, vatField, ibanField, remarkField;

	@FXML
	protected TabPane addressesList;

  @FXML
 	protected ListView personsList;

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
			Tab tab = new Tab(StringUtils.isNotBlank(address.getAddress())?Utils.shorten(address.getAddress(),16):getResources().getString("address"));
      //tab.getStyleClass().add("white-tab");
			ViewAddressControl addressView = new ViewAddressControl();
      addressView.setTitle(viewingEntity.getName());
      //addressView.getStyleClass().add("red_debug");
			addressView.setPadding(new Insets(20));
			addressView.setResources(getResources());
			addressView.setValue(address);
      //ScrollPane addressContainer = new ScrollPane();
      //addressContainer.getStyleClass().add("orange_debug");
      //addressContainer.getStyleClass().add("no_border");
      //addressContainer.setContent(addressView);
			tab.setContent(addressView);
			addressesList.getTabs().add(tab);
			addressIndex++;
		}                                               
    int personIndex = 1;
    for (Person person : viewingEntity.getPerson()) {
      personsList.getItems().add(new Label(person.getTitle()+" "+person.getName()));
      personIndex++;
    }
	}

  private String getStyleClasses(List<String> classes){
    if(classes!=null && !classes.isEmpty()){
      StringBuffer sb = new StringBuffer();
      for (String aClass : classes) {
        sb.append(aClass).append(" - ");
      }
      return sb.toString();
    }
    return "";
  }

	@FXML
	protected void handleAddAddress(ActionEvent event) {
		showDialog(String.format(getResources().getString("new.customer.address.dialog.title"), getEntity().getName()), NewAddressDialog.class, getEntity().getId());
	}

  @FXML
 	protected void handleAddPerson(ActionEvent event) {
 		showDialog(String.format(getResources().getString("new.customer.person.dialog.title"), getEntity().getName()), NewPersonDialog.class, getEntity().getId());
 	}

	@Override
	protected void doCustomBackgroundInitialization(Customer entity) {

	}

  @Override
  protected void started() {
    super.started();
    clearTabs();
  }

  @Subscribe
 	public void onSaveAddress(SaveAddressEvent event) {
 		doInBackground(new SaveAddressTaskCreator(event.getEntity()));
 	}   

  @Subscribe
 	public void onSavePerson(SavePersonEvent event) {
 		doInBackground(new SavePersonTaskCreator(event.getEntity()));
 	}

  private void clearTabs(){
    //addressesList.setStyle("-fx-background-color:red;");
    //addressesList.setSkin(null);
    addressesList.getTabs().clear();
  }
}
