package be.virtualsushi.jfx.dorse.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 28/10/12
 * Time: 06:46
 */
public class OrderLineProperty {


 	private SimpleLongProperty id;
  //private ArticleProperty article;
 	private SimpleIntegerProperty quantity;
 	private SimpleFloatProperty unitDiscount;
  private SimpleLongProperty orderId;
  private SimpleLongProperty articleId;
  private SimpleStringProperty articleName; 
  private SimpleFloatProperty articlePrice; 
  private SimpleStringProperty articleCode;
  private SimpleFloatProperty lineTotal;
  

  public SimpleLongProperty idProperty() {
    if (id == null) id = new SimpleLongProperty(this, "id");
    return id;
  }

  public SimpleIntegerProperty quantityProperty() {
    if (quantity == null) quantity = new SimpleIntegerProperty(this, "quantity");
    return quantity;
  }

  public SimpleFloatProperty unitDiscountProperty() {
    if (unitDiscount == null) unitDiscount = new SimpleFloatProperty(this, "unitDiscount");
    return unitDiscount;
  }

  public SimpleFloatProperty lineTotalProperty() {
    if (lineTotal == null) lineTotal = new SimpleFloatProperty(this, "lineTotal");
    return lineTotal;
  }

  public SimpleLongProperty orderIdProperty() {
    if (orderId == null) orderId = new SimpleLongProperty(this, "orderId");
    return orderId;
  }
  
  public SimpleLongProperty articleIdProperty() {
    if (articleId == null) articleId = new SimpleLongProperty(this, "articleId");
    return articleId;
  }
  
  public SimpleStringProperty articleNameProperty() {
    if (articleName == null) articleName = new SimpleStringProperty(this, "articleName");
    return articleName;
  }    
  
  public SimpleStringProperty articleCodeProperty() {
    if (articleCode == null) articleCode = new SimpleStringProperty(this, "articleCode");
    return articleCode;
  }  

  public SimpleFloatProperty articlePriceProperty() {
    if (articlePrice == null) articlePrice = new SimpleFloatProperty(this, "articlePrice");
    return articlePrice;
  }

  public OrderLineProperty(OrderLine line) {
    if(line.getId()!=null)
      idProperty().set(line.getId());
    if(line.getOrderId()!=null)
      orderIdProperty().set(line.getOrderId());
    if(line.getQuantity()!=null)
      quantityProperty().set(line.getQuantity());
    if(line.getUnitDiscount()!=null)
      unitDiscountProperty().set(line.getUnitDiscount());
    if(line.getArticle()!=null){
      if(line.getArticle().getId()!=null)
        articleIdProperty().set(line.getArticle().getId());
      if(line.getArticle().getName()!=null)
        articleNameProperty().set(line.getArticle().getName());
      if(line.getArticle().getCode()!=null)
        articleCodeProperty().set(line.getArticle().getCode());
      if(line.getArticle().getPrice()!=null)
        articlePriceProperty().set(line.getArticle().getPrice());
      float formatted;
      formatted = quantityProperty().get()*(articlePriceProperty().get()-unitDiscountProperty().get());
      formatted = ((float)((int)(formatted*100)))/100;
      lineTotal = new SimpleFloatProperty(formatted);
    }
  }

  public OrderLineProperty() {
  }

  public Long getId() {
    return idProperty().get();
  }

  public void setId(Long id) {
    idProperty().set(id);
  } 

  public Long getOrderId() {
    return orderIdProperty().get();
  }

  public void setOrderId(Long orderId) {
    orderIdProperty().set(orderId);
  }

  public Integer getQuantity() {
    return quantityProperty().get();
  }

  public void setQuantity(Integer quantity) {
    quantityProperty().set(quantity);
  }

  public Float getUnitDiscount() {
    return unitDiscountProperty().get();
  }

  public void setUnitDiscount(Float unitDiscount) {
    if(unitDiscount!=null)
      unitDiscountProperty().set(unitDiscount);
    else
      unitDiscountProperty().set(0);
  }     

  public Float getLineTotal() {
    return lineTotalProperty().get();
  }

  public void setLineTotal(Float lineTotal) {
    lineTotalProperty().set(lineTotal);
  }
  
  public Long getArticleId(){
    return articleIdProperty().get();
  }

  public void setArticleId(Long articleId) {
    articleIdProperty().set(articleId);
  }   
  
  public String getArticleName(){
    return articleNameProperty().get();
  }

  public void setArticleName(String articleName) {
    articleNameProperty().set(articleName);
  }    
    
  public String getArticleCode(){
    return articleCodeProperty().get();
  }

  public void setArticleCode(String articleCode) {
    articleCodeProperty().set(articleCode);
  }

  public Float getArticlePrice(){
    return articlePriceProperty().get();
  }

  public void setArticlePrice(Float articlePrice) {
    articlePriceProperty().set(articlePrice);
  }

}
