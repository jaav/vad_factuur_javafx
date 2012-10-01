package be.virtualsushi.jfx.dorse.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ComboBoxField;
import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSectorEvent;
import be.virtualsushi.jfx.dorse.model.Sector;

@Component
public class NewSectorDialog extends AbstractDialog implements HasValidationDialog {

	@FXML
	protected VBox container;

	@FXML
	protected TextField nameField;

	@FXML
	protected ComboBoxField<Sector> parentSectorField;

	private ValidationErrorPanel validationPanel;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	protected void handleSave(ActionEvent event) {
		Sector sector = new Sector();
		sector.setName(nameField.getValue());
		if (parentSectorField.getValue() != null) {
			sector.setParent(parentSectorField.getValue().getId());
		}
		postSaveEvent(new SaveSectorEvent(sector));
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();

		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();
		fieldsMap.put("name", nameField);
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
