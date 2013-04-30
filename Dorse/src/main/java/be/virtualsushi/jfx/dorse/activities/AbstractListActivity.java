package be.virtualsushi.jfx.dorse.activities;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.*;
import be.virtualsushi.jfx.dorse.dialogs.InvoiceFilterDialog;
import be.virtualsushi.jfx.dorse.events.CancelFilterEvent;
import be.virtualsushi.jfx.dorse.events.ClearFilterEvent;
import be.virtualsushi.jfx.dorse.events.FilterEvent;
import be.virtualsushi.jfx.dorse.export.ExportService;
import be.virtualsushi.jfx.dorse.export.ExportServiceException;
import be.virtualsushi.jfx.dorse.model.ServerResponse;
import be.virtualsushi.jfx.dorse.navigation.ActivityNavigator;
import com.google.common.eventbus.Subscribe;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import name.antonsmirnov.javafx.dialog.Dialog;
import org.springframework.beans.factory.annotation.Autowired;

import be.virtualsushi.jfx.dorse.control.table.EntityPropertyTableColumn;
import be.virtualsushi.jfx.dorse.model.BaseEntity;
import be.virtualsushi.jfx.dorse.navigation.AppActivitiesNames;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

public abstract class AbstractListActivity<E extends BaseEntity> extends DorseUiActivity<BorderPane> {

  public static final String FORCE_RELOAD_PARAMETER = "force_reload";
  private Integer pagerPage = 1;
  protected TableView<E> table;
  //protected List<? extends BaseEntity> dependingList;

  @Autowired
  protected ActivityNavigator activityNavigator;

  protected class LoadPageDataTaskCreator implements TaskCreator<DorseBackgroundTask<ServerResponse>> {

    private final Integer from;
    private final Integer quantity;
    private final String orderOn;
    private final Boolean fullInfo;
    private final Boolean includesNonActive;
    private final String additionalCondition;
    private final BaseEntity filter;
    private final Boolean asc;

    public LoadPageDataTaskCreator(Integer from, Integer quantity, String orderOn, boolean fullInfo, boolean includesNonActive, boolean asc, String additionalCondition, BaseEntity filter) {
      this.from = from;
      this.quantity = quantity;
      this.orderOn = orderOn;
      this.fullInfo = fullInfo;
      this.additionalCondition = additionalCondition;
      this.includesNonActive = includesNonActive;
      this.filter = filter;
      this.asc = asc;
    }

    public LoadPageDataTaskCreator(Integer from, Integer quantity, String orderOn, Boolean fullInfo, boolean includesNonActive, String additionalCondition, BaseEntity filter) {
      this.from = from;
      this.quantity = quantity;
      this.orderOn = orderOn;
      this.fullInfo = fullInfo;
      this.additionalCondition = additionalCondition;
      this.includesNonActive = includesNonActive;
      this.filter = filter;
      this.asc = true;
    }

    /*public LoadPageDataTaskCreator(Integer from, Integer quantity, String orderOn, Boolean fullInfo) {
      this.from = from;
      this.quantity = quantity;
      this.orderOn = orderOn;
      this.fullInfo = fullInfo;
      this.additionalCondition = null;
      this.includesNonActive = null;
      this.filter = null;
      this.asc = true;
    }*/

    public LoadPageDataTaskCreator(Integer from, Integer quantity, String orderOn, Boolean fullInfo, Boolean asc, BaseEntity filter) {
      this.from = from;
      this.quantity = quantity;
      this.orderOn = orderOn;
      this.fullInfo = fullInfo;
      this.additionalCondition = null;
      this.includesNonActive = null;
      this.filter = filter;
      this.asc = asc;
    }

    @Override
    public DorseBackgroundTask<ServerResponse> createTask() {
       return new DorseBackgroundTask<ServerResponse>(this, filter, from, quantity, orderOn, additionalCondition, fullInfo, includesNonActive, asc) {

        @Override
        protected void onPreExecute() {
          showLoadingMask();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ServerResponse call() throws Exception {
          try {
            doCustomBackgroundInitialization();
            //setDependingList();
            return getRestApiAccessor().getResponse(
                (Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0],
                getBaseEntity(0), getInteger(1), getInteger(2), getString(3), getString(4),
                getBoolean(5, true), getBoolean(6, false), getBoolean(7, true));
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        protected void onSuccess(ServerResponse value) {
          if (value != null) {
            listContainer.setCenter(createPage(value, getString(3), getBoolean(7, true)));
            listPage.setPageCount((int) Math.ceil(value.getMetaData().getCount() / (double) getItemsPerPageCount()));
          }
          hideLoadingMask();
        }
      };
    }
  }

  private class ExportTaskCreator implements TaskCreator<DorseBackgroundTask<ServerResponse>> {

    private final BaseEntity filter;
    private final File target;

    public ExportTaskCreator(BaseEntity filter, File target) {
      this.filter = filter;
      this.target = target;
    }

    @Override
    public DorseBackgroundTask<ServerResponse> createTask() {
      return new DorseBackgroundTask<ServerResponse>(this, filter) {

        @Override
        protected void onPreExecute() {
          showLoadingMask();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected ServerResponse call() throws Exception {
          try {
            doCustomBackgroundInitialization();
            return getRestApiAccessor().getResponse(filter);
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        protected void onSuccess(ServerResponse value) {
          //exportService.createCsv(value, target);
          createCsv(value, target);
          hideLoadingMask();
        }

        @Override
        protected void onError(Throwable exception) {
          super.onError(exception);
          exception.printStackTrace();
          hideLoadingMask();
        }
      };
    }
  }

  protected class DeleteEntityTaskCreator implements TaskCreator<DorseBackgroundTask<E>> {

    private final E entity;

    public DeleteEntityTaskCreator(E entityToDelete) {
      this.entity = entityToDelete;
    }

    @Override
    public DorseBackgroundTask<E> createTask() {
      return new DorseBackgroundTask<E>(this, entity) {

        @Override
        protected void onPreExecute() {
          showLoadingMask();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected E call() throws Exception {
          getRestApiAccessor().delete((E) getParameters()[0]);
          /*return getRestApiAccessor().getResponse((Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0],
              null, 0, getItemsPerPageCount(), ORDER_ON, null, true, false, true);*/
          return (E) getParameters()[0];
        }

        @Override
        protected void onSuccess(E value) {
          //listPage.setCurrentPageIndex(0);
          //createPage(value);
          table.getItems().remove(value);
          hideLoadingMask();
        }
      };
    }

  }

  protected class ActionsBar extends HBox {

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
          Button btn = (Button) event.getSource();
          Dialog.buildConfirmation(getResources().getString("delete.dialog.title"), getResources().getString("delete.dialog.message"))
              .addYesButton(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                  doInBackground(new DeleteEntityTaskCreator(entity));
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

      getChildren().add(editButton);
      getChildren().add(detailsButton);
      getChildren().add(deleteButton);
    }

  }

  protected BaseEntity filter = null;

  @Autowired
 	protected ExportService exportService;

  @FXML
  public Pagination listPage;

  @FXML
  public Label listTitle;

  @FXML
  public AddButton addR;

  @FXML
  public FilterButton filtR;

  @FXML
  public ExportButton exportR;

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
        doInBackground(new LoadPageDataTaskCreator(param * getItemsPerPageCount(), getItemsPerPageCount(), getDefaultOrder(), getFullInfo(), getDefaultAsc(), filter));
        setPagerPage(param);
        return listContainer;
      }
    });
    if(addR!=null){
      Tooltip add = new Tooltip();
      add.setText("Nieuw");
      addR.setTooltip(add);
    }
    if(filtR!=null){
      Tooltip filter = new Tooltip();
      filter.setText("Filter");
      filtR.setTooltip(filter);
    }
    if(exportR!=null){
      Tooltip export = new Tooltip();
      export.setText("Export");
      exportR.setTooltip(export);
    }
  }

  @Override
  protected void started() {
    super.started();

    if (getParameter(FORCE_RELOAD_PARAMETER, Boolean.class, false)) {
      listPage.setCurrentPageIndex(0);
      doInBackground(new LoadPageDataTaskCreator(0 * getItemsPerPageCount(), getItemsPerPageCount(), getDefaultOrder(), getFullInfo(), getDefaultAsc(), filter));
    }
  }

  @FXML
  public void handleAddAction(ActionEvent event) {
    goTo(getEditActivityName());
  }

  @FXML
  protected void handleLaunchFilter(ActionEvent event) {
    //show the corresponding filter dialog and possible add some lists to it as parameter
  }

  @FXML
  protected void handleLaunchExport(ActionEvent event) {
    //launch the export
    FileChooser fileChooser = new FileChooser();

    //Set extension filter
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.txt, *.csv)", "*.txt", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    //Show save file dialog

    File file = fileChooser.showSaveDialog(this.getView().toNode().getScene().getWindow());

    if (file != null) {
      System.out.println("file = " + file);
      if(filter!=null)
        doInBackground(new ExportTaskCreator(filter, file));
      else
        try {
          doInBackground(new ExportTaskCreator(((Class<E>) ((ParameterizedType) AbstractListActivity.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance(), file));
        } catch (InstantiationException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          hideFilterDialog();
        } catch (IllegalAccessException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          hideFilterDialog();
        }

    }
  }

  @Subscribe
  public void onFilter(FilterEvent event) {
    filter = event.getEntity();
    doInBackground(new LoadPageDataTaskCreator(0, getItemsPerPageCount(), getDefaultOrder(), getFullInfo(), getDefaultAsc(), filter));
    hideFilterDialog();
  }

  @Subscribe
  public void onClearFilter(ClearFilterEvent event) {
    System.out.println("event = " + event);
    filter = null;
    doInBackground(new LoadPageDataTaskCreator(0, getItemsPerPageCount(), getDefaultOrder(), getFullInfo(), getDefaultAsc(), filter));
    hideFilterDialog();
  }

  @Subscribe
  public void onCancelFilter(CancelFilterEvent event) {
    hideFilterDialog();
  }

  protected Integer getItemsPerPageCount() {
    return 20;
  }

  protected TableColumn<E, E> createActionsColumn() {
    TableColumn<E, E> result = new TableColumn<E, E>(getResources().getString("actions"));
    result.setSortable(false);
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

  protected TableCell<E, E> createActionsCell() {
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

  protected abstract void fillTableColumns(TableView<E> table, String columnName, boolean asc);

  protected abstract void doCustomBackgroundInitialization();

  protected abstract AppActivitiesNames getViewActivityName();

  protected abstract AppActivitiesNames getEditActivityName();

  protected abstract Boolean getFullInfo();

  protected abstract TableView createPage(ServerResponse serverResponse);

  protected abstract TableView createPage(ServerResponse serverResponse, String columnName, boolean asc);

  protected abstract String createCsv(ServerResponse serverResponse, File target);

  protected abstract String getDefaultOrder();

  protected abstract Boolean getDefaultAsc();


  /*private void createPage(ServerResponse serverResponse) {
      TableView<? extends BaseEntity> table = new TableView<? extends BaseEntity>();
      table.setMaxHeight(Double.MAX_VALUE);
      table.setMaxWidth(Double.MAX_VALUE);
      fillTableColumns(table);
      table.setItems(FXCollections.observableArrayList(serverResponse.getData()));
      listContainer.setCenter(table);
    }*/

  public Integer getPagerPage() {
    return pagerPage;
  }

  public void setPagerPage(Integer pagerPage) {
    this.pagerPage = pagerPage;
  }
}