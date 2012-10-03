package be.virtualsushi.jfx.dorse.model.report;

import be.virtualsushi.jfx.dorse.model.OrderLine;

public class OrderLineReport {

	private OrderLine orderLine;

	private String articleName;

	private Float price;

	private String code;

	public OrderLine getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLine orderLine) {
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
