package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractListActivity<E extends BaseEntity> extends DorseUiActivity<BorderPane> {

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

	@FXML
	public Pagination listPage;

	@FXML
	public Label listTitle;

	private Pane listContainer;

	@Override
	public void initialize() {
		super.initialize();
		listContainer = new Pane();
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

	private void createPage(List<E> data) {
		TableView<E> table = new TableView<E>();
		table.setMaxHeight(Double.MAX_VALUE);
		table.setMaxWidth(Double.MAX_VALUE);
		fillTableColumns(table);
		table.setItems(FXCollections.observableArrayList(data));
		listContainer.getChildren().add(table);
	}

	@FXML
	public void handleAddAction(ActionEvent event) {

	}

	protected Integer getItemsPerPageCount() {
		return 25;
	}

	protected abstract String getTitle();

	protected abstract void fillTableColumns(TableView<E> table);

	protected abstract void doCustomBackgroundInitialization();

}
