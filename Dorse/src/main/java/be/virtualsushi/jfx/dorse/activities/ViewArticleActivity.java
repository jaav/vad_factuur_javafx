package be.virtualsushi.jfx.dorse.activities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.model.Article;

@Component
@Scope("prototype")
public class ViewArticleActivity extends AbstractViewActivity<VBox, Article> {

	@FXML
	private Label codeField, idField, stockField, createdField, descriptionField, typeField, priceField, weightField, unitField, supplierField;

	@Override
	protected void mapFields() {
		idField.setText(String.valueOf(getViewingEntity().getId()));
		codeField.setText(getViewingEntity().getCode());
		stockField.setText("0");
		// TODO implement
	}

}
