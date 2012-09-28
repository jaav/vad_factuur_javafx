package be.virtualsushi.jfx.dorse.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.ComboBoxField;
import be.virtualsushi.jfx.dorse.control.FloatNumberField;
import be.virtualsushi.jfx.dorse.control.HasValidation;
import be.virtualsushi.jfx.dorse.control.IntegerNumberField;
import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveOrderLineEvent;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.OrderLine;
import be.virtualsushi.jfx.dorse.model.Unit;

@Component
public class EditOrderLineDialog extends AbstractDialog implements HasValidationDialog {

	private static final String ARTICLE_INFO_VALUE_PATTERN = "%s - %s - %s";

	@FXML
	protected VBox container;

	@FXML
	private ComboBoxField<Article> articleField;

	@FXML
	private FloatNumberField discountField;

	@FXML
	private IntegerNumberField quantityField;

	@FXML
	private Label articleInfoField, lineTotalField;

	private ValidationErrorPanel validationPanel;

	private OrderLine editingOrderLine;

	private List<Unit> units;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	protected void handleSave(ActionEvent event) {
		postSaveEvent(new SaveOrderLineEvent(getEditedValue()));
	}

	private OrderLine getEditedValue() {
		if (editingOrderLine == null) {
			editingOrderLine = new OrderLine();
		}
		editingOrderLine.setArticle(articleField.getValue().getId());
		editingOrderLine.setDiscount(discountField.getValue());
		editingOrderLine.setQuantity(quantityField.getValue());
		return editingOrderLine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);
		
		if (ArrayUtils.isNotEmpty(parameters)) {
			articleField.setAcceptableValues((List<Article>) parameters[0]);
			units = (List<Unit>) parameters[1];
			if (parameters.length > 2) {
				mapFields((OrderLine) parameters[2]);
			}
		}
	}

	private void mapFields(OrderLine value) {
		editingOrderLine = value;
		for (Article article : articleField.getAcceptableValues()) {
			if (article.getId().equals(value.getArticle())) {
				articleField.setValue(article);
				break;
			}
		}
		discountField.setValue(value.getDiscount());
		quantityField.setValue(value.getQuantity());
	}

	@Override
	protected void onUiBinded() {
		articleField.valueProperty().addListener(new ChangeListener<Article>() {

			@Override
			public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
				if (newValue != null) {
					updateArticleInfoField(newValue);
					updateLineTotalField(quantityField.getValue(), discountField.getValue(), newValue.getPrice());
				}
			}

		});
		quantityField.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (newValue != null && articleField.getValue() != null) {
					updateLineTotalField(newValue, discountField.getValue(), articleField.getValue().getPrice());
				}
			}
		});
		discountField.valueProperty().addListener(new ChangeListener<Float>() {

			@Override
			public void changed(ObservableValue<? extends Float> observable, Float oldValue, Float newValue) {
				if (newValue != null && articleField.getValue() != null) {
					updateLineTotalField(quantityField.getValue(), newValue, articleField.getValue().getPrice());
				}
			}
		});
		validationPanel = new ValidationErrorPanel(false);
		fieldsMap = new HashMap<String, HasValidation>();
		fieldsMap.put("article", articleField);
		fieldsMap.put("discount", discountField);
		fieldsMap.put("quantity", quantityField);

	}

	private void updateArticleInfoField(Article value) {
		String articlePrice = value.getPrice() != null ? String.valueOf(value.getPrice()) : "0";
		String articleCode = value.getCode() != null ? value.getCode() : "<n/a>";
		String articleUnit = value.getUnit() != null ? findUnit(value).getName() : "<n/a>";
		articleInfoField.setText(String.format(ARTICLE_INFO_VALUE_PATTERN, articlePrice, articleCode, articleUnit));
	}

	private void updateLineTotalField(Integer quantity, Float discount, Float price) {
		if (price != null && discount != null && quantity != null) {
			lineTotalField.setText(String.valueOf((price - discount) * quantity));
		}
	}

	private Unit findUnit(Article article) {
		for (Unit unit : units) {
			if (unit.getId().equals(article.getUnit())) {
				return unit;
			}
		}
		throw new IllegalStateException("Can't find unit with id=" + article.getUnit());
	}

	@Override
	public ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	public Map<String, HasValidation> getFieldsMap() {
		return fieldsMap;
	}

	@Override
	public void showValidationPanel() {
		container.getChildren().add(0, validationPanel);
	}

	@Override
	public void hideValidationPanel() {
		container.getChildren().remove(validationPanel);
	}

}
