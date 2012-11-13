package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.events.CancelFilterEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.FilterEvent;
import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

public abstract class AbstractFilterDialog<E extends BaseEntity> extends UiComponentBean {

  public static final String VALIDATION_MESSAGE_PATTERN = "%s : %s";

  private EventBus eventBus;

  private Validator validator;

  public EventBus getEventBus() {
    return eventBus;
  }


  @Autowired
  public void setEventBus(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @FXML
  public void handleCancel(ActionEvent event) {
    getEventBus().post(new CancelFilterEvent(this.getClass()));
  }

  @FXML
  @SuppressWarnings("unchecked")
  public void handleFilter(ActionEvent event) {
    postFilterEvent(new FilterEvent(getFilterEntity()));
  }

  @FXML
 	protected void handleSave(ActionEvent event) {
 	}

  public void onShow() {
    if (HasValidationDialog.class.isInstance(this)) {
      resetValidationPanel((HasValidationDialog) this);
    }
  }

  @SuppressWarnings("unchecked")
  protected void postFilterEvent(Object event) {
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

  @Override
  protected void onUiBinded() {
    super.onUiBinded();
  }

  protected abstract E getFilterEntity();

}
