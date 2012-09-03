package be.virtualsushi.jfx.dorse.activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.dialogs.ModifyStockDialog;
import be.virtualsushi.jfx.dorse.events.SaveStockEvent;
import be.virtualsushi.jfx.dorse.model.Article;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class EditArticleActivity extends AbstractEditActivity<VBox, Article> {

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
	protected void mapFields() {

	}

	@Override
	protected Article getEditedEntity() {
		return null;
	}

	@Subscribe
	public void onSaveStock(SaveStockEvent event) {
		// TODO save stock
	}

}
