package be.virtualsushi.jfx.dorse.activities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.model.Article;

@Component
@Scope("prototype")
public class ViewArticleActivity extends AbstractManageEntityActivity<VBox, Article> {

	@FXML
	private Label codeField, idField, stockField, createdField, descriptionField, typeField, priceField, weightField, unitField, supplierField;

	@Override
	protected void mapFields(Article article) {
		idField.setText(String.valueOf(article.getId()));
		codeField.setText(article.getCode());
		stockField.setText("0");
		// TODO implement
	}

	@Override
	protected void doCustomBackgroundInitialization(Article entity) {
		// TODO Auto-generated method stub

	}

}
