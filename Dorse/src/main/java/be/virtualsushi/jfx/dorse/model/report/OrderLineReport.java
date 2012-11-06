package be.virtualsushi.jfx.dorse.model.report;

import be.virtualsushi.jfx.dorse.model.OrderLineProperty;

public class OrderLineReport {

	private OrderLineProperty orderLine;

	private String articleName;

	private Float price;

	private String code;

	public OrderLineProperty getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLineProperty orderLine) {
		this.orderLine = orderLine;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
