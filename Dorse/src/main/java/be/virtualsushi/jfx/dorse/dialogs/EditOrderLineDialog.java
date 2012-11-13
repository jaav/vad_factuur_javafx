package be.virtualsushi.jfx.dorse.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.virtualsushi.jfx.dorse.model.OrderLineProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
import be.virtualsushi.jfx.dorse.model.OrderLineProperty;
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
	private Label lineTotalField;

  @FXML
  private CheckBox applyFreeCheck;

	private ValidationErrorPanel validationPanel;

	private OrderLineProperty editingOrderLine;

	private List<Unit> units;

	private Map<String, HasValidation> fieldsMap;

	@FXML
	protected void handleSave(ActionEvent event) {
		postSaveEvent(new SaveOrderLineEvent(getEditedValue()));
	}

	private OrderLineProperty getEditedValue() {
		if (editingOrderLine == null) {
			editingOrderLine = new OrderLineProperty();
		}
		editingOrderLine.setUnitDiscount(discountField.getValue());
		editingOrderLine.setQuantity(quantityField.getValue());
    editingOrderLine.setArticleId(articleField.getValue().getId());
    editingOrderLine.setArticleCode(articleField.getValue().getCode());
    editingOrderLine.setArticlePrice(articleField.getValue().getPrice());
    editingOrderLine.setArticleName(articleField.getValue().getName());
    if(applyFreeCheck.isSelected()){
      editingOrderLine.setApplyFree(true);
      editingOrderLine.setArticleFreeQuantity(articleField.getValue().getFreeQuantity());
    }
    else{
      editingOrderLine.setApplyFree(false);
      editingOrderLine.setArticleFreeQuantity(0);
    }
    editingOrderLine.setLineTotal(OrderLineProperty.getFormattedTotalPrice(applyFreeCheck.isSelected(),
        editingOrderLine.getQuantity(),
        editingOrderLine.getArticleFreeQuantity(),
        editingOrderLine.getArticlePrice(),
        editingOrderLine.getUnitDiscount()));
		return editingOrderLine;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onShow(Object... parameters) {
		super.onShow(parameters);
		
		if (ArrayUtils.isNotEmpty(parameters)) {
			articleField.setAcceptableValues((List<Article>) parameters[0]);
			units = (List<Unit>) parameters[1];
      mapFields((OrderLineProperty) parameters[2]);
		}
	}

	private void mapFields(OrderLineProperty value) {
		editingOrderLine = value;
		for (Article article : articleField.getAcceptableValues()) {
			if (article.getId().equals(value.getArticleId())) {
				articleField.setValue(article);
				break;
			}
		}
		discountField.setValue(value.getUnitDiscount());
		quantityField.setValue(value.getQuantity());
    if(value.getApplyFree()) applyFreeCheck.setSelected(true);
    else applyFreeCheck.setSelected(false);
	}

	@Override
	protected void onUiBinded() {
		articleField.valueProperty().addListener(new ChangeListener<Article>() {

			@Override
			public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
				if (newValue != null) {
					//updateArticleInfoField(newValue);
					updateLineTotalField(quantityField.getValue(), discountField.getValue(), newValue, applyFreeCheck.isSelected());
				}
			}

		});
		quantityField.valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (newValue != null && articleField.getValue() != null) {
					updateLineTotalField(newValue, discountField.getValue(), articleField.getValue(), applyFreeCheck.isSelected());
				}
			}
		});
		discountField.valueProperty().addListener(new ChangeListener<Float>() {

			@Override
			public void changed(ObservableValue<? extends Float> observable, Float oldValue, Float newValue) {
				if (newValue != null && articleField.getValue() != null) {
					updateLineTotalField(quantityField.getValue(), newValue, articleField.getValue(), applyFreeCheck.isSelected());
				}
			}
		});
    applyFreeCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue != null && articleField.getValue() != null) {
          updateLineTotalField(quantityField.getValue(), discountField.getValue(), articleField.getValue(), newValue);
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
		//articleInfoField.setText(String.format(ARTICLE_INFO_VALUE_PATTERN, articlePrice, articleCode, articleUnit));
	}

	private void updateLineTotalField(Integer quantity, Float discount, Article article, Boolean applyFree) {
		if (article != null && article.getPrice()!=null && article.getFreeQuantity()!=null && discount != null && quantity != null) {
      if(applyFree && article.getFreeQuantity()>0){
        Float total = (article.getPrice() - discount) * (quantity-article.getFreeQuantity());
        if(total<0) total = 0F;
        lineTotalField.setText(String.valueOf(total));
      }
      else
			  lineTotalField.setText(String.valueOf((article.getPrice() - discount) * quantity));
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
