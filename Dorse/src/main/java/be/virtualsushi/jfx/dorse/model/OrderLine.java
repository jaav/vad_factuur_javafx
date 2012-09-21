package be.virtualsushi.jfx.dorse.model;

import org.codehaus.jackson.annotate.JsonProperty;

import be.virtualsushi.jfx.dorse.restapi.ItemResourcePath;
import be.virtualsushi.jfx.dorse.restapi.ListResourcePath;

@ItemResourcePath("invoiceLine")
@ListResourcePath("invoiceLinesByInvoice/{id}")
public class OrderLine extends BaseEntity {

	private Long article;

	private Float discount;

	private Integer quantity;

	@JsonProperty("unit_discount")
	private Float unitDiscount;

	@JsonProperty("unit_price")
	private Float unitPrice;

	public Long getArticle() {
		return article;
	}

	public void setArticle(Long article) {
		this.article = article;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
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

	public Float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

}
