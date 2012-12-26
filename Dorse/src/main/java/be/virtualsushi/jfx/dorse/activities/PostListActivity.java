package be.virtualsushi.jfx.dorse.activities;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.model.Post;
import be.virtualsushi.jfx.dorse.model.PostResponse;
import be.virtualsushi.jfx.dorse.model.ServerResponse;
import be.virtualsushi.jfx.dorse.model.Supplier;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Scope("prototype")
@FxmlFile("PostListActivity.fxml")
public class PostListActivity extends AbstractListActivity<Post> {

	private List<Supplier> suppliers;

	@Override
	protected String getTitle() {
		return getResources().getString("post.list.title");
	}

  @SuppressWarnings("unchecked")
 	@Override
 	protected void fillTableColumns(TableView<Post> table) {
    fillTableColumns(table, "id", true);
  }

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Post> table, String columnName, boolean asc) {
    //SortTypeChangeListener sortTypeChangeListener = new SortTypeChangeListener();

    TableColumn<Post, Long> idColumn = createTableColumn("id");
    //checkSorting("id", idColumn, columnName, asc);
    //idColumn.sortTypeProperty().addListener(sortTypeChangeListener);


		TableColumn<Post, String> nameColumn = createTableColumn("name");
		nameColumn.setMinWidth(70);

		TableColumn<Post, Float> bottomColumn = createTableColumn("bottom");
		bottomColumn.setMinWidth(50);

    TableColumn<Post, Float> topColumn = createTableColumn("top");
    topColumn.setMinWidth(50);

    TableColumn<Post, Float> priceColumn = createTableColumn("price");
    priceColumn.setMinWidth(50);

		table.getColumns().addAll(idColumn, nameColumn, bottomColumn, topColumn, priceColumn, createActionsColumn());
	}

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(suppliers)) {
			suppliers = (List<Supplier>)getRestApiAccessor().getResponse(Supplier.class, false).getData();
      suppliers.add(0, Supplier.getEmptySupplier());
		}
	}

	@Override
	protected AppActivitiesNames getViewActivityName() {
		return AppActivitiesNames.VIEW_POST;
	}

	@Override
	protected AppActivitiesNames getEditActivityName() {
		return AppActivitiesNames.EDIT_POST;
	}

  @Override
  protected Boolean getFullInfo() {
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse) {
    return createPage(serverResponse, "id", true);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse, String columnName, final boolean asc) {
    table = new TableView<Post>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table, columnName, asc);


    if(serverResponse!=null && serverResponse.getData()!=null)
      table.setItems(FXCollections.observableArrayList((List<Post>) serverResponse.getData()));
    return table;
  }

  @Override
  protected String createCsv(ServerResponse serverResponse, File target) {
    return null;
  }
}
