package be.virtualsushi.jfx.dorse.activities;

import java.util.HashMap;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractEditActivity<N extends Node, E extends BaseEntity> extends AbstractManageEntityActivity<N, E> {

	public static final String VALIDATION_MESSAGE_PATTERN = "%s: %s";

	private Validator validator;

	private HashMap<String, HasValidation> fieldsMap;

	private class SaveTaskCreator implements TaskCreator<DorseBackgroundTask<E>> {

		private final E entity;

		public SaveTaskCreator(E entityToSave) {
			this.entity = entityToSave;
		}

		@Override
		public DorseBackgroundTask<E> createTask() {
			return new DorseBackgroundTask<E>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected E call() throws Exception {
					getRestApiAccessor().save(entity);
					return entity;
				}

				@Override
				protected void onSuccess(E value) {
					hideLoadingMask();
					goTo(getListActivityName(), AbstractListActivity.FORCE_RELOAD_PARAMETER, true);
				}

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          ValidationErrorPanel validationPanel = getValidationPanel();
       			if (validationPanel != null) {
       				validationPanel.clearMessages();
              validationPanel.addMessage(getResources().getString("save_error")+"\n\n"+exception.getMessage());
       				validationPanel.setVisible(true);
             }
          hideLoadingMask();
        }
      };
		}

	}

	@FXML
	public void handleCancel(ActionEvent event) {
		getActivityNavigator().goBack();
	}

	@FXML
	public void handleSave(ActionEvent event) {
		E entity = getEditedEntity();
		clearInvalid();
		Set<ConstraintViolation<E>> violations = getValidator().validate(entity);
		if (violations.isEmpty()) {
			doInBackground(new SaveTaskCreator(entity));
		} else {
			ValidationErrorPanel validationPanel = getValidationPanel();
			if (validationPanel != null) {
				validationPanel.clearMessages();
				for (ConstraintViolation<E> violation : violations) {
					String propertyName = violation.getPropertyPath().toString();
					if (fieldsMap.containsKey(propertyName)) {
						fieldsMap.get(propertyName).setInvalid();
					}
					validationPanel.addMessage(String.format(VALIDATION_MESSAGE_PATTERN, getResources().getString(propertyName), violation.getMessage()));
				}
				validationPanel.setVisible(true);
			}
		}
	}

	private void clearInvalid() {
		if (getValidationPanel() != null) {
			getValidationPanel().setVisible(false);
		}
		for (String key : fieldsMap.keySet()) {
			fieldsMap.get(key).clearInvalid();
		}
	}

	@Override
	protected void started() {
		super.started();

		clearInvalid();
		if (getParameter(ENTITY_ID_PARAMETER, Long.class, null) != null) {
			title.setText(getResources().getString(getEditTitleKey()));
		} else {
			title.setText(getResources().getString(getNewTitleKey()));
		}
	}

	@Override
	public void initialize() {
		super.initialize();
		fieldsMap = new HashMap<String, HasValidation>();
		fillFieldsMap(fieldsMap);
		if (getValidationPanel() != null) {
			getValidationPanel().setTitleText(getResources().getString("validation.panel.title"));
		}
	}

	public Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Override
	protected boolean canCreateNewEntity() {
		return true;
	}

	protected abstract String getEditTitleKey();

	protected abstract String getNewTitleKey();

	protected abstract E getEditedEntity();

	protected abstract AppActivitiesNames getListActivityName();

	protected abstract ValidationErrorPanel getValidationPanel();

	protected abstract void fillFieldsMap(HashMap<String, HasValidation> fieldsMap);

}
