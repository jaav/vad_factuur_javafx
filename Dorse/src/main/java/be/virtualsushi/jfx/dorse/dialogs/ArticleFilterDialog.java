package be.virtualsushi.jfx.dorse.dialogs;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.events.CancelDialogEvent;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveAddressEvent;
import be.virtualsushi.jfx.dorse.fxml.UiComponentBean;
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Component
public class ArticleFilterDialog extends AbstractFilterDialog {

	@FXML
	private VBox container;

	@FXML
	private TextField nameField, codeField;

  @Override
  protected Article getFilterEntity() {
    Article result = new Article();
    result.setArticleName(nameField.getValue());
    result.setCode(codeField.getValue());
    result.setSortType((String)sortTypeSelectorField.getValue());
    result.setColumnName((String)columnSelectorField.getValue());
    return result;
  }

  @SuppressWarnings("unchecked")
 	@Override
 	public void onShow(Object... parameters) {
 		super.onShow(parameters);
 	}

  protected List<String> getColumnNames(){
    List<String> names = new ArrayList<String>();
    names.add("");
    names.add("Id");
    names.add("Name");
    names.add("Code");
    names.add("Price");
    names.add("Stock");
    return names;
  }
}
