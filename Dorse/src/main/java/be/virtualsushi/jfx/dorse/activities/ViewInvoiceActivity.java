package be.virtualsushi.jfx.dorse.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import be.virtualsushi.jfx.dorse.control.ValidationErrorPanel;
import javafx.beans.property.ObjectProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.apache.commons.collections.CollectionUtils;
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
import be.virtualsushi.jfx.dorse.model.Address;
import be.virtualsushi.jfx.dorse.model.Article;
import be.virtualsushi.jfx.dorse.model.Invoice;
import be.virtualsushi.jfx.dorse.model.OrderLine;
import be.virtualsushi.jfx.dorse.model.Unit;
import be.virtualsushi.jfx.dorse.report.ReportService;
import be.virtualsushi.jfx.dorse.restapi.DorseBackgroundTask;

import com.google.common.eventbus.Subscribe;

@Component
@Scope("prototype")
public class ViewInvoiceActivity extends AbstractViewEntityActivity<VBox, Invoice> {

	private class ActionsBar extends HBox {

		public ActionsBar(final Long itemId) {
			super();
			setSpacing(3);

			EditButton editButton = new EditButton();
			editButton.setTooltip(new Tooltip(getResources().getString("edit")));
			editButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					showDialog(getResources().getString("edit.order.line.dialog.title"), EditOrderLineDialog.class, articles, units, findOrderLine(itemId));
				}
			});

			DeleteButton deleteButton = new DeleteButton();
			deleteButton.setTooltip(new Tooltip(getResources().getString("delete")));
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					doInBackground(new DeleteOrderLineTaskCreator(findOrderLine(itemId)));
				}
			});
			getChildren().add(editButton);
			getChildren().add(deleteButton);
		}

	}

	private class SaveOrderLineTaskCreator implements TaskCreator<DorseBackgroundTask<List<OrderLine>>> {

		private final OrderLine orderLine;

		public SaveOrderLineTaskCreator(OrderLine orderLine) {
			this.orderLine = orderLine;
		}

		@Override
		public DorseBackgroundTask<List<OrderLine>> createTask() {
			return new DorseBackgroundTask<List<OrderLine>>(this, orderLine) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected List<OrderLine> call() throws Exception {
					getRestApiAccessor().save((OrderLine) getParameters()[0]);
					return getRestApiAccessor().getList(OrderLine.class, null, null, "id", "order="+getEntity().getId(), true, false);
				}

				@Override
				protected void onSuccess(List<OrderLine> value) {
					orderLines = value;
					orderLineTable.setItems(FXCollections.observableArrayList(new ArrayList<OrderLine>()));
					orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
					hideLoadingMask();
				}

			};
		}

	}

	private class DeleteOrderLineTaskCreator implements TaskCreator<DorseBackgroundTask<List<OrderLine>>> {

		private final OrderLine entity;

		public DeleteOrderLineTaskCreator(OrderLine orderLine) {
			this.entity = orderLine;
		}

		@Override
		public DorseBackgroundTask<List<OrderLine>> createTask() {
			return new DorseBackgroundTask<List<OrderLine>>(this, entity) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@Override
				protected List<OrderLine> call() throws Exception {
					getRestApiAccessor().delete((OrderLine) getParameters()[0]);
          return getRestApiAccessor().getList(OrderLine.class, null, null, "id", "order="+getEntity().getId(), true, false);
				}

				@Override
				protected void onSuccess(List<OrderLine> value) {
					orderLines = value;
					orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
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
		private final List<OrderLine> orderLines;

		public GenerateReportTaskCreator(Invoice invoice, Address invoiceAddress, Address deliveryAddress, List<Article> articles, List<OrderLine> orderLines) {
			this.invoice = invoice;
			this.invoiceAddress = invoiceAddress;
			this.deliveryAddress = deliveryAddress;
			this.articles = articles;
			this.orderLines = orderLines;
		}

		@Override
		public DorseBackgroundTask<String> createTask() {
			return new DorseBackgroundTask<String>(this, invoice, invoiceAddress, deliveryAddress, articles, orderLines) {

				@Override
				protected void onPreExecute() {
					showLoadingMask();
				}

				@SuppressWarnings("unchecked")
				@Override
				protected String call() throws Exception {
					return reportService.createInvoiceReport((Invoice) getParameters()[0], (Address) getParameters()[1], (Address) getParameters()[2],
							(List<Article>) getParameters()[3], (List<OrderLine>) getParameters()[4]);
				}

				@Override
				protected void onSuccess(String value) {
					hideLoadingMask();
					getEventBus().post(new ShowReportEvent(value));
				}
			};
		}

	}

	@Autowired
	private ReportService reportService;

	@FXML
	private Label idField, customerField, createdField;

	@FXML
	private ViewAddressControl invoiceAddressField, deliveryAddressField;

	@FXML
	private TableView<OrderLine> orderLineTable;

	@FXML
	private TableColumn<OrderLine, Long> idColumn, actionsColumn;

	@FXML
	private TableColumn<OrderLine, String> codeColumn, articleNameColumn;

	@FXML
	private TableColumn<OrderLine, Float> priceColumn, discountColumn, lineTotalColumn;

	@FXML
	private TableColumn<OrderLine, Integer> quantityColumn;

	private Address invoiceAddressValue, deliveryAddressValue;

	private List<OrderLine> orderLines;

	private List<Article> articles;

	private List<Unit> units;

	@FXML
	protected void handleAddOrderLine(ActionEvent event) {
		showDialog(getResources().getString("new.order.line.dialog.title"), EditOrderLineDialog.class, articles, units, idField.getText());
	}

	@FXML
	protected void handlePrintInvoice(ActionEvent event) {
		doInBackground(new GenerateReportTaskCreator(getEntity(), invoiceAddressValue, deliveryAddressValue, articles, orderLines));
	}

	@Override
	public void initialize() {
		super.initialize();

		invoiceAddressField.setResources(getResources());
		deliveryAddressField.setResources(getResources());

		idColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Long>("id"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Float>("discount"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("quantity"));

		priceColumn.setCellValueFactory(new EntityPropertyValueFactory<OrderLine, Float>() {

			@Override
			protected void setPropertyValue(ObjectProperty<Float> property, OrderLine value) {
				property.set(findArticleById(value.getArticle()).getPrice());
			}
		});

		codeColumn.setCellValueFactory(new EntityStringPropertyValueFactory<OrderLine>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, OrderLine value) {
				property.set(findArticleById(value.getArticle()).getCode());
			}
		});

		discountColumn.setCellValueFactory(new EntityPropertyValueFactory<OrderLine, Float>() {

			@Override
			protected void setPropertyValue(ObjectProperty<Float> property, OrderLine value) {
				property.set(value.getUnitDiscount());
			}
		});

		lineTotalColumn.setCellValueFactory(new EntityPropertyValueFactory<OrderLine, Float>() {

			@Override
			protected void setPropertyValue(ObjectProperty<Float> property, OrderLine value) {
				Article article = findArticleById(value.getArticle());
				if (article.getPrice() != null && value.getQuantity() != null) {
					property.set((article.getPrice() - (value.getUnitDiscount() != null ? value.getUnitDiscount() : 0)) * value.getQuantity());
				}
			}
		});

		articleNameColumn.setCellValueFactory(new EntityStringPropertyValueFactory<OrderLine>() {

			@Override
			protected void setPropertyValue(ObjectProperty<String> property, OrderLine value) {
				Article currentArticle = findArticleById(value.getArticle());
        if(currentArticle.getName() == null)
          property.set(currentArticle.getDescription());
        else
          property.set(currentArticle.getName());
			}
		});

		actionsColumn.setCellValueFactory(new PropertyValueFactory<OrderLine, Long>("id"));
		actionsColumn.setCellFactory(new Callback<TableColumn<OrderLine, Long>, TableCell<OrderLine, Long>>() {

			@Override
			public TableCell<OrderLine, Long> call(TableColumn<OrderLine, Long> param) {
				return new TableCell<OrderLine, Long>() {
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

	private Article findArticleById(Long articleId) {
		for (Article article : articles) {
			if (articleId.equals(article.getId())) {
				return article;
			}
		}
		throw new IllegalStateException("Article with id " + articleId + " doesn't exists.");
	}

	@Override
	protected void mapFields(Invoice viewingEntity) {
		super.mapFields(viewingEntity);
		title.setText(String.format(getResources().getString("view.invoice"), viewingEntity.getCode()));
		invoiceAddressField.setValue(invoiceAddressValue);
		deliveryAddressField.setValue(deliveryAddressValue);
		idField.setText(String.valueOf(viewingEntity.getId()));
		customerField.setText(viewingEntity.getCustomer().getName());
		createdField.setText(new SimpleDateFormat(getResources().getString("date.format")).format(viewingEntity.getCreationDate()));
		orderLineTable.setItems(FXCollections.observableArrayList(orderLines));
	}

	@Override
	protected void doCustomBackgroundInitialization(Invoice entity) {
		if (entity.getInvoiceAddress() != null) {
			invoiceAddressValue = getRestApiAccessor().get(entity.getInvoiceAddress(), Address.class);
		}
		if (entity.getDeliveryAddress() != null) {
			deliveryAddressValue = getRestApiAccessor().get(entity.getDeliveryAddress(), Address.class);
		}
		if (CollectionUtils.isEmpty(articles)) {
			articles = getRestApiAccessor().getList(Article.class, null, null, "name", null, true, true);
		}
		orderLines = getRestApiAccessor().getList(OrderLine.class, null, null, "id", "invoice="+entity.getId(), true, false);
		if (CollectionUtils.isEmpty(units)) {
			units = getRestApiAccessor().getList(Unit.class, false);
		}
	}

	@Subscribe
	public void onSaveOrderLine(SaveOrderLineEvent event) {
		doInBackground(new SaveOrderLineTaskCreator(event.getEntity()));
	}

	private OrderLine findOrderLine(Long orderLineId) {
		for (OrderLine orderLine : orderLines) {
			if (orderLine.getId().equals(orderLineId)) {
				return orderLine;
			}
		}
		throw new IllegalStateException("Can't find OrderLine with id=" + orderLineId);
	}

}
