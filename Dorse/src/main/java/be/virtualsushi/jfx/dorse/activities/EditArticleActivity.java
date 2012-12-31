package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_ARTICLES;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.control.calendar.CalendarView;
import be.virtualsushi.jfx.dorse.control.calendar.DatePicker;
import be.virtualsushi.jfx.dorse.dialogs.*;
import be.virtualsushi.jfx.dorse.utils.AppVariables;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.events.dialogs.SaveArticleTypeEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveStockEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSupplierEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveUnitEvent;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ArticleType;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.model.IdNamePairEntity;
import be.virtualsushi.jfx.dorse.model.Stock;
import be.virtualsushi.jfx.dorse.model.Supplier;
import be.virtualsushi.jfx.dorse.model.Unit;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditArticleActivity extends AbstractEditActivity<HBox, Article> {

	private class SaveIdNamePairEntityTaskCreator<E extends IdNamePairEntity> implements TaskCreator<DorseBackgroundTask<E>> {

		private final E entity;

		public SaveIdNamePairEntityTaskCreator(E entityToSave) {
			this.entity = entityToSave;
		}

		@Override
		public DorseBackgroundTask<E> createTask() {
			return new DorseBackgroundTask<E>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected E call() throws Exception {
					getRestApiAccessor().save((E) getParameters()[0]);
					doCustomBackgroundInitialization(getEntity());
					return (E) getParameters()[0];
				}

				@Override
				protected void onSuccess(E value) {
					mapLists(getEntity());
					hideLoadingMask();
				}

			};
		}

	}

	private class SaveStockTaskCreator<Stock> implements TaskCreator<DorseBackgroundTask<Stock>> {

		private final Stock stock;

		public SaveStockTaskCreator(Stock entityToSave) {
			this.stock = entityToSave;
		}

		@Override
		public DorseBackgroundTask<Stock> createTask() {
			return new DorseBackgroundTask<Stock>(this, stock) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected Stock call() throws Exception {
					getRestApiAccessor().save((BaseEntity) getParameters()[0]);
					// doCustomBackgroundInitialization(getEntity());
					return (Stock) getParameters()[0];
				}

				@Override
				protected void onSuccess(Stock value) {
					mapLists(getEntity());
					hideLoadingMask();
				}

			};
		}

	}

	@FXML
	private EditableList<ArticleType> typeField;

	@FXML
	private EditableList<Unit> unitField;

	@FXML
	private EditableList<Supplier> supplierField;

  @FXML
 	private ComboBox vatField;

	@FXML
	private Label idField, stockField, stockIdField, createdField;

	@FXML
	private TextField codeField, nameField;

	@FXML
	private FloatNumberField priceField;

	@FXML
	private IntegerNumberField weightField, freeQuantityField;

	@FXML
	private TextAreaField descriptionField;

	@FXML
	private ValidationErrorPanel validationPanel;

  @FXML
 	private DatePicker copyDateField;

  @FXML
 	private EditButton stockButton;

	private List<ArticleType> acceptableArticleTypes;
	private List<Unit> acceptableUnits;
	private List<Supplier> acceptableSuppliers;

  @Override
 	public void started() {
 		super.started();
    title.getScene().getStylesheets().add("calendarstyle.css");
    copyDateField.setLocale(Locale.GERMAN);

  }

	@Override
	public void initialize() {
		super.initialize();

		typeField.setEditHandler(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        showDialog(getResources().getString("article.type.edit.dialog.title"), ArticleTypeEditDialog.class);
      }
    });

		unitField.setEditHandler(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showDialog(getResources().getString("unit.edit.dialog.title"), UnitEditDialog.class);
			}
		});

		supplierField.setEditHandler(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showDialog(getResources().getString("supplier.edit.dialog.title"), SupplierEditDialog.class);
			}
		});
	}

	@FXML
	protected void handleEditStock(ActionEvent event) {
		showDialog(getResources().getString("modify.stock.dialog.title"), ModifyStockDialog.class, getEntity().getStock());
	}

	@Override
	protected String getEditTitleKey() {
		return "edit.article";
	}

	@Override
	protected String getNewTitleKey() {
		return "new.article";
	}

	@Override
	protected Article newEntityInstance() {
		Article article = new Article();
		article.setCreationDate(new Date());
		return article;
	}

	@Override
	protected void mapFields(Article editingArticle) {
		idField.setText(String.valueOf(editingArticle.getId()));
		if (editingArticle.getStock() != null) {
			stockField.setText(String.valueOf(editingArticle.getStock().getQuantity()));
			stockIdField.setText(String.valueOf(editingArticle.getStock().getId()));
		}
		codeField.setValue(editingArticle.getCode());
		nameField.setValue(editingArticle.getArticleName());
		descriptionField.setValue(editingArticle.getDescription());
		priceField.setValue(editingArticle.getPrice());
    freeQuantityField.setValue(editingArticle.getFreeQuantity());
		weightField.setValue(editingArticle.getWeight());
		if (editingArticle.getCreationDate() != null) {
			createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(editingArticle.getCreationDate()));
		}
    if (editingArticle.getCopyDate() != null) {
      copyDateField.setSelectedDate(editingArticle.getCopyDate());
  		}
    if(editingArticle.getVat()!=null) vatField.setValue(editingArticle.getVat()+"%");
		mapLists(editingArticle);
	}

	private void mapLists(Article editingArticle) {
		unitField.setAcceptableValues(acceptableUnits);
		for (Unit unit : unitField.getAcceptableValues()) {
			if (editingArticle.getUnit() == unit.getId()) {
				unitField.setValue(unit);
				break;
			}
		}
		typeField.setAcceptableValues(acceptableArticleTypes);
		for (ArticleType type : typeField.getAcceptableValues()) {
			if (editingArticle.getArticleType() == type.getId()) {
				typeField.setValue(type);
				break;
			}
		}
		supplierField.setAcceptableValues(acceptableSuppliers);
		for (Supplier supplier : supplierField.getAcceptableValues()) {
			if (editingArticle.getSupplier() == supplier.getId()) {
				supplierField.setValue(supplier);
				break;
			}
		}
	}

	@Override
	protected Article getEditedEntity() {
		Article result = getEntity();
		result.setCode(codeField.getValue());
		result.setArticleName(nameField.getValue());
		result.setDescription(descriptionField.getValue());
		if (typeField.getValue() != null) {
			result.setArticleType(typeField.getValue().getId());
		}
		result.setPrice(priceField.getValue());
    result.setFreeQuantity(freeQuantityField.getValue());
		result.setWeight(weightField.getValue());
		if (unitField.getValue() != null) {
			result.setUnit(unitField.getValue().getId());
		}
		if (supplierField.getValue() != null) {
			result.setSupplier(supplierField.getValue().getId());
		}
    if (vatField.getValue() != null) {
      result.setVat(Float.parseFloat(vatField.getValue().toString().replaceAll(" ", "").replace("%", "")));
    }
		if (result.isNew() || result.getCreationDate() == null) {
			result.setCreationDate(new Date());
		}
    result.setCopyDate(copyDateField.getSelectedDate());
    HashMap test = getAppVariables();
    AppVariables appv = getAppVariables();
    if(getAppVariables().get(AUTHTOKEN_KEY)!=null && getAppVariables().get(USERNAME_KEY)!=null)
      result.setCreator((Long)getAppVariables().get(USERNAME_ID));
		return result;
	}

	@Subscribe
	public void onSaveStock(SaveStockEvent event) {
		doInBackground(new SaveStockTaskCreator<Stock>(event.getEntity()));
		stockField.setText(String.valueOf(((Stock) (event.getEntity())).getQuantity()));
		hideDialog(ModifyStockDialog.class);
	}

	@Subscribe
	public void onSaveUnit(SaveUnitEvent event) {
		doInBackground(new SaveIdNamePairEntityTaskCreator<IdNamePairEntity>(event.getEntity()));
	}

	@Subscribe
	public void onSaveArticleType(SaveArticleTypeEvent event) {
		doInBackground(new SaveIdNamePairEntityTaskCreator<IdNamePairEntity>(event.getEntity()));
	}

	@Subscribe
	public void onSaveSupplier(SaveSupplierEvent event) {
		doInBackground(new SaveIdNamePairEntityTaskCreator<IdNamePairEntity>(event.getEntity()));
	}

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Article editingEntity) {
    if(editingEntity.getId()!=null){
      stockButton.setVisible(true);
      idField.setVisible(true);
    }
    else{
      stockButton.setVisible(false);
      idField.setVisible(false);
    }
		acceptableArticleTypes = (List<ArticleType>)getRestApiAccessor().getResponse(ArticleType.class, false).getData();
    acceptableArticleTypes.add(0, ArticleType.getEmptyArticleType());
		acceptableSuppliers = (List<Supplier>)getRestApiAccessor().getResponse(Supplier.class, false).getData();
    acceptableSuppliers.add(0, Supplier.getEmptySupplier());
		acceptableUnits = (List<Unit>)getRestApiAccessor().getResponse(Unit.class, false).getData();
    acceptableUnits.add(0, Unit.getEmptyUnit());
    copyDateField.setDateFormat(new SimpleDateFormat(getResources().getString("input.date.format")));
    copyDateField.getCalendarView().setShowTodayButton(true);
	}

	@Override
	protected AppActivitiesNames getListActivityName() {
		return LIST_ARTICLES;
	}

	@Override
	protected ValidationErrorPanel getValidationPanel() {
		return validationPanel;
	}

	@Override
	protected void fillFieldsMap(HashMap<String, HasValidation> fieldsMap) {
		fieldsMap.put("articleType", typeField);
		fieldsMap.put("unit", unitField);
		fieldsMap.put("supplier", supplierField);
		fieldsMap.put("code", codeField);
		fieldsMap.put("name", nameField);
		fieldsMap.put("price", priceField);
    fieldsMap.put("freeQuantity", freeQuantityField);
		fieldsMap.put("weight", weightField);
		fieldsMap.put("description", descriptionField);
	}

  @Override
  protected void clearForm() {
    nameField.setValue("");
    codeField.setValue("");
    copyDateField.setSelectedDate(null);
    vatField.getSelectionModel().clearSelection();
    descriptionField.setValue("");
    typeField.setValue(ArticleType.getEmptyArticleType());
    supplierField.setValue(Supplier.getEmptySupplier());
    weightField.setValue(0);
    priceField.setValue(0F);
    freeQuantityField.setValue(0);
    unitField.setValue(Unit.getEmptyUnit());
  }
}
