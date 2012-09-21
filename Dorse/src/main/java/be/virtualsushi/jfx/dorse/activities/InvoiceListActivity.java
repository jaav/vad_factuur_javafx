package be.virtualsushi.jfx.dorse.activities;

import java.text.SimpleDateFormat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class InvoiceListActivity extends AbstractListActivity<Invoice> {

	@Override
	protected String getTitle() {
		return getResources().getString("invoice.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Invoice> table) {
		TableColumn<Invoice, Long> idColumn = createTableColumn("id");

		TableColumn<Invoice, String> customerColumn = createTableColumn("customer", new EntityStringPropertyValueFactory<Invoice>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Invoice value) {
				property.set(value.getCustomer().getName());
			}
		});
		customerColumn.setMinWidth(150);

		TableColumn<Invoice, String> dateColumn = createTableColumn("date", new EntityStringPropertyValueFactory<Invoice>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Invoice value) {
				if (value.getCreationDate() != null) {
					property.set(new SimpleDateFormat(getResources().getString("date.format")).format(value.getCreationDate()));
				}
			}
		});
		dateColumn.setMinWidth(100);

		TableColumn<Invoice, Float> shippingColumn = createTableColumn("shipping");
		shippingColumn.setMinWidth(70);

		TableColumn<Invoice, String> statusColumn = createTableColumn("status");
		statusColumn.setMinWidth(100);

		TableColumn<Invoice, Float> totalColumn = createTableColumn("total");
		totalColumn.setMinWidth(70);

		table.getColumns().addAll(idColumn, customerColumn, dateColumn, shippingColumn, statusColumn, totalColumn, createActionsColumn());
	}

	@Override
	protected void doCustomBackgroundInitialization() {

	}

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return AppActivitiesNames.VIEW_INVOICE;
	}

	@Override
	protected AppActivitiesNames getEditActivityName() {
		return AppActivitiesNames.EDIT_INVOICE;
	}

}
