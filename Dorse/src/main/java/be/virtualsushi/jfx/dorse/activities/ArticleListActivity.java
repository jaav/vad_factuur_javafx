package be.virtualsushi.jfx.dorse.activities;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Supplier;
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
		codeColumn.setMinWidth(150);

		TableColumn<Article, String> nameColumn = createTableColumn("name");
		nameColumn.setMinWidth(150);

		TableColumn<Article, String> descriptionColumn = createTableColumn("description");
		descriptionColumn.setMinWidth(300);

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
		table.getColumns().addAll(idColumn, codeColumn, nameColumn, descriptionColumn, supplierColumn, createActionsColumn());
	}

	@Override
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(suppliers)) {
			suppliers = getRestApiAccessor().getList(Supplier.class, false);
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

}
