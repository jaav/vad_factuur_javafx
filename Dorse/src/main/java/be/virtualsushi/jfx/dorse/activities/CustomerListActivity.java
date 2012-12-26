package be.virtualsushi.jfx.dorse.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.virtualsushi.jfx.dorse.dialogs.CustomerFilterDialog;
import be.virtualsushi.jfx.dorse.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.SortButton;
import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class CustomerListActivity extends AbstractListActivity<Customer> {

	private List<Sector> sectors;
  private List<Sector> parentSectors;

	@Override
	protected String getTitle() {
		return getResources().getString("customer.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Customer> table) {
		TableColumn<Customer, Long> idColumn = createTableColumn("id");

		TableColumn<Customer, String> nameColumn = createTableColumn("name");
    nameColumn.setMinWidth(200);

		TableColumn<Customer, String> sectorColumn = createTableColumn("sector", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Customer value) {
				if (sectors != null) {
					for (Sector sector : sectors) {
						if (sector.getId().equals(value.getSector())) {
							property.set(sector.getName());
							break;
						}
					}
				}
			}
		});
		sectorColumn.setMinWidth(150);

		TableColumn<Customer, String> zipColumn = createTableColumn("zipcode", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getZipcode());
				}
			}
		});
		zipColumn.setMinWidth(70);

		TableColumn<Customer, String> locationColumn = createTableColumn("location", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getCity());
				}
			}
		});
		locationColumn.setMinWidth(100);

		TableColumn<Customer, String> phoneColumn = createTableColumn("phone", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getPhone());
				}
			}
		});
		phoneColumn.setMinWidth(100);

		TableColumn<Customer, String> emailColumn = createTableColumn("email", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getEmail());
				}
			}
		});
		emailColumn.setMinWidth(70);

		table.getColumns().addAll(idColumn, nameColumn, sectorColumn, zipColumn, locationColumn, phoneColumn, emailColumn, createActionsColumn());
	}

  @Override
  protected void fillTableColumns(TableView<Customer> table, String columnName, boolean asc) {
    fillTableColumns(table);
  }

  @Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(sectors)) {
			sectors = (List<Sector>)getRestApiAccessor().getResponse(Sector.class, false).getData();
      sectors.add(0, Sector.getEmptySector());
      createParentSectorsList(sectors);
		}
	}

  private void createParentSectorsList(List<Sector> sectors){
    parentSectors = new ArrayList<Sector>();
    for (Sector sector : sectors) {
      if(sector.getParent()==null) parentSectors.add(sector);
    }
  }

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return AppActivitiesNames.VIEW_CUSTOMER;
	}

	@Override
	protected AppActivitiesNames getEditActivityName() {
		return AppActivitiesNames.EDIT_CUSTOMER;
	}

  @Override
  protected Boolean getFullInfo() {
    return true;
  } 
  
  @Override  
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse) {
    table = new TableView<Customer>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table);
    table.setItems(FXCollections.observableArrayList((List<Customer>) serverResponse.getData()));
    return table;
  }

  @Override
  protected TableView createPage(ServerResponse serverResponse, String columnName, boolean asc) {
    return createPage(serverResponse);
  }

  @Override
  protected String createCsv(ServerResponse serverResponse, File target) {
    exportService.createCsv((CustomerResponse)serverResponse, target, sectors);
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  @FXML
 	protected void handleLaunchFilter(ActionEvent event) {
    showFilterDialog(getResources().getString("customer.filter.dialog.title"), CustomerFilterDialog.class, parentSectors);
 	}
}
