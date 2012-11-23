package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Customer;
import be.virtualsushi.jfx.dorse.model.Sector;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerFilterDialog extends AbstractFilterDialog {

	@FXML
	private VBox container;

	@FXML
	private TextField nameField;
  @FXML
  private EditableList<Sector> parentSectorField;

  @Override
  protected Customer getFilterEntity() {
    Customer result = new Customer();
    result.setName(nameField.getValue());
    if(parentSectorField.getValue()!=null) result.setSector(parentSectorField.getValue().getId());
    result.setSortType((String)sortTypeSelectorField.getValue());
    result.setColumnName((String)columnSelectorField.getValue());
    return result;
  }

  @Override
  protected List<String> getColumnNames() {
    List<String> names = new ArrayList<String>();
    names.add("");
    names.add("Id");
    names.add("Name");
    return names;
  }

  @SuppressWarnings("unchecked")
 	@Override
 	public void onShow(Object... parameters) {
 		super.onShow(parameters);

 		if (ArrayUtils.isNotEmpty(parameters)) {
 			mapParentField((List<Sector>) parameters[0]);
 		}
 	}

  private void mapParentField(List<Sector> list) {
 		parentSectorField.setAcceptableValues(list);
 	}
}
