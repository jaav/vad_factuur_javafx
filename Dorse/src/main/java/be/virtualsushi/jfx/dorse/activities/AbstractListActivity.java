package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ResourceBundle;

import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.control.DeleteButton;
import be.virtualsushi.jfx.dorse.control.DetailsButton;
import be.virtualsushi.jfx.dorse.control.EditButton;
import be.virtualsushi.jfx.dorse.control.table.EntityPropertyTableColumn;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractListActivity<E extends BaseEntity> extends DorseUiActivity<BorderPane> {

	public static final String FORCE_RELOAD_PARAMETER = "force_reload";
	public static String ORDER_ON = "id";

	@Autowired
	protected ActivityNavigator activityNavigator;

	private class LoadPageDataTaskCreator implements TaskCreator<DorseBackgroundTask<List<E>>> {

		private final Integer from;
		private final Integer quantity;
		private final String orderOn;
    private final Boolean fullInfo;

		public LoadPageDataTaskCreator(Integer from, Integer quantity, String orderOn, Boolean fullInfo) {
			this.from = from;
			this.quantity = quantity;
			this.orderOn = orderOn;
      this.fullInfo = fullInfo;
		}

		@Override
		public DorseBackgroundTask<List<E>> createTask() {
			return new DorseBackgroundTask<List<E>>(this, from, quantity, orderOn, fullInfo) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected List<E> call() throws Exception {
					doCustomBackgroundInitialization();
					return getRestApiAccessor().getList((Integer) getParameters()[0], (Integer) getParameters()[1], (String) getParameters()[2],
              (Boolean) getParameters()[3], (Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
				}

				@Override
				protected void onSuccess(List<E> value) {
					createPage(value);
					hideLoadingMask();
				}
			};
		}
	}

	private class DeleteEntityTaskCreator implements TaskCreator<DorseBackgroundTask<List<E>>> {

		private final E entity;

		public DeleteEntityTaskCreator(E entityToDelete) {
			this.entity = entityToDelete;
		}

		@Override
		public DorseBackgroundTask<List<E>> createTask() {
			return new DorseBackgroundTask<List<E>>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected List<E> call() throws Exception {
					getRestApiAccessor().delete((E) getParameters()[0]);
					return getRestApiAccessor().getList(0, getItemsPerPageCount(), ORDER_ON,
							true, (Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
				}

				@Override
				protected void onSuccess(List<E> value) {
					listPage.setCurrentPageIndex(0);
					createPage(value);
					hideLoadingMask();
				}
			};
		}

	}

	private class ActionsBar extends HBox {

		public ActionsBar(final E entity) {
			super();
			setSpacing(3);
			EditButton editButton = new EditButton();
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getEditActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, entity.getId());
				}
			});
			editButton.setTooltip(new Tooltip(getResources().getString("edit")));

			DetailsButton detailsButton = new DetailsButton();
			detailsButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getActivityNavigator().goTo(getViewActivityName(), AbstractManageEntityActivity.ENTITY_ID_PARAMETER, entity.getId());
				}
			});
			detailsButton.setTooltip(new Tooltip(getResources().getString("details")));

			DeleteButton deleteButton = new DeleteButton();
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					doInBackground(new DeleteEntityTaskCreator(entity));
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
				doInBackground(new LoadPageDataTaskCreator(param * getItemsPerPageCount(), getItemsPerPageCount(), ORDER_ON, getFullInfo()));
				return listContainer;
			}
		});
	}

	@Override
	protected void started() {
		super.started();

		if (getParameter(FORCE_RELOAD_PARAMETER, Boolean.class, false)) {
			listPage.setCurrentPageIndex(0);
			doInBackground(new LoadPageDataTaskCreator(0 * getItemsPerPageCount(), getItemsPerPageCount(), ORDER_ON, getFullInfo()));
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
		return 20;
	}

	protected TableColumn<E, E> createActionsColumn() {
		TableColumn<E, E> result = new TableColumn<E, E>(getResources().getString("actions"));
		result.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<E, E>, ObservableValue<E>>() {

			@Override
			public ObservableValue<E> call(CellDataFeatures<E, E> param) {
				return new SimpleObjectProperty<E>(param.getValue());
			}
		});
		result.setCellFactory(new Callback<TableColumn<E, E>, TableCell<E, E>>() {

			@Override
			public TableCell<E, E> call(TableColumn<E, E> param) {
				return createActionsCell();
			}
		});
		result.setPrefWidth(120);
		return result;
	}

	private TableCell<E, E> createActionsCell() {
		return new TableCell<E, E>() {
			@Override
			protected void updateItem(E item, boolean empty) {
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

	protected <T> EntityPropertyTableColumn<E, T> createNoHeaderTableColumn(String propertyName) {
		return new EntityPropertyTableColumn<E, T>("", propertyName);
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

	protected String getName(String key) {
		return getResources().getString(key);
	}

	protected abstract String getTitle();

	protected abstract void fillTableColumns(TableView<E> table);

	protected abstract void doCustomBackgroundInitialization();

	protected abstract AppActivitiesNames getViewActivityName();

	protected abstract AppActivitiesNames getEditActivityName();

  protected abstract Boolean getFullInfo();

}