package be.virtualsushi.jfx.dorse.model;

import be.virtualsushi.jfx.dorse.restapi.ListResourcePath;

@ListResourcePath("invoiceLineByInvoice/{id}")
public class OrderLine extends BaseEntity {

	private Long article;
	private Float discount;
	private Integer quantity;

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

}
