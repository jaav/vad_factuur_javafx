package be.virtualsushi.jfx.dorse.activities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.DeleteButton;
import be.virtualsushi.jfx.dorse.control.DetailsButton;
import be.virtualsushi.jfx.dorse.control.EditButton;
import be.virtualsushi.jfx.dorse.control.StatsButton;
import be.virtualsushi.jfx.dorse.control.table.EntityPropertyValueFactory;
import be.virtualsushi.jfx.dorse.dialogs.ArticleFilterDialog;
import be.virtualsushi.jfx.dorse.events.CancelFilterEvent;
import be.virtualsushi.jfx.dorse.events.CancelStatsEvent;
import be.virtualsushi.jfx.dorse.events.StatsEvent;
import be.virtualsushi.jfx.dorse.model.*;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import com.google.common.eventbus.Subscribe;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import name.antonsmirnov.javafx.dialog.Dialog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.fxml.FxmlFile;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;

@Component
@Scope("prototype")
@FxmlFile("ListActivity.fxml")
public class ArticleListActivity extends AbstractListActivity<Article> {

	private List<Supplier> suppliers;

	private void checkSorting(String columnName, TableColumn<Article, ?> aColumn, String columnToSort, boolean asc) {
		if (columnName.equalsIgnoreCase(columnToSort)) {
			if (asc)
				aColumn.setSortType(TableColumn.SortType.ASCENDING);
			else
				aColumn.setSortType(TableColumn.SortType.DESCENDING);
			table.getSortOrder().add(aColumn);
		}
	}

	@Override
	@FXML
	protected void handleLaunchFilter(ActionEvent event) {
		showFilterDialog(getResources().getString("article.filter.dialog.title"), ArticleFilterDialog.class);
	}

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
				if (value.getStock() != null) property.set("" + value.getStock().getQuantity());
			}
		});
		stockColumn.setMinWidth(30);
		table.getColumns().addAll(idColumn, codeColumn, nameColumn, priceColumn, freeQColumn, supplierColumn, stockColumn, createActionsColumn());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization() {
		if (CollectionUtils.isEmpty(suppliers)) {
			suppliers = (List<Supplier>) getRestApiAccessor().getResponse(Supplier.class, false).getData();
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


		if (serverResponse != null && serverResponse.getData() != null)
			table.setItems(FXCollections.observableArrayList((List<Article>) serverResponse.getData()));
		return table;
	}

	@Override
	protected String createCsv(ServerResponse serverResponse, File target) {
		exportService.createCsv((ArticleResponse) serverResponse, target, suppliers);
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getDefaultOrder() {
		return Article.DEFAULT_COLUMN;
	}

	@Override
	protected Boolean getDefaultAsc() {
		return Article.DEFAULT_ASC;
	}

	@Subscribe
	public void onStats(StatsEvent event) {
		doInBackground(new CreateStatsTaskCreator(event.getEntity()));
	}

	@Subscribe
	public void onCancelStats(CancelStatsEvent event) {
		hideStatsDialog();
	}

	/*@FXML
	protected void handleLaunchStats(ActionEvent event) {
		showStatsDialog(getResources().getString("customer.filter.dialog.title"));
	}*/

	protected class CreateStatsTaskCreator implements TaskCreator<DorseBackgroundTask<ResponseEntity>> {

		private final StatsDTO statsDTO;

		public CreateStatsTaskCreator(StatsDTO statsDTO) {
			this.statsDTO = statsDTO;
		}

		@Override
		public DorseBackgroundTask<ResponseEntity> createTask() {
			return new DorseBackgroundTask<ResponseEntity>(this, statsDTO) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected ResponseEntity<StatsResponse> call() throws Exception {
					try {
						return getRestApiAccessor().getStats(statsDTO);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				protected void onSuccess(ResponseEntity value) {
					if (value != null) {
						List<Stat> result = ((ResponseEntity<StatsResponse>) value).getBody().getData();

						//launch the export
						FileChooser fileChooser = new FileChooser();

						//Set extension filter
						FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.txt, *.csv)", "*.txt", "*.csv");
						fileChooser.getExtensionFilters().add(extFilter);

						//Show save file dialog

						File file = fileChooser.showSaveDialog(getView().toNode().getScene().getWindow());
						try {
							BufferedWriter bf = new BufferedWriter(new FileWriter(file));
							bf.write("Date\tInvoice\tArticle\tCode\tPrice\tQuantity\tDiscount\tTotal\tCustomer");
							bf.newLine();
							for (Stat stat : result) {
								bf.write(stat.getDate() + "\t" + stat.getInvoice()
										+ "\t" + stat.getArticle() + "\t" + stat.getCode()
										+ "\t" + stat.getPrice() + "\t" + stat.getQuantity()
										+ "\t" + stat.getDiscount() + "\t" + stat.getTotal() + "\t" + stat.getCustomer());
								bf.newLine();
							}
							bf.flush();
							bf.close();
						} catch (IOException e) {
							e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
					}
					hideLoadingMask();
					hideStatsDialog();
				}
			};
		}
	}

	@Override
	protected TableColumn<Article, Article> createActionsColumn() {
   TableColumn<Article, Article> result = new TableColumn<Article, Article>(getResources().getString("actions"));
   result.setSortable(false);
   result.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Article, Article>, ObservableValue<Article>>() {

     @Override
     public ObservableValue<Article> call(TableColumn.CellDataFeatures<Article, Article> param) {
       return new SimpleObjectProperty<Article>(param.getValue());
     }
   });
   result.setCellFactory(new Callback<TableColumn<Article, Article>, TableCell<Article, Article>>() {

     @Override
     public TableCell<Article, Article> call(TableColumn<Article, Article> param) {
       return createActionsCell();
     }
   });
   result.setPrefWidth(160);
   return result;
 }

	@Override
	protected TableCell<Article, Article> createActionsCell() {
   return new TableCell<Article, Article>() {
     @Override
     protected void updateItem(Article item, boolean empty) {
       super.updateItem(item, empty);
       if (empty) {
         setText(null);
         setGraphic(null);
       } else {
         setGraphic(new ActionsBar(item));
         setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       }
     }
   };
 }

	protected class ActionsBar extends HBox {

		public ActionsBar(final Article article) {
			super();
			setSpacing(3);
			EditButton editButton = new EditButton();
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getEditActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, article.getId());
				}
			});
			editButton.setTooltip(new Tooltip(getResources().getString("edit")));

			DetailsButton detailsButton = new DetailsButton();
			detailsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getViewActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, article.getId());
				}
			});
			detailsButton.setTooltip(new Tooltip(getResources().getString("details")));

			DeleteButton deleteButton = new DeleteButton();
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Button btn = (Button) event.getSource();
					Dialog.buildConfirmation(getResources().getString("delete.dialog.title"), getResources().getString("delete.dialog.message"))
							.addYesButton(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									doInBackground(new DeleteEntityTaskCreator(article));
								}
							})
							.addCancelButton(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									//To change body of implemented methods use File | Settings | File Templates.
								}
							})
							.build()
							.show();

				}
			});
			deleteButton.setTooltip(new Tooltip(getResources().getString("delete")));

			StatsButton statsButton = new StatsButton();
			statsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					showStatsDialog(getResources().getString("stats.dialog.title"), article);
				}
			});
			statsButton.setTooltip(new Tooltip(getResources().getString("stats")));

			getChildren().add(editButton);
			getChildren().add(detailsButton);
			getChildren().add(deleteButton);
			getChildren().add(statsButton);
		}

	}
}
