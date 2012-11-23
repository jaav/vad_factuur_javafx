package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.calendar.DatePicker;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.Sector;
import be.virtualsushi.jfx.dorse.model.Status;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class InvoiceFilterDialog extends AbstractFilterDialog {

	@FXML
	private VBox container;

  @FXML
  private EditableList<Customer> customerField;

  @FXML
  private EditableList<Status> statusField;

  @FXML
 	private DatePicker creationDateField;

  @Override
  protected Invoice getFilterEntity() {
    Invoice result = new Invoice();
    result.setCustomer(customerField.getValue());
    result.setCreationDate(creationDateField.getSelectedDate());
    if(statusField.getValue()!=null) result.setStatus(statusField.getValue().getId());
    result.setSortType((String)sortTypeSelectorField.getValue());
    result.setColumnName((String)columnSelectorField.getValue());
    return result;
  }

  @Override
  protected List<String> getColumnNames() {
    List<String> names = new ArrayList<String>();
    names.add("");
    names.add("Id");
    names.add("Customername");
    names.add("Code");
    names.add("Total");
    names.add("Creation date");
    return names;
  }

  @SuppressWarnings("unchecked")
 	@Override
 	public void onShow(Object... parameters) {
 		super.onShow(parameters);
    List<String> styles = this.asNode().getScene().getStylesheets();
    creationDateField.setDateFormat(new SimpleDateFormat(getResources().getString("input.date.format")));
    creationDateField.getCalendarView().setShowTodayButton(true);
    creationDateField.setLocale(Locale.GERMAN);

 		if (ArrayUtils.isNotEmpty(parameters)) {
       customerField.setAcceptableValues((List<Customer>)parameters[0]);
 		}
    if (ArrayUtils.isNotEmpty(parameters)) {
      statusField.setAcceptableValues((List<Status>)parameters[1]);
    }
 	}
}
