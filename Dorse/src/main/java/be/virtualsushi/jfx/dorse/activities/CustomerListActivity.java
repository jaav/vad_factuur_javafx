package be.virtualsushi.jfx.dorse.activities;

import java.util.List;

import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ServerResponse;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.SortButton;
import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class CustomerListActivity extends AbstractListActivity<Customer> {

	private List<Sector> sectors;

	@Override
	protected String getTitle() {
		return getResources().getString("customer.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Customer> table) {
		TableColumn<Customer, Long> idColumn = createNoHeaderTableColumn("id");
		idColumn.setSortable(false);
		idColumn.setGraphic(new SortButton("id", getName("id"), activityNavigator));

		TableColumn<Customer, String> nameColumn = createNoHeaderTableColumn("name");
		nameColumn.setMinWidth(150);
		nameColumn.setGraphic(new SortButton("name", getName("name"), activityNavigator));

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
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(sectors)) {
			sectors = (List<Sector>)getRestApiAccessor().getResponse(Sector.class, false).getData();
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
    TableView<Customer> table = new TableView<Customer>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table);
    table.setItems(FXCollections.observableArrayList((List<Customer>) serverResponse.getData()));
    return table;
  }
}
