package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.events.CancelFilterEvent;
import be.virtualsushi.jfx.dorse.events.ClearFilterEvent;
import be.virtualsushi.jfx.dorse.events.FilterEvent;
import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

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
  protected ComboBox<String> columnSelectorField, sortTypeSelectorField;

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
  @SuppressWarnings("unchecked")
  public void handleClearFilter(ActionEvent event) {
    postFilterEvent(new ClearFilterEvent(this.getClass()));
  }

  @FXML
 	protected void handleSave(ActionEvent event) {
 	}

  public void onShow(Object... parameters) {

    columnSelectorField.setItems(FXCollections.observableArrayList(getColumnNames()));
    sortTypeSelectorField.setItems(FXCollections.observableArrayList(getAscDesc()));
    //columnSelectorField.setValue("");
    //sortTypeSelectorField.setValue("");
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
  protected abstract List<String> getColumnNames();

  protected List<String> getAscDesc(){
    List<String> ascDesc = new ArrayList<String>();
    ascDesc.add("");
    ascDesc.add("ASC");
    ascDesc.add("DESC");
    return ascDesc;
  }

}
