package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.control.DeleteButton;
import be.virtualsushi.jfx.dorse.control.DetailsButton;
import be.virtualsushi.jfx.dorse.control.EditButton;
import be.virtualsushi.jfx.dorse.control.table.EntityPropertyTableColumn;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractListActivity<E extends BaseEntity> extends DorseUiActivity<BorderPane> {

	public static final String FORCE_RELOAD_PARAMETER = "force_reload";

	private class LoadPageDataTaskCreator implements TaskCreator<DorseBackgroundTask<List<E>>> {

		private final Integer from;
		private final Integer quantity;

		public LoadPageDataTaskCreator(Integer from, Integer quantity) {
			this.from = from;
			this.quantity = quantity;
		}

		@Override
		public DorseBackgroundTask<List<E>> createTask() {
			return new DorseBackgroundTask<List<E>>(this, from, quantity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected List<E> call() throws Exception {
					doCustomBackgroundInitialization();
					return getRestApiAccessor().getList((Integer) getParameters()[0], (Integer) getParameters()[1],
							(Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0], true);
				}

				@Override
				protected void onSuccess(List<E> value) {
					createPage(value);
					hideLoadingMask();
				}
			};
		}
	}

	private class ActionsBar extends HBox {

		public ActionsBar(final Long entityId) {
			super();
			setSpacing(3);
			EditButton editButton = new EditButton();
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getEditActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, entityId);
				}
			});
			editButton.setTooltip(new Tooltip(getResources().getString("edit")));

			DetailsButton detailsButton = new DetailsButton();
			detailsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getViewActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, entityId);
				}
			});
			detailsButton.setTooltip(new Tooltip(getResources().getString("details")));

			DeleteButton deleteButton = new DeleteButton();
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

				}
			});
			deleteButton.setTooltip(new Tooltip(getResources().getString("delete")));

			getChildren().add(editButton);
			getChildren().add(detailsButton);
			getChildren().add(deleteButton);
		}

	}

	@FXML
	public Pagination listPage;

	@FXML
	public Label listTitle;

	private BorderPane listContainer;

	@Override
	public void initialize() {
		super.initialize();
		listContainer = new BorderPane();
		listContainer.setPadding(new Insets(3));
		listContainer.setPrefHeight(Double.MAX_VALUE);
		listContainer.setPrefWidth(Double.MAX_VALUE);
		listTitle.setText(getTitle());
		listPage.setPageFactory(new Callback<Integer, Node>() {

			@Override
			public Node call(Integer param) {
				listContainer.getChildren().clear();
				doInBackground(new LoadPageDataTaskCreator(param * getItemsPerPageCount(), getItemsPerPageCount()));
				return listContainer;
			}
		});
	}

	@Override
	protected void started() {
		super.started();

		if (getParameter(FORCE_RELOAD_PARAMETER, Boolean.class, false)) {
			listPage.setCurrentPageIndex(0);
		}
	}

	private void createPage(List<E> data) {
		TableView<E> table = new TableView<E>();
		table.setMaxHeight(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
		fillTableColumns(table);
		table.setItems(FXCollections.observableArrayList(data));
		listContainer.setCenter(table);
	}

	@FXML
	public void handleAddAction(ActionEvent event) {
		goTo(getEditActivityName());
	}

	protected Integer getItemsPerPageCount() {
		return 25;
	}

	protected TableColumn<E, Long> createActionsColumn() {
		TableColumn<E, Long> result = new TableColumn<E, Long>(getResources().getString("actions"));
		result.setCellValueFactory(new PropertyValueFactory<E, Long>("id"));
		result.setCellFactory(new Callback<TableColumn<E, Long>, TableCell<E, Long>>() {

			@Override
			public TableCell<E, Long> call(TableColumn<E, Long> param) {
				return createActionsCell();
			}
		});
		result.setPrefWidth(120);
		return result;
	}

	private TableCell<E, Long> createActionsCell() {
		return new TableCell<E, Long>() {
			@Override
			protected void updateItem(Long item, boolean empty) {
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

	protected <T> EntityPropertyTableColumn<E, T> createTableColumn(String propertyName) {
		return new EntityPropertyTableColumn<E, T>(getResources(), propertyName);
	}

	protected <T> EntityPropertyTableColumn<E, T> createTableColumn(String titleKey, String propertyName) {
		return new EntityPropertyTableColumn<E, T>(getResources().getString(titleKey), propertyName);
	}

	protected <T> EntityPropertyTableColumn<E, T> createTableColumn(String titleKey, Callback<CellDataFeatures<E, T>, ObservableValue<T>> valueFactory) {
		return new EntityPropertyTableColumn<E, T>(getResources().getString(titleKey), valueFactory);
	}

	protected abstract String getTitle();

	protected abstract void fillTableColumns(TableView<E> table);

	protected abstract void doCustomBackgroundInitialization();

	protected abstract AppActivitiesNames getViewActivityName();

	protected abstract AppActivitiesNames getEditActivityName();

}