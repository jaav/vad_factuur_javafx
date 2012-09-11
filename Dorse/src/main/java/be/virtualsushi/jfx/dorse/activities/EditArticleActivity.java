package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.EditableList;
import be.virtualsushi.jfx.dorse.control.TextAreaField;
import be.virtualsushi.jfx.dorse.control.TextField;
import be.virtualsushi.jfx.dorse.dialogs.ArticleTypeEditDialog;
import be.virtualsushi.jfx.dorse.dialogs.ModifyStockDialog;
import be.virtualsushi.jfx.dorse.dialogs.SupplierEditDialog;
import be.virtualsushi.jfx.dorse.dialogs.UnitEditDialog;
import be.virtualsushi.jfx.dorse.events.SaveEntityEvent;
import be.virtualsushi.jfx.dorse.events.SetValueEvent;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.ArticleType;
import be.virtualsushi.jfx.dorse.model.Supplier;
import be.virtualsushi.jfx.dorse.model.Unit;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditArticleActivity extends AbstractEditActivity<VBox, Article> {

	@FXML
	private EditableList<ArticleType> typeField;

	@FXML
	private EditableList<Unit> unitField;

	@FXML
	private EditableList<Supplier> supplierField;

	@FXML
	private Label idField, stockField, createdField;

	@FXML
	private TextField codeField, nameField, priceField, weightField;

	@FXML
	private TextAreaField descriptionField;

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
		showDialog(getResources().getString("modify.stock.dialog.title"), ModifyStockDialog.class);
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
	protected void mapFields(Article editingActivity) {

	}

	@Override
	protected Article getEditedEntity() {
		return null;
	}

	@Subscribe
	public void onSaveStock(SetValueEvent<Integer> event) {
		// TODO save stock
	}

	@Subscribe
	public void onSaveUnit(SaveEntityEvent<Unit> event) {
		getRestApiAccessor().save(event.getEntity());
		hideDialog(UnitEditDialog.class);
	}

	@Subscribe
	public void onSaveArticleType(SaveEntityEvent<ArticleType> event) {
		getRestApiAccessor().save(event.getEntity());
		hideDialog(ArticleTypeEditDialog.class);
	}

	@Subscribe
	public void onSaveSupplier(SaveEntityEvent<Supplier> event) {
		getRestApiAccessor().save(event.getEntity());
		hideDialog(SupplierEditDialog.class);
	}

	@Override
	protected void doCustomBackgroundInitialization(Article editingEntity) {
		// TODO Auto-generated method stub
		
	}

}
