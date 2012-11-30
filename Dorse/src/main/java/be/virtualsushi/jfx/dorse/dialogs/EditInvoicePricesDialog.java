package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.events.dialogs.ChangeInvoicePricesEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveOrderLineEvent;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;
import be.virtualsushi.jfx.dorse.model.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EditInvoicePricesDialog extends AbstractDialog implements HasValidationDialog {

	private static final String ARTICLE_INFO_VALUE_PATTERN = "%s - %s - %s";

  private Invoice editingInvoice;

  @FXML
 	protected VBox container;

	@FXML
	private FloatNumberField shippingField, productsField;

	private ValidationErrorPanel validationPanel;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	protected void handleSave(ActionEvent event) {
		postSaveEvent(new ChangeInvoicePricesEvent(getEditedValue()));
	}

	private Invoice getEditedValue() {
    editingInvoice.setShipping(shippingField.getValue());
    editingInvoice.setProducts(productsField.getValue());
    editingInvoice.setTotal(shippingField.getValue()+productsField.getValue());
		return editingInvoice;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);
    Invoice i = (Invoice)parameters[0];
    editingInvoice = i;
    shippingField.setValue(i.getShipping());
    productsField.setValue(i.getProducts());
	}

	@Override
	protected void onUiBinded() {
		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();

	}

	@Override
	public ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	public Map<String, HasValidation> getFieldsMap() {
		return fieldsMap;
	}

	@Override
	public void showValidationPanel() {
		container.getChildren().add(0, validationPanel);
	}

	@Override
	public void hideValidationPanel() {
		container.getChildren().remove(validationPanel);
	}

}
