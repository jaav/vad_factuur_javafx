package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.model.*;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_POST;

@Component
@Scope("prototype")
public class EditPostActivity extends AbstractEditActivity<HBox, Post> {



	@FXML
	private Label idField, idLabel, nameLabel, bottomLabel, topLabel;

	@FXML
	private TextField nameField;

	@FXML
	private FloatNumberField priceField, bottomField, topField;

	@FXML
	private ValidationErrorPanel validationPanel;

	@Override
	protected String getEditTitleKey() {
		return "edit.post";
	}

	@Override
	protected String getNewTitleKey() {
		return "new.post";
	}

  @Override
 	protected Post newEntityInstance() {
 		Post post = new Post();
 		return post;
 	}

  @Override
  protected void doCustomBackgroundInitialization(Post entity) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
	protected void mapFields(Post editingPost) {
		if(editingPost!=null){
      if(editingPost.getId()!=null) idLabel.setText(String.valueOf(editingPost.getId()));
      nameField.setValue(editingPost.getName());
      bottomField.setValue(editingPost.getBottom());
      topField.setValue(editingPost.getTop());
      priceField.setValue(editingPost.getPrice());
    }
	}

	@Override
	protected Post getEditedEntity() {
		Post result = getEntity();
		result.setName(nameField.getValue());
		result.setPrice(priceField.getValue());
    result.setBottom(bottomField.getValue());
    result.setTop(topField.getValue());
		return result;
	}

	@Override
	protected AppActivitiesNames getListActivityName() {
		return LIST_POST;
	}

	@Override
	protected ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	protected void fillFieldsMap(HashMap<String, HasValidation> fieldsMap) {
		fieldsMap.put("name", nameField);
		fieldsMap.put("price", priceField);
    fieldsMap.put("bottom", bottomField);
    fieldsMap.put("top", topField);
	}

  @Override
  protected void clearForm() {
    nameField.setValue("");
    priceField.setValue(0F);
    bottomField.setValue(0F);
    topField.setValue(0F);
  }
}
