package be.virtualsushi.jfx.dorse.activities;

import java.util.List;

import be.virtualsushi.jfx.dorse.control.table.EntityPropertyValueFactory;
import be.virtualsushi.jfx.dorse.dialogs.ArticleFilterDialog;
import be.virtualsushi.jfx.dorse.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class ArticleListActivity extends AbstractListActivity<Article> {

	private List<Supplier> suppliers;

	@Override
	protected String getTitle() {
		return getResources().getString("article.list.title");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Article> table) {
		TableColumn<Article, Long> idColumn = createTableColumn("id");

		TableColumn<Article, String> codeColumn = createTableColumn("code");
		codeColumn.setMinWidth(70);

		TableColumn<Article, String> nameColumn = createTableColumn("name");
		nameColumn.setMinWidth(150);

		TableColumn<Article, Float> priceColumn = createTableColumn("price");
    priceColumn.setMinWidth(70);

    TableColumn<Article, Integer> freeQColumn = createTableColumn("freeQuantity");
    freeQColumn.setMinWidth(70);

		TableColumn<Article, String> supplierColumn = createTableColumn("supplier", new EntityStringPropertyValueFactory<Article>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, Article value) {
				if (CollectionUtils.isNotEmpty(suppliers)) {
					for (Supplier supplier : suppliers) {
						if (supplier.getId().equals(value.getSupplier())) {
							property.set(supplier.getName());
							break;
						}
					}
				}
			}
		});
		supplierColumn.setMinWidth(150);

    TableColumn<Article, String> stockColumn = createTableColumn("stock", new EntityStringPropertyValueFactory<Article>() {

      @Override
      protected void setPropertyValue(ObjectProperty<String> property, Article value) {
        if(value.getStock()!=null) property.set(""+value.getStock().getQuantity());
      }
    });
    stockColumn.setMinWidth(30);
		table.getColumns().addAll(idColumn, codeColumn, nameColumn, priceColumn, freeQColumn, supplierColumn, stockColumn, createActionsColumn());
	}

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(suppliers)) {
			suppliers = (List<Supplier>)getRestApiAccessor().getResponse(Supplier.class, false).getData();
		}
	}

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return AppActivitiesNames.VIEW_ARTICLE;
	}

	@Override
	protected AppActivitiesNames getEditActivityName() {
		return AppActivitiesNames.EDIT_ARTICLE;
	}

  @Override
  protected Boolean getFullInfo() {
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse) {
    TableView<Article> table = new TableView<Article>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table);
    table.setItems(FXCollections.observableArrayList((List<Article>) serverResponse.getData()));
    return table;
  }
  @Override
  @FXML
 	protected void handleLaunchFilter(ActionEvent event) {
    showFilterDialog(getResources().getString("article.filter.dialog.title"), ArticleFilterDialog.class);
 	}
}
