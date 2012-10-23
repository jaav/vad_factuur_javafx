package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SavePersonEvent;
import be.virtualsushi.jfx.dorse.model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NewPersonDialog extends AbstractDialog implements HasValidationDialog {

	@FXML
	private VBox container;

	@FXML
	private TextField titleField, nameField, phoneField, emailField;

	private ValidationErrorPanel validationPanel;

	private Integer personType;

	private Long customer;
  private Person oldPerson;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	protected void handleSave(ActionEvent event) {
		Person person = new Person();
    person.setId(idField.getValue());
		person.setTitle(titleField.getValue());
		person.setName(nameField.getValue());
		person.setPhone(phoneField.getValue());
		person.setEmail(emailField.getValue());
		person.setCustomer(customer);
		postSaveEvent(new SavePersonEvent(person));
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();

		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();
		fieldsMap.put("title", titleField);
		fieldsMap.put("name", nameField);
		fieldsMap.put("phone", phoneField);
		fieldsMap.put("email", emailField);
	}

	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);

		if (ArrayUtils.isNotEmpty(parameters)) {
			customer = (Long) parameters[0];
      if(parameters.length>1){
        oldPerson = (Person) parameters[1];
        if(oldPerson!=null){
          titleField.setValue(oldPerson.getTitle());
          nameField.setValue(oldPerson.getName());
          emailField.setValue(oldPerson.getEmail());
          phoneField.setValue(oldPerson.getPhone());
          idField.setValue(oldPerson.getId());
        }
      }
			/*if (parameters.length > 1) {
				personType = (Integer) parameters[1];
			} else {
				personType = 0;
			}*/
		}
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
