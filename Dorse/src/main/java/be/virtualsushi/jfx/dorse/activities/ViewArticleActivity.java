package be.virtualsushi.jfx.dorse.activities;

import java.text.SimpleDateFormat;
import java.util.List;

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
		stockField.setText(String.valueOf(article.getStock().getQuantity()));
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
	protected void doCustomBackgroundInitialization(Article entity) {
		acceptableTypes = getRestApiAccessor().getList(ArticleType.class, false);
		acceptableUnits = getRestApiAccessor().getList(Unit.class, false);
		acceptableSuppliers = getRestApiAccessor().getList(Supplier.class, false);
	}

}
