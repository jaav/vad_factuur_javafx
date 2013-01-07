package be.virtualsushi.jfx.dorse.activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import be.virtualsushi.jfx.dorse.control.table.MyPropertyValueFactory;
import be.virtualsushi.jfx.dorse.control.table.MyRelatedPropertyValueFactory;
import be.virtualsushi.jfx.dorse.dialogs.EditInvoicePricesDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.ChangeInvoicePricesEvent;
import be.virtualsushi.jfx.dorse.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import be.virtualsushi.jfx.dorse.control.DeleteButton;
import be.virtualsushi.jfx.dorse.control.EditButton;
import be.virtualsushi.jfx.dorse.control.ViewAddressControl;
import be.virtualsushi.jfx.dorse.control.table.EntityPropertyValueFactory;
import be.virtualsushi.jfx.dorse.control.table.EntityStringPropertyValueFactory;
import be.virtualsushi.jfx.dorse.dialogs.EditOrderLineDialog;
import be.virtualsushi.jfx.dorse.events.dialogs.SaveOrderLineEvent;
import be.virtualsushi.jfx.dorse.events.report.ShowReportEvent;
import be.virtualsushi.jfx.dorse.report.ReportService;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class ViewInvoiceActivity extends AbstractViewEntityActivity<VBox, Invoice> {
  private static final Logger log = LoggerFactory.getLogger(ViewInvoiceActivity.class);
  public static int PRINT_INVOICE = 0;
  public static int PRINT_REMINDER = 2;

	private class ActionsBar extends HBox {

		public ActionsBar(final Long itemId) {
			super();
			setSpacing(3);

			EditButton editButton = new EditButton();
			editButton.setTooltip(new Tooltip(getResources().getString("edit")));
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					showDialog(getResources().getString("edit.order.line.dialog.title"), EditOrderLineDialog.class, articles, units, orderLines.get(findOrderLineIndex(itemId)));
				}
			});

			DeleteButton deleteButton = new DeleteButton();
			deleteButton.setTooltip(new Tooltip(getResources().getString("delete")));
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					doInBackground(new DeleteOrderLineTaskCreator(orderLines.get(findOrderLineIndex(itemId))));
				}
			});
			getChildren().add(editButton);
			getChildren().add(deleteButton);
		}

	}

	private class SaveOrderLineTaskCreator implements TaskCreator<DorseBackgroundTask<OrderLineProperty>> {

		private final OrderLineProperty orderLine;

		public SaveOrderLineTaskCreator(OrderLineProperty orderLine) {
			this.orderLine = orderLine;
		}

    public SaveOrderLineTaskCreator(OrderLine orderLine) {
  			this.orderLine = new OrderLineProperty((orderLine));
  		}

		@Override
		public DorseBackgroundTask<OrderLineProperty> createTask() {
			return new DorseBackgroundTask<OrderLineProperty>(this, orderLine) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected OrderLineProperty call() throws Exception {
          OrderLineProperty lineProp = (OrderLineProperty) getParameters()[0];
					Long id = getRestApiAccessor().save(new OrderLine(lineProp));
          lineProp.setId(id);
          return lineProp;
				}

				@Override
				protected void onSuccess(OrderLineProperty value) {
          if(!hasOrderLine(value)){
            orderLines.add(value);
            orderLineTable.getItems().add(value);
          }
          updateInvoiceData();
          //hideLoadingMask();
				}

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          hideLoadingMask();
        }
      };
		}

	}

	private class DeleteOrderLineTaskCreator implements TaskCreator<DorseBackgroundTask<OrderLineProperty>> {

		private final OrderLineProperty entity;

		public DeleteOrderLineTaskCreator(OrderLineProperty orderLine) {
			this.entity = orderLine;
		}

		@Override
		public DorseBackgroundTask<OrderLineProperty> createTask() {
			return new DorseBackgroundTask<OrderLineProperty>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected OrderLineProperty call() throws Exception {
          OrderLine l = new OrderLine((OrderLineProperty) getParameters()[0]);
					getRestApiAccessor().delete(new OrderLine((OrderLineProperty) getParameters()[0]));
          //return getRestApiAccessor().getList(OrderLineProperty.class, null, null, "id", "invoice="+getEntity().getId(), true, false);
          return (OrderLineProperty) getParameters()[0];
				}

				@Override
				protected void onSuccess(OrderLineProperty value) {
          orderLineTable.getItems().remove(value);
          orderLines.remove(value);
          updateInvoiceData();
					//hideLoadingMask();
				}

        @Override
        protected void onError(Throwable exception) {
          exception.printStackTrace();
          hideLoadingMask();
        }
      };
		}
	}

	private class GenerateReportTaskCreator implements TaskCreator<DorseBackgroundTask<String>> {

		private final Invoice invoice;
		private final Address invoiceAddress;
		private final Address deliveryAddress;
		private final List<Article> articles;
		private final List<OrderLineProperty> orderLines;
    private final int reportType;

		public GenerateReportTaskCreator(int reportType, Invoice invoice, Address invoiceAddress, Address deliveryAddress, List<Article> articles, List<OrderLineProperty> orderLines) {
			this.invoice = invoice;
			this.invoiceAddress = invoiceAddress;
			this.deliveryAddress = deliveryAddress;
			this.articles = articles;
			this.orderLines = orderLines;
      this.reportType = reportType;
		}

		@Override
		public DorseBackgroundTask<String> createTask() {
			return new DorseBackgroundTask<String>(this, reportType, invoice, invoiceAddress, deliveryAddress, orderLines) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected String call() throws Exception {
					return reportService.createInvoiceReport((Integer)getParameters()[0], (Invoice) getParameters()[1], (Address) getParameters()[2], (Address) getParameters()[3],
							(List<OrderLineProperty>) getParameters()[4]);
				}

				@Override
				protected void onSuccess(String value) {
					hideLoadingMask();
					getEventBus().post(new ShowReportEvent(value));
				}

        @Override
        protected void onError(Throwable exception) {
          log.warn(exception.getMessage());
          hideLoadingMask();
          super.onError(exception);    //To change body of overridden methods use File | Settings | File Templates.
        }
      };
		}

	}

  /*private class GenerateLabelTaskCreator implements TaskCreator<DorseBackgroundTask<String>> {

 		private final Long invoiceId;
    private final Integer addressType;

 		public GenerateLabelTaskCreator(Long invoiceId, Integer type) {
       this.invoiceId = invoiceId;
       this.addressType = type;
 		}

 		@Override
 		public DorseBackgroundTask<String> createTask() {
 			return new DorseBackgroundTask<String>(this, invoiceId, addressType) {

 				@Override
 				protected void onPreExecute() {
 					showLoadingMask();
 				}

 				@SuppressWarnings("unchecked")
 				@Override
 				protected String call() throws Exception {
           return reportService.createAddressLabel((Long) getParameters()[0], (Integer)getParameters()[1]);
 				}

 				@Override
 				protected void onSuccess(String value) {
 					hideLoadingMask();
 					getEventBus().post(new ShowReportEvent(value));
 				}

         @Override
         protected void onError(Throwable exception) {
           log.warn(exception.getMessage());
           hideLoadingMask();
           super.onError(exception);    //To change body of overridden methods use File | Settings | File Templates.
         }
       };
 		}

 	}*/
  
  private class LoadInvoiceTaskCreator implements TaskCreator<DorseBackgroundTask<Invoice>> {
 
 		public LoadInvoiceTaskCreator() {
 		}
 
 		@Override
 		public DorseBackgroundTask<Invoice> createTask() {
 			return new DorseBackgroundTask<Invoice>(this) {

         @Override
 				protected void onPreExecute() {
 					showLoadingMask();
 				};
 
 				@SuppressWarnings("unchecked")
 				@Override
 				protected Invoice call() throws Exception {
 					Invoice result = getRestApiAccessor().get(getEntity().getId(), Invoice.class);
          return result;
 				}

         @Override
 				protected void onSuccess(Invoice value) {
           mapFields(value);
           setEntity(value);
 					hideLoadingMask();
 				}
 
         @Override
         protected void onError(Throwable exception) {
           hideLoadingMask();
         }
 
 			};
 		}
 
 	}
  
  

  private class SaveInvoiceTaskCreator implements TaskCreator<DorseBackgroundTask<Invoice>> {
 
 		private final Invoice entity;
 
 		public SaveInvoiceTaskCreator(Invoice entityToSave) {
 			this.entity = entityToSave;
 		}
 
 		@Override
 		public DorseBackgroundTask<Invoice> createTask() {
 			return new DorseBackgroundTask<Invoice>(this, entity) {
 
 				@Override
 				protected void onPreExecute() {
 					showLoadingMask();
 				}
 
 				@Override
 				protected Invoice call() throws Exception {
 					getRestApiAccessor().save(entity);
 					return entity;
 				}
 
 				@Override
 				protected void onSuccess(Invoice value) {
          updatePriceFields(value);
 					hideLoadingMask();
 				}
 
         @Override
         protected void onError(Throwable exception) {
           exception.printStackTrace();
           ValidationErrorPanel validationPanel = getValidationPanel();
        			if (validationPanel != null) {
        				validationPanel.clearMessages();
               validationPanel.addMessage(getResources().getString("save_error")+"\n\n"+exception.getMessage());
        				validationPanel.setVisible(true);
              }
           hideLoadingMask();
         }
       };
 		}
 
 	}

	@Autowired
	private ReportService reportService;

	@FXML
	private Label idField, customerField, createdField, shippingField, productsField, totalField;

	@FXML
	private ViewAddressControl invoiceAddressField, deliveryAddressField;

	@FXML
	private TableView<OrderLineProperty> orderLineTable;

	@FXML
	private TableColumn<OrderLineProperty, Long> idColumn, actionsColumn;

	@FXML
	private TableColumn<OrderLineProperty, String> codeColumn, articleNameColumn;

	@FXML
	private TableColumn<OrderLineProperty, Float> priceColumn, discountColumn, lineTotalColumn;

	@FXML
	private TableColumn<OrderLineProperty, Integer> quantityColumn, freeQColumn;

	private Address invoiceAddressValue, deliveryAddressValue;

	private List<OrderLineProperty> orderLines;

	private List<Article> articles;

	private List<Unit> units;

	@FXML
	protected void handleAddOrderLine(ActionEvent event) {
    OrderLineProperty line  = new OrderLineProperty();
    line.setOrderId(Long.parseLong(idField.getText()));
    line.setApplyFree(true);
		showDialog(getResources().getString("new.order.line.dialog.title"), EditOrderLineDialog.class, articles, units, line);
	}

	@FXML
	protected void handlePrintInvoice(ActionEvent event) {
		doInBackground(new GenerateReportTaskCreator(ViewInvoiceActivity.PRINT_INVOICE, getEntity(), invoiceAddressValue, deliveryAddressValue, articles, orderLineTable.getItems()));
	}

  @FXML
 	protected void handleChangeAmounts(ActionEvent event) {
		showDialog(getResources().getString("change.prices.dialog.title"), EditInvoicePricesDialog.class, getEntity());
 	}

  @FXML
 	protected void handlePrintReminder(ActionEvent event) {
    doInBackground(new GenerateReportTaskCreator(ViewInvoiceActivity.PRINT_REMINDER, getEntity(), invoiceAddressValue, deliveryAddressValue, articles, orderLineTable.getItems()));
 	}

	@Override
	public void initialize() {
		super.initialize();
		invoiceAddressField.setResources(getResources());
		deliveryAddressField.setResources(getResources());

		idColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Long>("id"));
		discountColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Float>("unitDiscount"));
		quantityColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Integer>("quantity"));
    priceColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Float>("articlePrice"));
    freeQColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Integer>("articleFreeQuantity"));
    articleNameColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, String>("articleName"));
    codeColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, String>("articleCode"));
    lineTotalColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Float>("lineTotal"));

		actionsColumn.setCellValueFactory(new MyPropertyValueFactory<OrderLineProperty, Long>("id"));
		actionsColumn.setCellFactory(new Callback<TableColumn<OrderLineProperty, Long>, TableCell<OrderLineProperty, Long>>() {

			@Override
			public TableCell<OrderLineProperty, Long> call(TableColumn<OrderLineProperty, Long> param) {
				return new TableCell<OrderLineProperty, Long>() {
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
		});

	}

	@Override
	protected void mapFields(Invoice viewingEntity) {
		super.mapFields(viewingEntity);
		title.setText(String.format(getResources().getString("view.invoice"), viewingEntity.getCode()));
		invoiceAddressField.setValue(invoiceAddressValue);
		deliveryAddressField.setValue(deliveryAddressValue);
		idField.setText(String.valueOf(viewingEntity.getId()));
		customerField.setText(viewingEntity.getCustomer().getName());
    shippingField.setText(""+viewingEntity.getShipping());
    productsField.setText(""+viewingEntity.getProducts());
    totalField.setText(viewingEntity.getTotalPrice());
		createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(viewingEntity.getCreationDate()));
		//orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
	}

  private void updatePriceFields(Invoice modifiedEntity){
    shippingField.setText(""+modifiedEntity.getShipping());
    productsField.setText(""+modifiedEntity.getProducts());
    totalField.setText(modifiedEntity.getTotalPrice());
  }

	@Override
  @SuppressWarnings("unchecked")
	protected void doCustomBackgroundInitialization(Invoice entity) {
		if (entity.getInvoiceAddress() != null) {
			invoiceAddressValue = getRestApiAccessor().get(entity.getInvoiceAddress(), Address.class);
		}
		if (entity.getDeliveryAddress() != null) {
			deliveryAddressValue = getRestApiAccessor().get(entity.getDeliveryAddress(), Address.class);
		}
		if (shouldReloadArticles()) {
			articles = (List<Article>)getRestApiAccessor().getAllArticless().getData();
      setReloadArticles(false);
		}
    orderLines = new ArrayList<OrderLineProperty>();
    List<OrderLine> lines = (List<OrderLine>)getRestApiAccessor().getResponse(OrderLine.class, null, null, null, "id", "invoice="+entity.getId(), true, false, true).getData();
    for (OrderLine line : lines) {
      orderLines.add(new OrderLineProperty(line));
    }
    orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
		//orderLineTable.getItems().addAll(orderLines);
		if (CollectionUtils.isEmpty(units)) {
			units = (List<Unit>)getRestApiAccessor().getResponse(Unit.class, false).getData();
		}
	}

	@Subscribe
	public void onSaveOrderLine(SaveOrderLineEvent event) {
		doInBackground(new SaveOrderLineTaskCreator(event.getEntity()));
	}

  @Subscribe
 	public void onChangeInvoicePrices(ChangeInvoicePricesEvent event) {
 		doInBackground(new SaveInvoiceTaskCreator(event.getEntity()));
 	}

	private int findOrderLineIndex(Long orderLineId) {
    int counter = -1;
		for (OrderLineProperty orderLine : orderLines) {
      counter++;
			if (orderLine.getId().equals(orderLineId)) {
				return counter;
			}
		}
		throw new IllegalStateException("Can't find OrderLineProperty with id=" + orderLineId);
	}

  private boolean hasOrderLine(OrderLineProperty orderLineProperty) {
 		for (OrderLineProperty orderLine : orderLines) {
 			if (orderLine.getId().equals(orderLineProperty.getId())) {
 				return true;
 			}
 		}
 		return false;
 	}
  
  private void updateInvoiceData(){
    doInBackground(new LoadInvoiceTaskCreator());
  }
}
