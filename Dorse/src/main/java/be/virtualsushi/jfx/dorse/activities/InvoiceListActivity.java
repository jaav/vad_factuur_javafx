package be.virtualsushi.jfx.dorse.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import be.virtualsushi.jfx.dorse.dialogs.InvoiceFilterDialog;
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

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class InvoiceListActivity extends AbstractListActivity<Invoice> {
  private List<Customer> customers;

	@Override
	protected String getTitle() {
		return getResources().getString("invoice.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Invoice> table) {
		TableColumn<Invoice, Long> idColumn = createTableColumn("id");

    TableColumn<Invoice, String> codeColumn = createTableColumn("code");
    codeColumn.setMinWidth(100);

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

    TableColumn<Invoice, Float> productsColumn = createTableColumn("products");
    productsColumn.setMinWidth(70);

    TableColumn<Invoice, Float> totalColumn = createTableColumn("total");
    totalColumn.setMinWidth(70);

		TableColumn<Invoice, String> statusColumn = createTableColumn("status", new EntityStringPropertyValueFactory<Invoice>() {

					@Override
					protected void setPropertyValue(ObjectProperty<String> property, Invoice value) {
            if(value.getStatus()!=null)
						  property.set(getStatus(value.getStatus()).getName());
            else
              property.set(getResources().getString("N/A"));
					}
				});
		statusColumn.setMinWidth(100);

		table.getColumns().addAll(idColumn, codeColumn, customerColumn, dateColumn, productsColumn, shippingColumn, totalColumn, statusColumn, createActionsColumn());
	}

  @Override
  protected void fillTableColumns(TableView<Invoice> table, String columnName, boolean asc) {
    fillTableColumns(table);
  }

  @Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
    if (CollectionUtils.isEmpty(customers)) {
      customers = (List<Customer>)getRestApiAccessor().getResponse(Customer.class, null, null, null, "name", "", false, false, true).getData();
      customers.add(0, Customer.getEmptyCustomer());
    }
	}

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return AppActivitiesNames.VIEW_INVOICE;
	}

	@Override
	protected AppActivitiesNames getEditActivityName() {
		return AppActivitiesNames.EDIT_INVOICE;
	}

  @Override
  protected Boolean getFullInfo() {
    return true;
  } 
  
  @Override  
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse) {
    table = new TableView<Invoice>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table);
    table.setItems(FXCollections.observableArrayList((List<Invoice>) serverResponse.getData()));
    return table;
  }

  @Override
  protected TableView createPage(ServerResponse serverResponse, String columnName, boolean asc) {
    return createPage(serverResponse);
  }

  @Override
  protected String createCsv(ServerResponse serverResponse, File target) {
    exportService.createCsv((InvoiceResponse) serverResponse, target);
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  @FXML
 	protected void handleLaunchFilter(ActionEvent event) {
    showFilterDialog(getResources().getString("customer.filter.dialog.title"), InvoiceFilterDialog.class, customers, getStatuses());
 	}
}
