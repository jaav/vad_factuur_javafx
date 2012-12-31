package be.virtualsushi.jfx.dorse.activities;

import java.io.File;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.table.EntityPropertyValueFactory;
import be.virtualsushi.jfx.dorse.dialogs.ArticleFilterDialog;
import be.virtualsushi.jfx.dorse.model.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    fillTableColumns(table, "id", true);
  }

	@SuppressWarnings("unchecked")
	@Override
	protected void fillTableColumns(TableView<Article> table, String columnName, boolean asc) {
    //SortTypeChangeListener sortTypeChangeListener = new SortTypeChangeListener();

    TableColumn<Article, Long> idColumn = createTableColumn("id");
    //checkSorting("id", idColumn, columnName, asc);
    //idColumn.sortTypeProperty().addListener(sortTypeChangeListener);


		TableColumn<Article, String> codeColumn = createTableColumn("code");
		codeColumn.setMinWidth(70);

		TableColumn<Article, String> nameColumn = createTableColumn("articleName");
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

  private void checkSorting(String columnName, TableColumn<Article, ?> aColumn, String columnToSort, boolean asc){
    if(columnName.equalsIgnoreCase(columnToSort)){
      if(asc)
        aColumn.setSortType(TableColumn.SortType.ASCENDING);
      else
        aColumn.setSortType(TableColumn.SortType.DESCENDING);
      table.getSortOrder().add(aColumn);
    }
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
    return createPage(serverResponse, "id", true);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected TableView createPage(ServerResponse serverResponse, String columnName, final boolean asc) {
    table = new TableView<Article>();
    table.setMaxHeight(Double.MAX_VALUE);
    table.setMaxWidth(Double.MAX_VALUE);
    fillTableColumns(table, columnName, asc);
    /*table.getSortOrder().addListener(new ListChangeListener<TableColumn<Article, ?>>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TableColumn<Article, ?>> change) {
          //System.out.println("THIS HAPPENS IF ARROW APPEARS OR DISAPPEARS ON ALL COLUMNS");
          if(change.getList().size()>0){
            String clickedColumnName = change.getList().get(0).getText();
            boolean clickedSortType = true;
            if(change.getList().get(0).getSortType().equals(TableColumn.SortType.DESCENDING)) clickedSortType = false;
            System.out.println("ORDER ON "+clickedColumnName+" IN ASC("+asc+") ORDER");
            doInBackground(new LoadPageDataTaskCreator(0 * getItemsPerPageCount(), getItemsPerPageCount(), clickedColumnName, getFullInfo(), false, clickedSortType, null, null));
          }
          else{
            System.out.println("REMOVE ALL ORDERING");
            doInBackground(new LoadPageDataTaskCreator(0 * getItemsPerPageCount(), getItemsPerPageCount(), "id", getFullInfo(), false, true, null, null));
          }
        }
    });*/


    if(serverResponse!=null && serverResponse.getData()!=null)
      table.setItems(FXCollections.observableArrayList((List<Article>) serverResponse.getData()));
    return table;
  }

  @Override
  protected String createCsv(ServerResponse serverResponse, File target) {
    exportService.createCsv((ArticleResponse)serverResponse, target, suppliers);
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  @FXML
 	protected void handleLaunchFilter(ActionEvent event) {
    showFilterDialog(getResources().getString("article.filter.dialog.title"), ArticleFilterDialog.class);
 	}

  @Override
  protected String getDefaultOrder() {
    return Article.DEFAULT_COLUMN;
  }

  @Override
  protected Boolean getDefaultAsc() {
    return Article.DEFAULT_ASC;
  }

  /*@Override
  protected void setDependingList() {
    dependingList = suppliers;
  }*/

  /*private class SortTypeChangeListener implements InvalidationListener {

      @Override
      public void invalidated(Observable o) {
        System.out.println("THIS ONLY HAPPENS IF SORTING GOES FROM ASC TO DESC IN THE SAME COLUMN");
          *//**
           * If the column is not in sortOrder list, just ignore.
           * It avoids intermittent duplicate reload() calling
           *//*
          TableColumn col = (TableColumn) ((SimpleObjectProperty) o).getBean();
          ObservableList<TableColumn<Article, ?>> obsList = table.getSortOrder();
          if (!table.getSortOrder().contains(col)) {
              return;
          }
        *//*boolean asc = true;
        if(col.getSortType().equals(TableColumn.SortType.DESCENDING)) asc = false;
        System.out.println("o.getClass().getName() = " + o.getClass().getName());
        doInBackground(new LoadPageDataTaskCreator(0 * getItemsPerPageCount(), getItemsPerPageCount(), col.getText().toLowerCase(), getFullInfo(), true, asc, null, null));*//*
      }
  }*/
}
