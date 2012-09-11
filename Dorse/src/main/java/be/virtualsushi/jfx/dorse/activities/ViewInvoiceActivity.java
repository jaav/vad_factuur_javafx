package be.virtualsushi.jfx.dorse.activities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ViewAddressControl;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.OrderLine;

@Component
@Scope("prototype")
public class ViewInvoiceActivity extends AbstractManageEntityActivity<VBox, Invoice> {

	@FXML
	private Label idField, customerField, createdField;

	@FXML
	private ViewAddressControl invoiceAddress, deliveryAddress;

	@FXML
	private TableView<OrderLine> orderLineTable;

	@FXML
	private TableColumn<OrderLine, String> idColumn, codeColumn, articleNameColumn, dicsountColumn, quantityColumn, lineTotalColumn;

	@Override
	public void initialize() {
		super.initialize();
		// invoiceAddress.setResources(getResources());
		// deliveryAddress.setResources(getResources());
	}

	@Override
	protected void mapFields(Invoice viewingEntity) {
		idField.setText(String.valueOf(viewingEntity.getId()));
	}

	@Override
	protected void doCustomBackgroundInitialization(Invoice entity) {

	}

}
