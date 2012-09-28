package be.virtualsushi.jfx.dorse.dialogs;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.model.IdNamePairEntity;

public abstract class IdNamePairEditDialog<E extends IdNamePairEntity> extends AbstractDialog implements HasValidationDialog {

	@FXML
	public VBox container;

	@FXML
	public TextField nameField;

	public ValidationErrorPanel validationPanel;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	public void handleSave(ActionEvent event) {
		E entity = createNewEntityInstance();
		entity.setName(nameField.getValue());
		postSaveEvent(createSaveEvent(entity));
	}

	@Override
	protected void onUiBinded() {
		super.onUiBinded();

		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();
		fieldsMap.put("name", nameField);
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

	protected abstract E createNewEntityInstance();

	protected abstract SaveEntityEvent<E> createSaveEvent(E entity);

}
