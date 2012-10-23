package be.virtualsushi.jfx.dorse.dialogs;

import java.util.Set;

import be.virtualsushi.jfx.dorse.control.LongNumberField;
import be.virtualsushi.jfx.dorse.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.events.CancelDialogEvent;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;
import be.virtualsushi.jfx.dorse.model.BaseEntity;

import com.google.common.eventbus.EventBus;

public abstract class AbstractDialog extends UiComponentBean {

	public static final String VALIDATION_MESSAGE_PATTERN = "%s : %s";

	private EventBus eventBus;

	private Validator validator;

	public EventBus getEventBus() {
		return eventBus;
	}


  @FXML
 	protected LongNumberField idField;

	@Autowired
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@FXML
	public void handleCancel(ActionEvent event) {
		getEventBus().post(new CancelDialogEvent(this.getClass()));
	}

	public void onShow(Object... parameters) {
		if (HasValidationDialog.class.isInstance(this)) {
			resetValidationPanel((HasValidationDialog) this);
		}
	}

	@SuppressWarnings("unchecked")
	protected void postSaveEvent(Object event) {
		if (HasValidationDialog.class.isInstance(this)) {
			HasValidationDialog thisAsHasValidationDialog = (HasValidationDialog) this;
			resetValidationPanel(thisAsHasValidationDialog);
			Set<ConstraintViolation<BaseEntity>> violations = getValidator().validate(((SaveEntityEvent<BaseEntity>) event).getEntity());
			if (!violations.isEmpty()) {
				for (ConstraintViolation<BaseEntity> violation : violations) {
					String propertyName = violation.getPropertyPath().toString();
					thisAsHasValidationDialog.getValidationPanel().addMessage(
							String.format(VALIDATION_MESSAGE_PATTERN, getResources().getString(propertyName), violation.getMessage()));
					if (thisAsHasValidationDialog.getFieldsMap().containsKey(propertyName)) {
						thisAsHasValidationDialog.getFieldsMap().get(propertyName).setInvalid();
					}
				}
				thisAsHasValidationDialog.showValidationPanel();
				return;
			}
		}
		getEventBus().post(event);
	}

	private void resetValidationPanel(HasValidationDialog dialog) {
		dialog.hideValidationPanel();
		for (String key : dialog.getFieldsMap().keySet()) {
			dialog.getFieldsMap().get(key).clearInvalid();
		}
		dialog.getValidationPanel().clearMessages();
	}

	public Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
