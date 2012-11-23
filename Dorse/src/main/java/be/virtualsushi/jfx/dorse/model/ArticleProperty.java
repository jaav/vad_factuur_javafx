package be.virtualsushi.jfx.dorse.model;

import javafx.beans.property.*;

/**
 * Created on IntelliJ
 * User: Jef Waumans for Virtual Sushi
 * Date: 05/11/12
 * Time: 12:45
 */
public class ArticleProperty {


  private SimpleLongProperty id;
  private SimpleFloatProperty price;
  private SimpleStringProperty code;
  private SimpleStringProperty name;

  public ArticleProperty() {
  }

  public ArticleProperty(Article article) {
    idProperty().set(article.getId());
    priceProperty().set(article.getPrice());
    nameProperty().set(article.getArticleName());
    codeProperty().set(article.getCode());
  }

  public LongProperty idProperty() {
    if (id == null) id = new SimpleLongProperty(this, "id");
    return id;
  }

  public FloatProperty priceProperty() {
    if (price == null) price = new SimpleFloatProperty(this, "price");
    return price;
  }

  public StringProperty codeProperty() {
    if (code == null) code = new SimpleStringProperty(this, "code");
    return code;
  }

  public StringProperty nameProperty() {
    if (name == null) name = new SimpleStringProperty(this, "name");
    return name;
  }

  public String getCode() {
    return codeProperty().get();
  }

  public void setCode(String code) {
    codeProperty().set(code);
  }

  public String getName() {
    return nameProperty().get();
  }

  public void setName(String name) {
    nameProperty().set(name);
  }

  public Long getId() {
    return idProperty().get();
  }

  public void setId(Long id) {
    idProperty().set(id);
  }    

  public Float getPrice() {
    return priceProperty().get();
  }

  public void setPrice(Float price) {
    priceProperty().set(price);
  }

  public Article get(){
    Article a = new Article();
    a.setCode(this.getCode());
    a.setArticleName(this.getName());
    a.setId(this.getId());
    a.setPrice(this.getPrice());
    return a;
  }

  public void set(Article article) {
    idProperty().set(article.getId());
    priceProperty().set(article.getPrice());
    nameProperty().set(article.getArticleName());
    codeProperty().set(article.getCode());
  }
}
