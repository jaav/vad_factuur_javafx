package be.virtualsushi.jfx.dorse.activities;

import java.text.SimpleDateFormat;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ViewAddressControl;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.OrderLine;

@Component
@Scope("prototype")
public class ViewInvoiceActivity extends AbstractManageEntityActivity<VBox, Invoice> {

	@FXML
	private Label idField, customerField, createdField, title;

	@FXML
	private ViewAddressControl invoiceAddressField, deliveryAddressField;

	@FXML
	private TableView<OrderLine> orderLineTable;

	@FXML
	private TableColumn<OrderLine, Long> idColumn;

	@FXML
	private TableColumn<OrderLine, String> codeColumn, articleNameColumn;

	@FXML
	private TableColumn<OrderLine, Float> discountColumn, lineTotalColumn;

	@FXML
	private TableColumn<OrderLine, Integer> quantityColumn;

	private Address invoiceAddressValue, deliveryAddressValue;

	private Customer customerValue;

	private List<OrderLine> orderLines;

	@Override
	public void initialize() {
		super.initialize();

		invoiceAddressField.setResources(getResources());
		deliveryAddressField.setResources(getResources());

		idColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Long>("id"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Float>("discount"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("quantity"));

	}

	@Override
	protected void mapFields(Invoice viewingEntity) {
		title.setText(String.format(getResources().getString("view.invoice"), viewingEntity.getCode()));
		invoiceAddressField.setValue(invoiceAddressValue);
		deliveryAddressField.setValue(deliveryAddressValue);
		idField.setText(String.valueOf(viewingEntity.getId()));
		customerField.setText(customerValue.getName());
		createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(viewingEntity.getCreationDate()));
		updateTableData();
	}

	private void updateTableData() {
		if (orderLineTable.getItems() != null) {
			orderLineTable.getItems().clear();
			orderLineTable.getItems().addAll(orderLines);
		} else {
			orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
		}
	}

	@Override
	protected void doCustomBackgroundInitialization(Invoice entity) {
		invoiceAddressValue = getRestApiAccessor().get(entity.getInvoiceAddress(), Address.class);
		deliveryAddressValue = getRestApiAccessor().get(entity.getDeliveryAddress(), Address.class);
		customerValue = getRestApiAccessor().get(entity.getCustomer(), Customer.class);
		orderLines = getRestApiAccessor().getList(OrderLine.class, OrderLine[].class, true, entity.getId());
	}

}
