package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.calendar.DatePicker;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.Sector;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Component
public class InvoiceFilterDialog extends AbstractFilterDialog {

	@FXML
	private VBox container;

  @FXML
  private EditableList<Customer> customerField;

  @FXML
 	private DatePicker creationDateField;

  @Override
  protected Invoice getFilterEntity() {
    Invoice result = new Invoice();
    result.setCustomer(customerField.getValue());
    result.setCreationDate(creationDateField.getSelectedDate());
    return result;
  }

  @SuppressWarnings("unchecked")
 	@Override
 	public void onShow(Object... parameters) {
 		super.onShow(parameters);
    customerField.getScene().getStylesheets().add("calendarstyle.css");
    creationDateField.setDateFormat(new SimpleDateFormat(getResources().getString("input.date.format")));
    creationDateField.getCalendarView().setShowTodayButton(true);
    creationDateField.setLocale(Locale.GERMAN);

 		if (ArrayUtils.isNotEmpty(parameters)) {
       customerField.setAcceptableValues((List<Customer>)parameters[0]);
 		}
 	}
}
