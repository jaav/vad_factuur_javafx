package be.virtualsushi.jfx.dorse.activities;

import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.LIST_ARTICLES;
import static be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames.VIEW_ARTICLE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import be.virtualsushi.jfx.dorse.events.dialogs.SaveStockEvent;
import be.virtualsushi.jfx.dorse.model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.FloatNumberField;
import be.virtualsushi.jfx.dorse.control.IntegerNumberField;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.dialogs.ArticleTypeEditDialog;
import be.virtualsushi.jfx.dorse.dialogs.ModifyStockDialog;
import be.virtualsushi.jfx.dorse.dialogs.SupplierEditDialog;
import be.virtualsushi.jfx.dorse.dialogs.UnitEditDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveArticleTypeEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveSupplierEvent;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveUnitEvent;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditArticleActivity extends AbstractEditActivity<VBox, Article> {

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
  					//doCustomBackgroundInitialization(getEntity());
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
	private Label idField, stockField, stockIdField, createdField;

	@FXML
	private TextField codeField, nameField;

	@FXML
	private FloatNumberField priceField;

	@FXML
	private IntegerNumberField weightField;

	@FXML
	private TextAreaField descriptionField;

	private List<ArticleType> acceptableArticleTypes;
	private List<Unit> acceptableUnits;
	private List<Supplier> acceptableSuppliers;

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
		return new Article();
	}

	@Override
	protected void mapFields(Article editingArticle) {
		idField.setText(String.valueOf(editingArticle.getId()));
    if(editingArticle.getStock()!=null){
      stockField.setText(String.valueOf(editingArticle.getStock().getQuantity()));
      stockIdField.setText(String.valueOf(editingArticle.getStock().getId()));
    }
		codeField.setValue(editingArticle.getCode());
		nameField.setValue(editingArticle.getName());
		descriptionField.setValue(editingArticle.getDescription());
		priceField.setValue(editingArticle.getPrice());
		weightField.setValue(editingArticle.getWeight());
		if (editingArticle.getCreationDate() != null) {
			createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(editingArticle.getCreationDate()));
		}
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
		result.setName(nameField.getValue());
		result.setDescription(descriptionField.getValue());
		if (typeField.getValue() != null) {
			result.setArticleType(typeField.getValue().getId());
		}
		result.setPrice(priceField.getValue());
		result.setWeight(weightField.getValue());
		if (unitField.getValue() != null) {
			result.setUnit(unitField.getValue().getId());
		}
		if (supplierField.getValue() != null) {
			result.setSupplier(supplierField.getValue().getId());
		}
		if (result.isNew() || result.getCreationDate() == null) {
			result.setCreationDate(new Date());
		}
		return result;
	}

	@Subscribe
	public void onSaveStock(SaveStockEvent event) {
    doInBackground(new SaveStockTaskCreator<Stock>(event.getEntity()));
		stockField.setText(String.valueOf(((Stock)(event.getEntity())).getQuantity()));
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
	protected void doCustomBackgroundInitialization(Article editingEntity) {
		acceptableArticleTypes = getRestApiAccessor().getList(ArticleType.class, false);
		acceptableSuppliers = getRestApiAccessor().getList(Supplier.class, false);
		acceptableUnits = getRestApiAccessor().getList(Unit.class, false);
	}

	@Override
	protected AppActivitiesNames getListActivityName() {
		return LIST_ARTICLES;
	}

}
