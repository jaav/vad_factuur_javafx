package be.virtualsushi.jfx.dorse.activities;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;
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

  private void clearFields(){
    Parent root = title.getScene().getRoot();
    Set<Node> inputfields = new HashSet<Node>();
    inputfields.addAll(root.lookupAll("TextField"));
    inputfields.addAll(root.lookupAll("TextArea"));
    inputfields.addAll(root.lookupAll("IntegerNumberField"));
    inputfields.addAll(root.lookupAll("DatePicker"));
    inputfields.addAll(root.lookupAll("FloatNumberField"));
    inputfields.addAll(root.lookupAll("ComboBox"));
    inputfields.addAll(root.lookupAll("EditableList"));
    Object nil = null;
    for (Node inputfield : inputfields) {
      try {
        PropertyDescriptor valueDescriptor = new PropertyDescriptor("value", inputfield.getClass());
        Method valueSetter = valueDescriptor!=null ? valueDescriptor.getWriteMethod() : null;
        if(valueSetter!=null) valueSetter.invoke(inputfield, nil);
      } catch (IllegalAccessException e) {
      } catch (InvocationTargetException e) {
      } catch (IntrospectionException e) {
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        PropertyDescriptor textDescriptor = new PropertyDescriptor("text", inputfield.getClass());
        Method textSetter = textDescriptor!=null ? textDescriptor.getWriteMethod() : null;
        if(textSetter!=null) textSetter.invoke(inputfield, "");
      } catch (IllegalAccessException e) {
      } catch (InvocationTargetException e) {
      } catch (IntrospectionException e) {
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        PropertyDescriptor dateDescriptor = new PropertyDescriptor("selectedDate", inputfield.getClass());
        Method dateSetter = dateDescriptor!=null ? dateDescriptor.getWriteMethod() : null;
        if(dateSetter!=null) dateSetter.invoke(inputfield, nil);
      } catch (IllegalAccessException e) {
      } catch (InvocationTargetException e) {
      } catch (IntrospectionException e) {
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

	@Override
	protected void started() {
		super.started();

		clearInvalid();
    clearFields();
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
