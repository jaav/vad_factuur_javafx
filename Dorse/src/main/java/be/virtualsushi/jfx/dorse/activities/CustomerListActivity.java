package be.virtualsushi.jfx.dorse.activities;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class CustomerListActivity extends AbstractListActivity<Customer> {

	private class CustomerTableColumn<T> extends TableColumn<Customer, T> {

		public CustomerTableColumn(String titleKey, Callback<CellDataFeatures<Customer, T>, ObservableValue<T>> cellFactory) {
			super(getResources().getString(titleKey));
			setCellValueFactory(cellFactory);
		}
	}

	private List<Sector> sectors;

	@Override
	protected String getTitle() {
		return getResources().getString("customer.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Customer> table) {
		CustomerTableColumn<Long> idColumn = new CustomerTableColumn<Long>("id", new PropertyValueFactory<Customer, Long>("id"));

		CustomerTableColumn<String> nameColumn = new CustomerTableColumn<String>("name", new PropertyValueFactory<Customer, String>("name"));
		nameColumn.setMinWidth(150);

		CustomerTableColumn<String> sectorColumn = new CustomerTableColumn<String>("sector", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(SimpleStringProperty property, Customer value) {
				if (sectors != null) {
					for (Sector sector : sectors) {
						if (sector.getId() == value.getSubsector()) {
							property.set(sector.getName());
							break;
						}
					}
				}
			}
		});
		sectorColumn.setMinWidth(150);

		CustomerTableColumn<String> zipColumn = new CustomerTableColumn<String>("zipcode", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(SimpleStringProperty property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getZipcode());
				}
			}
		});
		zipColumn.setMinWidth(70);

		CustomerTableColumn<String> locationColumn = new CustomerTableColumn<String>("location", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(SimpleStringProperty property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getCity());
				}
			}
		});
		locationColumn.setMinWidth(100);

		CustomerTableColumn<String> phoneColumn = new CustomerTableColumn<String>("phone", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(SimpleStringProperty property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getPhone());
				}
			}
		});
		phoneColumn.setMinWidth(100);

		CustomerTableColumn<String> emailColumn = new CustomerTableColumn<String>("email", new EntityStringPropertyValueFactory<Customer>() {

			@Override
			protected void setPropertyValue(SimpleStringProperty property, Customer value) {
				if (CollectionUtils.isNotEmpty(value.getAddress())) {
					property.set(value.getAddress().get(0).getEmail());
				}
			}
		});
		emailColumn.setMinWidth(70);

		table.getColumns().addAll(idColumn, nameColumn, sectorColumn, zipColumn, locationColumn, phoneColumn, emailColumn);
	}

	@Override
	protected void doCustomBackgroundInitialization() {
		sectors = getRestApiAccessor().getList(Sector.class, false);
	}
}
