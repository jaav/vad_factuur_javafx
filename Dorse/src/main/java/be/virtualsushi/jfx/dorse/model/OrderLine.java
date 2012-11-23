package be.virtualsushi.jfx.dorse.model;

import javax.validation.constraints.NotNull;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.jackson.annotate.JsonProperty;

import be.virtualsushi.jfx.dorse.restapi.ItemResourcePath;
import be.virtualsushi.jfx.dorse.restapi.ListResourcePath;

@ItemResourcePath("invoiceLine")
@ListResourcePath("invoiceLines")
public class OrderLine extends BaseEntity {

	@NotNull
	private Article article;

	/*@NotNull
	private Float discount;*/

	@NotNull
	private Integer quantity;

	@JsonProperty("unit_discount")
	private Float unitDiscount;

  @JsonProperty("order_id")
 	@NotNull
 	private Long orderId;

  @JsonProperty("apply_free")
 	private Boolean applyFree;

  private Float lineTotal;

  public OrderLine(OrderLineProperty lineProperty) {
    this.article = new Article();
    this.article.setId(lineProperty.getArticleId());
    this.article.setCode(lineProperty.getArticleCode());
    this.article.setArticleName(lineProperty.getArticleName());
    this.article.setPrice(lineProperty.getArticlePrice());
    this.article.setFreeQuantity(lineProperty.getArticleFreeQuantity());
    this.orderId = lineProperty.getOrderId();
    this.quantity = lineProperty.getQuantity();
    this.unitDiscount = lineProperty.getUnitDiscount();
    this.applyFree = lineProperty.getApplyFree();
    this.setId(lineProperty.getId());
  }

  public OrderLine() {
  }

  public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getUnitDiscount() {
		return unitDiscount;
	}

	public void setUnitDiscount(Float unitDiscount) {
		this.unitDiscount = unitDiscount;
	}

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Float getLineTotal() {
    return lineTotal;
  }

  public void setLineTotal(Float lineTotal) {
    this.lineTotal = lineTotal;
  }

  public Boolean getApplyFree() {
    return applyFree;
  }

  public void setApplyFree(Boolean applyFree) {
    this.applyFree = applyFree;
  }
}
