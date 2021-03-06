package be.virtualsushi.jfx.dorse.activities;

import java.text.SimpleDateFormat;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ArticleType;
import be.virtualsushi.jfx.dorse.model.Supplier;
import be.virtualsushi.jfx.dorse.model.Unit;

@Component
@Scope("prototype")
public class ViewArticleActivity extends AbstractViewEntityActivity<VBox, Article> {

	@FXML
	private Label codeField, idField, stockField, createdField, descriptionField, typeField;

	@FXML
	private Label priceField, weightField, unitField, supplierField;

	private List<ArticleType> acceptableTypes;
	private List<Unit> acceptableUnits;
	private List<Supplier> acceptableSuppliers;

	@Override
	protected void mapFields(Article article) {
		super.mapFields(article);
		idField.setText(String.valueOf(article.getId()));
		codeField.setText(article.getCode());
		if (article.getStock() != null) {
			stockField.setText(String.valueOf(article.getStock().getQuantity()));
		}
		if (article.getCreationDate() != null) {
			createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(article.getCreationDate()));
		}
		descriptionField.setText(article.getDescription());
		for (ArticleType type : acceptableTypes) {
			if (type.getId() == article.getArticleType()) {
				typeField.setText(type.getName());
				break;
			}
		}
		for (Unit unit : acceptableUnits) {
			if (unit.getId() == article.getUnit()) {
				unitField.setText(unit.getName());
				break;
			}
		}
		for (Supplier supplier : acceptableSuppliers) {
			if (supplier.getId() == article.getSupplier()) {
				supplierField.setText(supplier.getName());
				break;
			}
		}
		priceField.setText(String.valueOf(article.getPrice()));
		weightField.setText(String.valueOf(article.getWeight()));
	}

  @Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Article entity) {
    acceptableTypes = (List<ArticleType>)getRestApiAccessor().getResponse(ArticleType.class, false).getData();
    acceptableSuppliers = (List<Supplier>)getRestApiAccessor().getResponse(Supplier.class, false).getData();
    acceptableUnits = (List<Unit>)getRestApiAccessor().getResponse(Unit.class, false).getData();
	}

}
